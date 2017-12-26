package com.srinivasanand.tvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by srinivasanand on 18/10/17.
 */
public class Profiles  extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        final String name = getIntent().getStringExtra("id");
        final ImageView image=(ImageView)findViewById(R.id.header_cover_image);
        final TextView names=(TextView)findViewById(R.id.name);
        final TextView emails=(TextView)findViewById(R.id.email);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Members").child(name);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //email10 = (String) dataSnapshot.getValue();
                //Toast.makeText(Profiles.this,(String) dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                emails.setText((String) dataSnapshot.getValue());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("Profile Pic Uri").child(name);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //uri10 = (String) dataSnapshot.getValue();
                Picasso.with(getApplicationContext())
                        .load((String) dataSnapshot.getValue())
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                String nam=user.getDisplayName();
                if(!nam.equals(name))
                {
                    UserDetails.username=nam;
                    UserDetails.chatWith = name;
                    startActivity(new Intent(Profiles.this, Chat.class));
                }
                else {
                    Toast.makeText(Profiles.this,"You cant text you...  xp",Toast.LENGTH_SHORT).show();
                }

                //startActivity(new Intent(Home.this,MContacts.class));
            }
        });
        names.setText(name);
        //emails.setText(email10);
        //Toast.makeText(this,email10,Toast.LENGTH_SHORT).show();

        DatabaseReference ref30=FirebaseDatabase.getInstance().getReference().child("Status").child(name);
        ref30.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val= (String) dataSnapshot.getValue();
                if(val.equals("true"))
                {
                    names.setText(name+"("+"ONLINE"+")");
                }
                else
                {
                    names.setText(name+"("+"OFFLINE"+")");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
