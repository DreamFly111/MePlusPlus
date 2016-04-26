package io.agora.sample.agora;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by apple on 15/9/23.
 */
public class WebActivity extends BaseEngineHandlerActivity {
    private WebView mWebView;

    public final static String AGORA_URL_GETKEY = "http://www.agora.io";
    public final static String AGORA_URL_FAQ = "http://www.agora.io/faq/";
    public final static String EXTRA_KEY_URL = "EXTRA_KEY_URL";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        setContentView(R.layout.agora_activity_web);
        initViews();
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        setupWebView(url);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserInteraction(View view) {
        int id = view.getId();
        if (id == R.id.web_back) {
            finish();
        } else {
            super.onUserInteraction(view);
        }
    }

    private void initViews() {
        mWebView = (WebView) findViewById(R.id.web_webView);
        String url = getIntent().getStringExtra(EXTRA_KEY_URL);
        if (url.equals(AGORA_URL_GETKEY)) {
            ((TextView) findViewById(R.id.web_title)).setText(getString(R.string.web_key));
        } else if (url.equals(AGORA_URL_FAQ)) {
            ((TextView) findViewById(R.id.web_title)).setText(getString(R.string.web_issue));
        } else {
            ((TextView) findViewById(R.id.web_title)).setText(getString(R.string.record_quality));
        }
        findViewById(R.id.web_back).setOnClickListener(getViewClickListener());
    }

    private void setupWebView(String url) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }
}
