package graham.com.codesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {

    private WebView browser;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.url = (String) getIntent().getExtras().get("url");

        configureBrowser();
        navigateToSite();
    }

    private void configureBrowser() {
        browser = (WebView) findViewById(R.id.webView);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
