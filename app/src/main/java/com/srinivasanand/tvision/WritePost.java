package com.srinivasanand.tvision;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by srinivasanand on 05/10/17.
 */

public class WritePost extends AppCompatActivity {
    EditText head,body,links;
    Button old,newOne;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);
        head=(EditText)findViewById(R.id.sessionon1);
        body=(EditText)findViewById(R.id.sessiondescription1);
        links=(EditText)findViewById(R.id.links1);
        old=(Button)findViewById(R.id.edit);
        newOne=(Button)findViewById(R.id.post);
        old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                DatabaseReference ref20=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club name")).child(String.valueOf(head.getText())).child("Body");
                ref20.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val= (String) dataSnapshot.getValue();
                        body.setText(val);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                DatabaseReference ref21=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club name")).child(String.valueOf(head.getText())).child("Head");
                ref21.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val= (String) dataSnapshot.getValue();
                        head.setText(val);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                DatabaseReference ref22=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club name")).child(String.valueOf(head.getText())).child("Links");
                ref22.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val= (String) dataSnapshot.getValue();
                        links.setText(val);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        newOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(WritePost.this,"sent1",Toast.LENGTH_SHORT).show();

                final String head1=head.getText().toString().trim();
                //Toast.makeText(WritePost.this,"sent2",Toast.LENGTH_SHORT).show();
                String body1=body.getText().toString().trim();
                //Toast.makeText(WritePost.this,"sent3",Toast.LENGTH_SHORT).show();
                String links1=links.getText().toString().trim();
                //Toast.makeText(WritePost.this,"sent4",Toast.LENGTH_SHORT).show();
                if(!head1.equals("")&&!body1.equals("")&&!links1.equals(""))
                {
                    /*final WebView mWebview=new WebView(WritePost.this);
                    mWebview.getSettings().setJavaScriptEnabled(true);
                    mWebview.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        }
                    });*/
                    DatabaseReference ref15=FirebaseDatabase.getInstance().getReference().child("User Token");
                    ref15.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String ,String> vals=(Map<String, String>) dataSnapshot.getValue();
                            for(Map.Entry m:vals.entrySet()){
                                final WebView mWebview=new WebView(WritePost.this);
                                mWebview.getSettings().setJavaScriptEnabled(true);
                                mWebview.setWebViewClient(new WebViewClient() {
                                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    }
                                });
                                //System.out.println(m.getKey()+" "+m.getValue());
                                mWebview .loadUrl("https://tfaculty.000webhostapp.com/send.php?send_notification=3&token="+m.getValue()+"&message="+head1+"&from="+getIntent().getStringExtra("Club name"));
                                mWebview .loadUrl("https://tfaculty.000webhostapp.com/send.php?send_notification=3&token="+m.getValue()+"&message="+head1+"&from="+getIntent().getStringExtra("Club name"));
                                //Toast.makeText(WritePost.this, (CharSequence) m.getValue(),Toast.LENGTH_SHORT).show();









                            }
                            Toast.makeText(WritePost.this, "Update Notification Sent to all... :)",Toast.LENGTH_SHORT).show();




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Date dNow = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                    String time=(String)ft.format(dNow);
                    DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club name")).child(head1);
                    ref1.child("Body").setValue(body1);
                    ref1.child("Heading").setValue(head1);
                    ref1.child("Links").setValue(links1);
                    ref1.child("Last Updated time").setValue(time);
                    startActivity(new Intent(WritePost.this,Home.class));
                    Toast.makeText(WritePost.this,"Updated Succesfully...",Toast.LENGTH_SHORT).show();



                }
                else
                {
                    Toast.makeText(WritePost.this,"Missing Data",Toast.LENGTH_SHORT).show();

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
