package graham.com.codesearch;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {

    private WebView browser;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);

        this.url = (String) getIntent().getExtras().get("url");

        configureBrowser();
        navigateToSite();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }

    private void configureBrowser() {
        browser = (WebView) findViewById(R.id.webView);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new GitWebViewClient());
    }

    private void navigateToSite() {
        browser.loadUrl(url);
    }

    private class GitWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
