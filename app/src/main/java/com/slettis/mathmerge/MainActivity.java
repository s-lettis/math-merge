package com.slettis.mathmerge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private WebView web;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF14141F);

        web = new WebView(this);
        web.setBackgroundColor(0xFF14141F);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        s.setTextZoom(100);
        web.setWebViewClient(new WebViewClient());
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web.addJavascriptInterface(new Bridge(), "Android");

        root.addView(web, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        // Keep the game clear of the status bar / nav bar (Android 15+ draws edge-to-edge).
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            if (Build.VERSION.SDK_INT >= 30) {
                android.graphics.Insets b = insets.getInsets(
                        WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                v.setPadding(b.left, b.top, b.right, b.bottom);
                return WindowInsets.CONSUMED;
            }
            return insets;
        });

        setContentView(root);
        web.loadUrl("file:///android_asset/index.html");

        tts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            tts.setLanguage(Locale.US);
            tts.setSpeechRate(0.9f);
            tts.setPitch(1.1f);
            ttsReady = true;
        }
    }

    private class Bridge {
        @JavascriptInterface
        public int buildNumber() {
            return BuildConfig.VERSION_CODE;
        }

        // Asks GitHub for the newest release; answers via window.onUpdateInfo({build, url}) or null.
        @JavascriptInterface
        public void checkUpdate() {
            if (BuildConfig.REPO == null || BuildConfig.REPO.isEmpty()) return;
            new Thread(() -> {
                String result = "null";
                try {
                    URL u = new URL("https://api.github.com/repos/" + BuildConfig.REPO + "/releases/latest");
                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
                    c.setRequestProperty("Accept", "application/vnd.github+json");
                    c.setConnectTimeout(8000);
                    c.setReadTimeout(8000);
                    if (c.getResponseCode() == 200) {
                        StringBuilder sb = new StringBuilder();
                        try (BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
                            String line;
                            while ((line = r.readLine()) != null) sb.append(line);
                        }
                        JSONObject j = new JSONObject(sb.toString());
                        String tag = j.optString("tag_name", "");
                        int build = Integer.parseInt(tag.replaceAll("[^0-9]", ""));
                        String url = j.optString("html_url", "");
                        JSONArray assets = j.optJSONArray("assets");
                        if (assets != null) {
                            for (int i = 0; i < assets.length(); i++) {
                                JSONObject a = assets.getJSONObject(i);
                                if (a.optString("name", "").endsWith(".apk")) {
                                    url = a.optString("browser_download_url", url);
                                    break;
                                }
                            }
                        }
                        JSONObject out = new JSONObject();
                        out.put("build", build);
                        out.put("url", url);
                        result = out.toString();
                    }
                    c.disconnect();
                } catch (Exception ignored) {
                }
                final String r = result;
                runOnUiThread(() -> {
                    if (web != null) web.evaluateJavascript("window.onUpdateInfo && window.onUpdateInfo(" + r + ")", null);
                });
            }).start();
        }

        // Opens the APK link in the browser, which downloads it; tapping it then offers "Update".
        @JavascriptInterface
        public void openUrl(String url) {
            runOnUiThread(() -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception ignored) {
                }
            });
        }

        @JavascriptInterface
        public void speak(String text) {
            if (ttsReady && tts != null) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "mm");
            }
        }

        @JavascriptInterface
        public void vibrate(int ms) {
            try {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null && v.hasVibrator()) {
                    v.vibrate(VibrationEffect.createOneShot(Math.max(1, ms), VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        // Let the game decide: close popup -> back to menu -> exit.
        web.evaluateJavascript(
                "(window.onAndroidBack && window.onAndroidBack()) ? '1' : '0'",
                value -> {
                    if (!"\"1\"".equals(value)) finish();
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (web != null) web.onPause();
        if (tts != null) tts.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (web != null) web.onResume();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
        if (web != null) web.destroy();
        super.onDestroy();
    }
}
