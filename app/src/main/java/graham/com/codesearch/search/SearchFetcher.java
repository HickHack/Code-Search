package graham.com.codesearch.search;

import android.app.Notification;
import android.app.NotificationManager;
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
import graham.com.codesearch.SearchActivity;
import graham.com.codesearch.search.model.Repo;
import graham.com.codesearch.search.model.Results;

/**
 * Created by Graham Murray on 16/11/16.
 */

public class SearchFetcher extends AsyncTask<String, Void, Results> {

    private static String RESOURCE = "https://api.github.com/search/repositories?q=%s&sort=stars&order=desc";
    private HttpURLConnection urlConnection;
    private InputStream inputStream;
    private ProgressDialog progress;
    private Context context;
    private SearchActivity activity;
    private ListView listView;
    private SearchAdapter adapter;

    public SearchFetcher(Context context, SearchActivity activity, ListView listView) {
        this.context = context;
        this.activity = activity;
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
        generateNotification(results.getRepos().size());
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

            if (status == 200 || status == 201) {
                inputStream = url.openStream();

                Gson gson = new Gson();
                Reader reader = new InputStreamReader(inputStream);
                results = gson.fromJson(reader, Results.class);

                getImages(results);
            } else {
                String error = context.getResources().getString(R.string.search_error);
                activity.changeSearchMessage(error);
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
        adapter = new SearchAdapter(context, R.layout.listview_search_item, results.getRepos());
        listView.setAdapter(adapter);
        activity.hideSearchMessage();
        activity.setNumberOfResults(results.getRepos().size());
    }

    private void generateNotification(int numberResults) {
        String title = context.getResources().getString(R.string.search_complete);
        String body = numberResults + " " + context.getResources().getString(R.string.results_fetched);
        String subject = context.getResources().getString(R.string.app_name);

        NotificationManager notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(context).setContentTitle(title)
                                    .setContentText(body).setContentTitle(subject)
                                        .setSmallIcon(R.drawable.ic_github_white).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.notify(0, notify);
    }
}
