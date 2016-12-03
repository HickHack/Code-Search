package graham.com.codesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import graham.com.codesearch.search.model.Repo;

public class ResultActivity extends AppCompatActivity {

    private Repo result;
    private TextView descriptionTextView;
    private TextView repoNameTextView;
    private TextView repoOwnerTextView;
    private TextView createdTextView;
    private TextView updatedTextView;
    private TextView watchersTextView;
    private TextView languageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Repo result = (Repo) getIntent().getSerializableExtra("result");

        if (result != null) {
            this.result = result;

            setupImage();
            setupDescriptionTextView();
            setupRepoNameTextView();
            setupOwnerNameTextView();
            setupCreatedTextView();
            setupUpdatedTextView();
            setupWatchersTextView();
            setupLanguageTextView();
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
            byte[] imageArray = result.getOwner().getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            projectImage.setImageBitmap(bmp);
        }
    }

    private void setupDescriptionTextView() {
        descriptionTextView = (TextView) findViewById(R.id.repoDescription);
        descriptionTextView.setText(result.getDescription());
    }

    private void setupRepoNameTextView() {
        repoNameTextView = (TextView) findViewById(R.id.repoName);
        repoNameTextView.setText(result.getName());
    }

    private void setupOwnerNameTextView() {
        repoOwnerTextView = (TextView) findViewById(R.id.ownerName);
        repoOwnerTextView.setText(result.getOwner().getUsername());
    }

    private void setupCreatedTextView() {
        createdTextView = (TextView) findViewById(R.id.created);
        createdTextView.setText(result.getCreated());
    }
    private void setupUpdatedTextView() {
        updatedTextView = (TextView) findViewById(R.id.lastUpdated);
        updatedTextView.setText(result.getUpdated());
    }

    private void setupWatchersTextView() {
        watchersTextView = (TextView) findViewById(R.id.watchers);
        watchersTextView.setText(String.valueOf(result.getWatchers()));
    }

    private void setupLanguageTextView() {
        languageTextView = (TextView) findViewById(R.id.language);
        languageTextView.setText(result.getLanguage());
    }

    private void configureBrowserFab() {
        FloatingActionButton fabBrowser = (FloatingActionButton) findViewById(R.id.fabBrowser);
        fabBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBrowser();
            }
        });
    }

    private void launchBrowser() {
        Intent intent = new Intent(getApplicationContext(), BrowserActivity.class);
        intent.putExtra("url", result.getProfileUrl());
        startActivity(intent);
    }

    private void configureMessageFab() {
        FloatingActionButton fabText = (FloatingActionButton) findViewById(R.id.fabText);
        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMessageService();
            }
        });
    }

    private void launchMessageService() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", getMessageBody());
        startActivity(sendIntent);
    }

    private String getMessageBody() {
        String body = getResources().getString(R.string.message_body);

        return String.format(body, result.getName(), result.getProfileUrl());
    }
}
