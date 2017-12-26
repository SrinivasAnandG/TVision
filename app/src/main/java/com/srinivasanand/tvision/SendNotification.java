package com.srinivasanand.tvision;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by srinivasanand on 16/10/17.
 */

public class SendNotification extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);
        final EditText content;
        Button notify;
        content=(EditText) findViewById(R.id.notifyContent);
        notify=(Button)findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=content.getText().toString().trim();
                if(!msg.equals(""))
                {
                    DatabaseReference ref15= FirebaseDatabase.getInstance().getReference().child("User Token");
                    ref15.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String ,String> vals=(Map<String, String>) dataSnapshot.getValue();
                            for(Map.Entry m:vals.entrySet()){
                                final WebView mWebview=new WebView(SendNotification.this);
                                mWebview.getSettings().setJavaScriptEnabled(true);
                                mWebview.setWebViewClient(new WebViewClient() {
                                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    }
                                });
                                //System.out.println(m.getKey()+" "+m.getValue());
                                mWebview .loadUrl("https://tfaculty.000webhostapp.com/send.php?send_notification=3&token="+m.getValue()+"&message="+content.getText()+"&from="+"Team Tech Vision");
                                mWebview .loadUrl("https://tfaculty.000webhostapp.com/send.php?send_notification=3&token="+m.getValue()+"&message="+content.getText()+"&from="+"Team Tech Vision");
                                //Toast.makeText(WritePost.this, (CharSequence) m.getValue(),Toast.LENGTH_SHORT).show();









                            }
                            Toast.makeText(SendNotification.this, "Update Notification Sent to all... :)",Toast.LENGTH_SHORT).show();




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        String name1=user1.getDisplayName();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Status").child(name1);
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
