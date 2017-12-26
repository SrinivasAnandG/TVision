package com.srinivasanand.tvision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView image;
    TextView name,email;

    String Name,LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        String name1=user1.getDisplayName();
        DatabaseReference ref10=FirebaseDatabase.getInstance().getReference().child("User Token").child(name1);
        ref10.setValue(FirebaseInstanceId.getInstance().getToken());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("App Club");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this,MContacts.class);
                i.putExtra("Home","true");
                i.putExtra("Club name","false");
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final String[] v=new String[4];

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        name=(TextView)header.findViewById(R.id.UserName);
        email=(TextView)header.findViewById(R.id.email);
        name.setText(user.getDisplayName().toString());
        email.setText(user.getEmail());
        Name=user.getDisplayName();
        Uri pic=user.getPhotoUrl();
       // Toast.makeText(Home.this, (CharSequence) pic,Toast.LENGTH_SHORT).show();
        image=(ImageView)header.findViewById(R.id.profilePic);
        image.setImageURI(pic);
        Picasso.with(getApplicationContext())
                .load(pic)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(image);
        String uri=pic.toString();
        DatabaseReference ref40=FirebaseDatabase.getInstance().getReference().child("Profile Pic Uri").child(Name);
        ref40.setValue(uri);






        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new CardFragment();
            ;
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();


        }







        final String user2 = user.getDisplayName();
        final String pass2 = user.getEmail();

        if(user2.equals("")){
            //username.setError("can't be blank");
        }
        else {
            final ProgressDialog pd = new ProgressDialog(Home.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://tvision-f0787.firebaseio.com/users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase.setAndroidContext(Home.this);
                    Firebase reference = new Firebase("https://tvision-f0787.firebaseio.com/users");

                    if(s.equals("null")) {
                        reference.child(user2).child("password").setValue(pass2);
                        //Toast.makeText(Home.this, "registration successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(user2)) {
                                reference.child(user2).child("password").setValue(pass2);
                                //Toast.makeText(Home.this, "registration successful", Toast.LENGTH_LONG).show();
                            } else {
                               // Toast.makeText(Home.this, "username already exists", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(Home.this);
            rQueue.add(request);
        }
    }








    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            final String name=user.getDisplayName();
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUser");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String names= (String) dataSnapshot.getValue();
                    if(name.equals(names)||name.equals("Srinivas Gooduri"))
                    {
                        startActivity(new Intent(Home.this,ChangeSuperUser.class));
                    }
                    else
                    {
                        Toast.makeText(Home.this,"Sorry, You are not Super User to Access this Feature.. :(",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(id==R.id.send)
        {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            final String name=user.getDisplayName();
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUsers");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String,String> map= (Map<String, String>) dataSnapshot.getValue();
                    for(Map.Entry m:map.entrySet()){
                        if(name.equals(m.getValue().toString().trim())||name.equals("Srinivas Gooduri"))
                        {
                            startActivity(new Intent(Home.this,SendNotification.class));
                        }

                    }
                    Toast.makeText(Home.this,"Sorry, You are not any club Super User to Access this Feature.. :(",Toast.LENGTH_SHORT).show();



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));
            // Handle the camera action
        }  else if (id == R.id.nav_tools) {
            startActivity(new Intent(this,ScanQr.class));


        }


        else if (id == R.id.nav_college_web_site) {
            Intent intent = new Intent(getBaseContext(), UrlLoad.class);
            intent.putExtra("id","https://www.sreenidhi.edu.in/" );
            startActivity(intent);


        }

        else if (id == R.id.AddClubs) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            final String name=user.getDisplayName();
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Club SuperUser");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String superUser= (String) dataSnapshot.getValue();
                    if(superUser.equals(name)||name.equals("Srinivas Gooduri"))
                    {
                        Intent intent = new Intent(getBaseContext(), AddClub.class);

                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Only App SuperUser can add clubs",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
        else if (id == R.id.nav_community_web_site) {

            Intent intent = new Intent(getBaseContext(), UrlLoad.class);
            intent.putExtra("id","https://snistcommunity.000webhostapp.com/index.php" );
            startActivity(intent);


        }
        else if (id == R.id.profile) {

            Intent intent = new Intent(getBaseContext(), Profiles.class);
            intent.putExtra("id",Name);
            startActivity(intent);


        }
        else if (id == R.id.Searchprofile) {

            Intent intent = new Intent(getBaseContext(), SearchProfiles.class);
            startActivity(intent);


        }

        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://www.facebook.com/techvisionclub/";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }
        else if(id == R.id.Chating)
        {

            //startActivity(new Intent(this, ChatRoomActivity.class));


            final FirebaseAuth mAuth1=FirebaseAuth.getInstance();
            FirebaseUser user1=mAuth1.getCurrentUser();
           // String name1=user1.getDisplayName();
            final String user = user1.getDisplayName();
            final String pass = user1.getEmail();

            if(user.equals("")){
                //username.setError("can't be blank");
            }
            else{
                String url = "https://tvision-f0787.firebaseio.com/users.json";
                final ProgressDialog pd = new ProgressDialog(Home.this);
                pd.setMessage("Loading...");
                pd.show();

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("null")){
                            Toast.makeText(Home.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                JSONObject obj = new JSONObject(s);

                                if(!obj.has(user)){
                                    Toast.makeText(Home.this, "user not found", Toast.LENGTH_LONG).show();
                                }
                                else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                    UserDetails.username = user;
                                    UserDetails.password = pass;
                                    startActivity(new Intent(Home.this, Users.class));
                                    //startActivity(new Intent(Home.this, Chat.class));
                                }
                                else {
                                    Toast.makeText(Home.this, "incorrect password", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        pd.dismiss();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(Home.this);
                rQueue.add(request);
            }

        }






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        if(user1!=null)
        {
            String name1=user1.getDisplayName();
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Status").child(name1);
            ref.setValue("true");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth mAuth1=FirebaseAuth.getInstance();
        FirebaseUser user1=mAuth1.getCurrentUser();
        if(user1!=null)
        {
            String name1=user1.getDisplayName();
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Status").child(name1);
            ref.setValue("false");
        }

    }
}
