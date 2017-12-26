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

public class DeleteTeamMember extends AppCompatActivity {
    EditText email;
    Button delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_team_member);
        email=(EditText)findViewById(R.id.email);
        delete=(Button)findViewById(R.id.deleteHim);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=email.getText().toString().trim();
                if(!Email.equals(""))
                {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Members");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            final Map<String ,String> val= (Map<String, String>) dataSnapshot.getValue();
                            for(Map.Entry m:val.entrySet()){
                                System.out.println(m.getKey()+" "+m.getValue());
                                if(Email.equals(m.getValue()))
                                {

                                    final String name= (String) m.getKey();
                                    DatabaseReference ref4=FirebaseDatabase.getInstance().getReference().child("Individual Club Member Validation").child(getIntent().getStringExtra("Club name"));
                                    ref4.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Map<String ,String> val2= (Map<String, String>) dataSnapshot.getValue();
                                            if(val2!=null)
                                            {
                                                for(Map.Entry m:val2.entrySet()) {
                                                    System.out.println(m.getKey() + " " + m.getValue());
                                                    if(name.equals(m.getKey()))
                                                    {
                                                        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Individual Club Member Validation").child(getIntent().getStringExtra("Club name")).child(name);
                                                        ref2.removeValue();
                                                        DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(getIntent().getStringExtra("Club name")).child(name);
                                                        ref3.removeValue();
                                                        Toast.makeText(DeleteTeamMember.this,"Member Deleted.  ;)", Toast.LENGTH_SHORT).show();

                                                        break;
                                                    }
                                                }
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }



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
