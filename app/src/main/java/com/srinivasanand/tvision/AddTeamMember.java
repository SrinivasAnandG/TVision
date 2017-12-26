package com.srinivasanand.tvision;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
 * Created by srinivasanand on 05/10/17.
 */

public class AddTeamMember extends AppCompatActivity {
    EditText email,position,number;
    Button add;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_team_member);
        email=(EditText)findViewById(R.id.email1);
        position=(EditText)findViewById(R.id.position);
        number=(EditText)findViewById(R.id.number);
        add=(Button)findViewById(R.id.addHim);
        final int[] i = {0};
        //Toast.makeText(getApplicationContext(),getIntent().getStringExtra("Club name"),Toast.LENGTH_LONG).show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1=email.getText().toString().trim();
                final String position1=position.getText().toString().trim();
                final String number1=number.getText().toString().trim();
                if(!email1.equals("")&&!position1.equals("")&&!number1.equals(""))
                {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Members");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String val2=position1+";"+number1;

                            Map<String ,String> val= (Map<String, String>) dataSnapshot.getValue();
                            for(Map.Entry m:val.entrySet()){
                                System.out.println(m.getKey()+" "+m.getValue());
                                if(email1.equals(m.getValue()))
                                {

                                    String name= (String) m.getKey();
                                    DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Individual Club Member Validation").child(getIntent().getStringExtra("Club name")).child(name);
                                    ref2.setValue("true");
                                    DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(getIntent().getStringExtra("Club name")).child(name);
                                    ref3.setValue(val2);
                                    Toast.makeText(AddTeamMember.this,"Member Added.  ;)",Toast.LENGTH_SHORT).show();
                                    i[0] =1;
                                    break;
                                }

                            }
                            if(i[0]==0)
                            {
                                Toast.makeText(AddTeamMember.this,"Email not Found in Database... charaters are case sensitive",Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(AddTeamMember.this,"Missing Data..",Toast.LENGTH_SHORT).show();
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
