package com.srinivasanand.tvision;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by srinivasanand on 30/10/17.
 */
public class ChangeClubSuperuser extends AppCompatActivity{

    AutoCompleteTextView text;
    ArrayList<String> list=new ArrayList<String>();
    ImageButton imgButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_club_superuser);
        this.setTitle("Change Club Super User");




        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Members");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String ,String> map= (Map<String, String>) dataSnapshot.getValue();
                for(Map.Entry m:map.entrySet()){
                    //System.out.println(m.getKey()+" "+m.getValue());
                    list.add((String) m.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //this.setTitle("Change App SuperUser");
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,list);

        text.setAdapter(adapter);
        text.setThreshold(1);
        imgButton =(ImageButton)findViewById(R.id.search);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String val=text.getText().toString().trim();
                if(!val.equals(""))
                {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Members").child(val);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String vals= (String) dataSnapshot.getValue();
                            if(vals!=null||vals.equals("Srinivas Gooduri"))
                            {
                                DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(getIntent().getStringExtra("Club name")).child(val);
                                ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String res= (String) dataSnapshot.getValue();
                                        if(res!=null)
                                        {
                                            StringTokenizer tok=new StringTokenizer(res,";");
                                            String na=tok.nextToken();
                                            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(getIntent().getStringExtra("Club name"));
                                            ref2.setValue(val);
                                            String number=tok.nextToken();
                                            DatabaseReference ref4=FirebaseDatabase.getInstance().getReference().child("Club SuperUser Details").child(getIntent().getStringExtra("Club name"));
                                            ref4.setValue(val+";"+number);
                                            DatabaseReference ref5=FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(getIntent().getStringExtra("Club name")).child(val);
                                            ref5.setValue("Club SuperUser;"+number);
                                        }
                                        else
                                        {
                                            Toast.makeText(ChangeClubSuperuser.this,"User must be there in club before becoming club super user",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(ChangeClubSuperuser.this,"Please search something...",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
