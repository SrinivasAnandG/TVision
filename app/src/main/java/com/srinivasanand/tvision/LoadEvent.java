package com.srinivasanand.tvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by srinivasanand on 29/10/17.
 */

public class LoadEvent extends AppCompatActivity {

    TextView email,time,head,body;
    Button linking,Register,UnRegister,who;
    String Name,LINK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_event);
        //Toast.makeText(LoadEvent.this,getIntent().getStringExtra("Club Name"),Toast.LENGTH_SHORT).show();
        //Toast.makeText(LoadEvent.this,getIntent().getStringExtra("Event Heading"),Toast.LENGTH_SHORT).show();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        final String nam=user.getDisplayName();
        this.setTitle(getIntent().getStringExtra("Club Name").toString());
        time=(TextView)findViewById(R.id.time);
        head=(TextView)findViewById(R.id.head);
        body=(TextView)findViewById(R.id.body);
        linking=(Button)findViewById(R.id.linking);
        Register=(Button)findViewById(R.id.Register);
        UnRegister=(Button)findViewById(R.id.UnRegister);
        who=(Button)findViewById(R.id.WhoRegister);
        who.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoadEvent.this,WhoRegistered.class);
                i.putExtra("Club Name",getIntent().getStringExtra("Club Name"));
                i.putExtra("Event Heading",getIntent().getStringExtra("Event Heading"));
                startActivity(i);
            }
        });
        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Registers").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child(nam);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String val= (String) dataSnapshot.getValue();
                if(val!=null)
                {
                    UnRegister.setEnabled(true);
                    //UnRegister.setVisibility(View.VISIBLE);
                }
                else
                {
                    Register.setEnabled(true);
                    //Register.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Registers").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child(nam);
                ref.setValue("true");
                Register.setEnabled(false);
                //Register.setVisibility(View.INVISIBLE);
                UnRegister.setEnabled(true);
                //UnRegister.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Registered Succesfully..",Toast.LENGTH_SHORT).show();
            }
        });
        UnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Registers").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child(nam);
                ref.removeValue();
                UnRegister.setEnabled(false);
                //UnRegister.setVisibility(View.INVISIBLE);
                Register.setEnabled(true);
                //Register.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"UnRegistered Succesfully..",Toast.LENGTH_SHORT).show();
            }
        });
        linking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UrlLoad.class);
                intent.putExtra("id",LINK );
                startActivity(intent);
            }
        });



        DatabaseReference ref20= FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child("Body");
        ref20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val= (String) dataSnapshot.getValue();
                //body.setText(val);
                body.setText(val);
                body.setTextColor(R.color.black);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref21=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child("Heading");
        ref21.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val= (String) dataSnapshot.getValue();
                //head.setText(val);
                head.setText(val);
                head.setTextColor(R.color.black);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref22=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child("Links");
        ref22.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val= (String) dataSnapshot.getValue();
                //links.setText(val);
                linking.setText(val);
                linking.setEnabled(true);
                linking.setTextColor(R.color.black);
                LINK=val;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference ref23=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(getIntent().getStringExtra("Club Name")).child(getIntent().getStringExtra("Event Heading")).child("Last Updated time");
        ref23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val= (String) dataSnapshot.getValue();
                time.setText(val);
                time.setTextColor((int)R.color.black);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
