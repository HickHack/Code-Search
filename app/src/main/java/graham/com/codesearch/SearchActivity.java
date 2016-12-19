package graham.com.codesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import graham.com.codesearch.search.SuggestionProvider;
import graham.com.codesearch.search.model.Repo;
import graham.com.codesearch.search.SearchFetcher;

/**
 * Search activity that is used for searching and displaying
 * the list of search results
 */
public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private Menu menu;
    private TextView searchMessage;
    private TextView numberOfResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        listView = (ListView) findViewById(R.id.listView);
        searchMessage = (TextView) findViewById(R.id.searchMessage);
        numberOfResults = (TextView) findViewById(R.id.numberOfResults);

        setupListViewItemClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        //Search View Menu Inflation
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.searchView);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        doSearch();

        //Search Repo Menu Inflation
        getMenuInflater().inflate(R.menu.list_menu, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void hideSearchMessage() {
        searchMessage.setText("");
    }

    public void changeSearchMessage(String text){
        searchMessage.setText(text);
    }

    public void setNumberOfResults(int resultCount) {
        String text = resultCount + " " + getResources().getString(R.string.result_count);
        numberOfResults.setText(text);
    }

    public void setupListViewItemClick() {
        //Configure the list view to handle an item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                Repo result = (Repo) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });
    }

    private void doSearch() {
        Intent intent = getIntent();

        searchView.clearFocus();

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Perform a search based on the result from the
            // search preferences
            String query = intent.getStringExtra(SearchManager.QUERY);
            runSearchThread(query);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //Perform a search from the query that came from the Search View
            //the onQuerySubmit can't be used as it causes problems with
            //the search preferences provider and causes two search activities
            //to be launched
            if (intent.getStringExtra(SearchManager.QUERY) != null) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                saveSuggestion(query);
                runSearchThread(query);
            }
        }
    }

    private void runSearchThread(String query) {
        //If there is a valid network connection
        //run the search fetcher thread to get the results
        if (isNetworkConnection()) {
            SearchFetcher fetcher = new SearchFetcher(this, this, listView);
            fetcher.execute(query);
        } else {
            displayNoNetworkConnectionMessage();
        }
    }

    private boolean isNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }

    private void displayNoNetworkConnectionMessage() {
        Toast.makeText(this, getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
    }

    private void saveSuggestion(String query) {
        //Store the search query so it can be retrieved again
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(), SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void clearHistory() {
        //Clear the search history items
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.clearHistory();
    }
}
