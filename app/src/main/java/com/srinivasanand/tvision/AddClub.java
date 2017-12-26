package com.srinivasanand.tvision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by srinivasanand on 30/10/17.
 */

public class AddClub extends AppCompatActivity {

    EditText ClubsuperUserName,phone,clubName;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://tvision-f0787.appspot.com/");    //change the url according to your firebase app


    Button pic,upload;
    ImageView image;
    public static String username,phonenumber,clubname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_club);
        ClubsuperUserName=(EditText)findViewById(R.id.username);
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        phone=(EditText)findViewById(R.id.phoneNumber);
        pic=(Button)findViewById(R.id.Photo);
        upload=(Button)findViewById(R.id.Upload);
        clubName=(EditText)findViewById(R.id.ClubName);
        image=(ImageView)findViewById(R.id.image);
        upload.setEnabled(false);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubname=clubName.getText().toString().trim();
                username=ClubsuperUserName.getText().toString().trim();
                phonenumber=phone.getText().toString().trim();
                if(!clubname.equals("")&&!username.equals("")&&!phonenumber.equals(""))
                {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Members").child(username);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String email= (String) dataSnapshot.getValue();
                            if(email!=null&&phonenumber.length()==10)
                            {
                                upload.setEnabled(true);
                                pic.setEnabled(false);
                                Toast.makeText(AddClub.this,"Values captured cannot be edited... if you want to edit cancel upload",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                            }
                            else
                            {
                                Toast.makeText(AddClub.this,"Check Phone number and username...",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(AddClub.this,"missing data",Toast.LENGTH_LONG).show();
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {
                    pd.show();

                    StorageReference childRef = storageRef.child("image.jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(AddClub.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            Uri uri=taskSnapshot.getDownloadUrl();
                            if(!uri.toString().equals(""))

                            {
                                DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("Club SuperUsers").child(clubname);
                                ref2.setValue(username);
                                DatabaseReference ref3= FirebaseDatabase.getInstance().getReference().child("Club SuperUser Details").child(clubname);
                                ref3.setValue(username+";"+phonenumber);
                                DatabaseReference ref4= FirebaseDatabase.getInstance().getReference().child("Individual Club Member Validation").child(clubname);
                                ref4.child(username).setValue("true");
                                DatabaseReference ref5= FirebaseDatabase.getInstance().getReference().child("Individual Club Members Details").child(clubname);
                                ref5.child(username).setValue("Club Super User;"+phonenumber);
                                DatabaseReference ref6= FirebaseDatabase.getInstance().getReference().child("Clubs").child(clubname);
                                ref6.setValue(uri.toString());
                            }
                            else
                            {
                                Toast.makeText(AddClub.this, "something went wrong, we are working on it...", Toast.LENGTH_SHORT).show();
                            }





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddClub.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(AddClub.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
