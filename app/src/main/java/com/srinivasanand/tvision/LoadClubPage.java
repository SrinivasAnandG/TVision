package com.srinivasanand.tvision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


/**
 * Created by srinivasanand on 19/10/17.
 */
public class LoadClubPage extends AppCompatActivity {
    private Menu menu;
    private List<EventListValues> eventList=new ArrayList<>();
    private RecyclerView recyclerView;
    private EventHeadingAdapter mAdapter;
    String clubName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_club_page);
        final ImageView i=(ImageView)findViewById(R.id.expandedImage);

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        final String nam=user.getDisplayName();
        final String[] uri = new String[1];
        clubName=getIntent().getStringExtra("club name");
        this.setTitle(clubName);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(LoadClubPage.this,MContacts.class);
                i.putExtra("Home","false");
                i.putExtra("Club name",clubName);
                startActivity(i);
            }
        });

        if(clubName!=null)
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Clubs").child(clubName);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    uri[0] = (String) dataSnapshot.getValue();
                    //Toast.makeText(LoadClubPage.this, uri[0],Toast.LENGTH_SHORT).show();
                    Glide.with(LoadClubPage.this)
                            .load(uri[0])
                            .into(i);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }








        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    //showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    //hideOption(R.id.action_info);
                }
            }
        });





        recyclerView = (RecyclerView) findViewById(R.id.events);

        mAdapter = new EventHeadingAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(LoadClubPage.this, recyclerView, new ClickListener() {

            public void onClick(View view, int position) {
                EventListValues movie = eventList.get(position);
                Intent i=new Intent(LoadClubPage.this,LoadEvent.class);
                i.putExtra("Club Name",clubName);
                i.putExtra("Event Heading",movie.getHeading().toString());
                startActivity(i);
                //Toast.makeText(getApplicationContext(),movie.getHeading().toString(),Toast.LENGTH_SHORT).show();
            }


            public void onLongClick(View view, int position) {
                final EventListValues movie = eventList.get(position);
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(clubName);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
                        FirebaseUser user1=mAuth1.getCurrentUser();
                        String name1=user1.getDisplayName();
                        String val= (String) dataSnapshot.getValue();
                        //Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
                        if(val.equals(name1))
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadClubPage.this);
                            alertDialogBuilder.setMessage("Warning:Are you sure, You wanted to delete this session (No Backup)");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(clubName).child(movie.getHeading().toString());
                                            ref2.removeValue();
                                            DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("Registers").child(clubName).child(movie.getHeading().toString());
                                            ref3.removeValue();
                                            Toast.makeText(LoadClubPage.this,"Deleted Event Succesfully...",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoadClubPage.this,Home.class));

                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                        else
                        {
                            final String[] values = new String[1];
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadClubPage.this);
                            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(clubName).child(movie.getHeading().toString()).child("Body");
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    values[0] = (String) dataSnapshot.getValue();
                                    alertDialogBuilder.setMessage(values[0] +"Click Yes if you want to register...");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    //Toast.makeText(LoadClubPage.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Registers").child(clubName).child(movie.getHeading().toString()).child(nam);
                                                    ref.setValue("true");
                                                    Toast.makeText(getApplicationContext(),"Registered Succesfully..",Toast.LENGTH_SHORT).show();


                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

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
        }));
        final EventListValues[] movie = {new EventListValues("srininvas")};
        //eventList.add(movie[0]);
        //movie[0] = new EventListValues("somu");
        //eventList.add(movie[0]);
        if(clubName!=null)
        {
            DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("Club Event details").child(clubName);
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String ,Map> map= (Map<String, Map>) dataSnapshot.getValue();
                    if(map!=null)
                    {
                        for(Map.Entry m:map.entrySet()){
                            System.out.println(m.getKey()+" "+m.getValue());
                            movie[0] = new EventListValues((String) m.getKey());
                            eventList.add(movie[0]);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Sorry no events found",Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        //hideOption(R.id.action_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changeClubSuperUser&&clubName!=null) {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(clubName);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    String name=user.getDisplayName();
                    String Value= (String) dataSnapshot.getValue();
                    if(Value.equals(name)||name.equals("Srinivas Gooduri"))
                    {
                        Intent i=new Intent(LoadClubPage.this,ChangeClubSuperuser.class);
                        i.putExtra("Club name",clubName);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),Value+" "+"Is club Super User",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (id == R.id.addEvent&&clubName!=null) {

            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Individual Club Member Validation").child(clubName);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    String name=user.getDisplayName();
                    Map<String,String> map= (Map<String, String>) dataSnapshot.getValue();
                    if(map!=null)
                    {
                        for(Map.Entry m:map.entrySet()){
                            //System.out.println(m.getKey()+" "+m.getValue());
                            String Value= (String) m.getKey();
                            if(Value.equals(name))
                            {
                                Intent i=new Intent(LoadClubPage.this,WritePost.class);
                                i.putExtra("Club name",clubName);
                                startActivity(i);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        else if (id == R.id.addTeamMember&&clubName!=null) {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(clubName);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    String name=user.getDisplayName();
                    String Value= (String) dataSnapshot.getValue();
                    if(Value.equals(name))
                    {
                        Intent i=new Intent(LoadClubPage.this,AddTeamMember.class);
                        i.putExtra("Club name",clubName);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),Value+" "+"Is club Super User",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (id == R.id.deleteTeamMember&&clubName!=null) {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(clubName);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    String name=user.getDisplayName();
                    String Value= (String) dataSnapshot.getValue();
                    if(Value.equals(name))
                    {
                        Intent i=new Intent(LoadClubPage.this,DeleteTeamMember.class);
                        i.putExtra("Club name",clubName);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),Value+" "+"Is club Super User",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }



}

