package graham.com.codesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import graham.com.codesearch.search.Repo;

public class ResultActivity extends AppCompatActivity {

    private Repo result;
    private TextView descriptionTextView;
    private TextView repoNameTextView;
    private TextView repoOwnerTextView;
    private TextView updatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabText = (FloatingActionButton) findViewById(R.id.fabText);
        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tell a friend", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Repo result = (Repo) getIntent().getSerializableExtra("result");

        if (result != null) {
            this.result = result;

            setupImage();
            setupDescriptionTextView();
            setupRepoNameTextView();
            setupOwnerNameTextView();
            setupUpdatedTextView();
            configureBrowserFab();
        }
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

    private void setupUpdatedTextView() {
        updatedTextView = (TextView) findViewById(R.id.lastUpdated);
        updatedTextView.setText(result.getUpdated());
    }

    private void configureBrowserFab() {
        FloatingActionButton fabBrowser = (FloatingActionButton) findViewById(R.id.fabBrowser);
        fabBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BrowserActivity.class);
                intent.putExtra("url", result.getOwner().getHtmlUrl());
                startActivity(intent);
            }
        });
    }

}
