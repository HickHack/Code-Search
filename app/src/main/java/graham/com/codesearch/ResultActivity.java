package graham.com.codesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import graham.com.codesearch.search.RepoMapAdapter;
import graham.com.codesearch.search.model.Repo;

/**
 * Results activity that is used to display and
 * an individual search item from the search activity
 */
public class ResultActivity extends AppCompatActivity {

    private Repo result;
    private RepoMapAdapter adapter;
    private Map<String, String> repoData;
    private ListView listView;
    private TextView description;
    private TextView repoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Configure the action bar so it has a back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        repoData = new HashMap<>();

        //Get the result item sent from the search activity
        Repo result = (Repo) getIntent().getSerializableExtra("result");

        if (result != null) {
            this.result = result;

            populateRepoItems();
            setupRepoName();
            setupImage();
            setupDescription();
            configureListView();
            configureBrowserFab();
            configureMessageFab();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }

    private void setupImage() {
        ImageView projectImage = (ImageView) findViewById(R.id.projectImage);

        if (result.getOwner().getImage() != null) {
            //Get the image byte array and decode it
            byte[] imageArray = result.getOwner().getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            projectImage.setImageBitmap(bmp);
        }
    }

    private void setupDescription() {
        description = (TextView) findViewById(R.id.repoDescription);
        description.setText(result.getDescription());
    }

    private void setupRepoName() {
        repoName = (TextView) findViewById(R.id.repoName);
        repoName.setText(result.getName());
    }

    private void configureListView() {
        listView = (ListView) findViewById(R.id.repoListView);

        adapter = new RepoMapAdapter(this, repoData);
        listView.setAdapter(adapter);
    }

    private void populateRepoItems() {
        //Add the result items to a map
        repoData.put(getString(R.string.repo_key_owner), result.getOwner().getUsername());
        repoData.put(getString(R.string.repo_key_created), result.getCreated());
        repoData.put(getString(R.string.repo_key_updated), result.getUpdated());
        repoData.put(getString(R.string.repo_key_watchers), String.valueOf(result.getWatchers()));
        repoData.put(getString(R.string.repo_key_language), result.getLanguage());
        repoData.put(getString(R.string.repo_key_open_issues), String.valueOf(result.getOpenIssueCount()));
        repoData.put(getString(R.string.repo_key_forks), String.valueOf(result.getForks()));
    }

    private void configureBrowserFab() {
        //Setup the floating action button used to launch the web view
        FloatingActionButton fabBrowser = (FloatingActionButton) findViewById(R.id.fabBrowser);
        fabBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBrowser();
            }
        });
    }

    private void launchBrowser() {
        //Launch the browser activity and pass over the repo url
        Intent intent = new Intent(getApplicationContext(), BrowserActivity.class);
        intent.putExtra("url", result.getProfileUrl());
        startActivity(intent);
    }

    private void configureMessageFab() {
        //Setup the floating action button for sending a message to a friend
        FloatingActionButton fabText = (FloatingActionButton) findViewById(R.id.fabText);
        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMessageService();
            }
        });
    }

    private void launchMessageService() {
        //Launch the implicit intent for sending an SMSs
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", getMessageBody());
        startActivity(sendIntent);
    }

    private String getMessageBody() {
        String body = getString(R.string.message_body);

        return String.format(body, result.getName(), result.getProfileUrl());
    }
}
