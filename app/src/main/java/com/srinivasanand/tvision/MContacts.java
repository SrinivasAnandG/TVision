package com.srinivasanand.tvision;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.srinivasanand.tvision.R.id.position;

/**
 * Created by srinivasanand on 22/09/17.
 */
public class MContacts extends AppCompatActivity {


    private List<ContactsListValues> ContactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcontacts);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ContactsAdapter(ContactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MContacts.this, recyclerView, new ClickListener() {

            public void onClick(View view, int position) {
                ContactsListValues movie = ContactList.get(position);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+movie.getNumber()));
                startActivity(intent);
            }


            public void onLongClick(View view, int position) {
                ContactsListValues movie = ContactList.get(position);
                String name=movie.getName();
                StringTokenizer tok=new StringTokenizer(name,"(");
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                String nam=user.getDisplayName();
                String names=tok.nextToken();
                if(!nam.equals(names))
                {
                    UserDetails.username=nam;
                    UserDetails.chatWith = names;
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    startActivity(new Intent(MContacts.this, Chat.class));
                }
                else {
                    Toast.makeText(MContacts.this,"You cant text you...  xp",Toast.LENGTH_SHORT).show();
                }

            }
        }));

        prepareMovieData();


    }



    private void prepareMovieData() {
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        final ContactsListValues[] movie = {new ContactsListValues("Srinivas Gooduri(App Developer)", "9030304098")};
        ContactList.add(movie[0]);
        String validate=getIntent().getStringExtra("Home");
        if(validate.equals("true"))
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Club SuperUser Details");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String ,String> numbers= (Map<String, String>) dataSnapshot.getValue();
                    for(Map.Entry m:numbers.entrySet()){
                        //System.out.println(m.getKey()+" "+m.getValue());
                        String pnum= (String) m.getValue();
                        StringTokenizer pnu= new StringTokenizer(pnum,";");
                        String originalName=pnu.nextToken();
                        String number=pnu.nextToken();
                        String name= (String) m.getKey()+"("+position+")";
                        movie[0] = new ContactsListValues(originalName+"("+m.getKey()+")", number);
                        ContactList.add(movie[0]);
                    }
                    mAdapter.notifyDataSetChanged();



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(getIntent().getStringExtra("Club name"));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String ,String> numbers= (Map<String, String>) dataSnapshot.getValue();
                    if(numbers!=null)
                    {
                        for(Map.Entry m:numbers.entrySet()){
                            //System.out.println(m.getKey()+" "+m.getValue());
                            String pnum= (String) m.getValue();
                            StringTokenizer pnu= new StringTokenizer(pnum,";");
                            String position=pnu.nextToken();
                            String number=pnu.nextToken();
                            String name= (String) m.getKey()+"("+position+")";
                            movie[0] = new ContactsListValues(name, number);
                            ContactList.add(movie[0]);
                        }
                        mAdapter.notifyDataSetChanged();
                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }




        //mAdapter.notifyDataSetChanged();
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
