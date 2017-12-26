package com.srinivasanand.tvision;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by srinivasanand on 04/10/17.
 */

public class UrlLoad extends AppCompatActivity {
    public WebView a;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String s = getIntent().getStringExtra("id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_load);
        a = (WebView) findViewById(R.id.webview);
        WebSettings b = a.getSettings();
        b.setJavaScriptEnabled(true);
        a.loadUrl(s);
        a.setWebViewClient(new WebViewClient());


    }
    @Override
    public void onBackPressed() {
        if(a.canGoBack())
        {
            a.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        String name1=user1.getDisplayName();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Status").child(name1);
        ref.setValue("true");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        String name1=user1.getDisplayName();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Status").child(name1);
        ref.setValue("false");

    }
}
