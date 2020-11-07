package com.dimonvideo.smarthome;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        WebView webview = this.findViewById(R.id.view_first);
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId("1:532425247202:android:c66c1918b93ebe684851bb") // Required for Analytics.
                    .setProjectId("smarthome-dv") // Required for Firebase Installations.
                    .build();
            FirebaseApp.initializeApp(MainActivity.this, options, "SmartHome");

                FirebaseMessaging.getInstance().subscribeToTopic("all");
        } catch (Throwable ignored) {
        }
        String page = "https://dom.dimon.me/esp3.php";
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);

        webview.setWebChromeClient(new WebChromeClient());

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(page);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String apiUrl = "https://dom.dimon.me/esp3.php?op=11";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String tempIn = jsonObject.getString("in");
                            String tempOut = jsonObject.getString("out");
                            ((TextView) toolbar.findViewById(R.id.toolbarIn)).setText(tempIn);
                            ((TextView) toolbar.findViewById(R.id.toolbarOut)).setText(tempOut);


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_network_timeout), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

        // light
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxRemove("https://dom.dimon.me/esp3.php?op=5");

            }
        });
        // vorota
        FloatingActionButton fab_2 = findViewById(R.id.fab_2);
        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxRemove("https://dom.dimon.me/esp3.php?op=7");
            }
        });
        // door
        FloatingActionButton fab_3 = findViewById(R.id.fab_3);
        fab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxRemove("https://dom.dimon.me/esp3.php?op=8");

            }
        });
        // nasos
        FloatingActionButton fab_4 = findViewById(R.id.fab_4);
        fab_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxRemove("https://dom.dimon.me/esp3.php?op=10");

            }
        });


    }

    public void AlertBoxRemove(final String url) {
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
        alt_bld.setMessage(getString(R.string.confirm_text))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(stringRequest);
                    }
                })
                .setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle(getString(R.string.confirm_title));
        alert.setIcon(R.mipmap.ic_launcher);
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            AlertBoxRemove("https://dom.dimon.me/esp3.php?op=9");
        }
        if (id == R.id.action_ogorod) {
            AlertBoxRemove("https://dom.dimon.me/esp3.php?op=6");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyLongPress(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyLongPress(keycode, event);
    }
}