package graham.com.codesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import graham.com.codesearch.search.SuggestionProvider;
import graham.com.codesearch.search.model.Repo;
import graham.com.codesearch.search.SearchFetcher;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        //Search View Menu Inflation
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.searchView);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setupListViewItemClick();
        searchView.clearFocus();

        SearchFetcher fetcher = new SearchFetcher(this, this, listView);
        fetcher.execute(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
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

    private void saveSuggestion(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(), SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void clearHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.clearHistory();
    }
}
