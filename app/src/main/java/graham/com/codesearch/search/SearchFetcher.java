package graham.com.codesearch.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import graham.com.codesearch.R;

/**
 * Created by Graham Murray on 16/11/16.
 */

public class SearchFetcher extends AsyncTask<String, Void, Results> {

    private static String RESOURCE = "https://api.github.com/search/repositories?q=%s&sort=stars&order=desc";
    private HttpURLConnection urlConnection;
    private InputStream inputStream;
    private ProgressDialog progress;
    private Context context;
    private ListView listView;
    private CustomAdapter adapter;

    public SearchFetcher(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(context);
        progress.setTitle("Github Search Retrieval");
        progress.setMessage("Fetching Results...");
        progress.show();
    }

    @Override
    protected void onPostExecute(Results results) {
        super.onPostExecute(results);
        progress.dismiss();
        populateResultList(results);
    }

    @Override
    protected Results doInBackground(String... params) {
        String url = params[0];
        return getJSON(url);
    }

    private Results getJSON(String query) {
        Results results = new Results();

        try {
            URL url = new URL(String.format(RESOURCE, query));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    inputStream = url.openStream();

                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(inputStream);
                    results = gson.fromJson(reader, Results.class);

                    getImages(results);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return results;
    }

    private void getImages(Results results) {
        int counter = 0;
        for (Repo tmp: results.getRepos()) {
            byte[] image = getImage(tmp.getOwner().getAvatarUrl());

            if (image != null) {
                results.getRepos().get(counter).getOwner().setImage(image);
            }

            counter++;
        }
    }

    private byte[] getImage(String link) {

        try {
            if (!link.isEmpty()) {
                URL url = new URL(link);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                inputStream = url.openStream();
                return IOUtils.toByteArray(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void populateResultList(Results results) {
        adapter = new CustomAdapter(context, R.layout.listview_search_item, results.getRepos());
        listView.setAdapter(adapter);
    }
}