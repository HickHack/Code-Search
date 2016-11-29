package graham.com.codesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import graham.com.codesearch.search.Repo;
import graham.com.codesearch.search.Results;
import graham.com.codesearch.search.SearchFetcher;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Search View Menu Inflation
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        //Search Repo Menu Inflation
        getMenuInflater().inflate(R.menu.list_menu, menu);

        return true;
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

        SearchFetcher fetcher = new SearchFetcher(this, listView);
        fetcher.execute(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
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
}
