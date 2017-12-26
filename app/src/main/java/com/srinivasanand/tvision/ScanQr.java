package com.srinivasanand.tvision;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

/**
 * Created by srinivasanand on 17/08/17.
 */

public class ScanQr extends AppCompatActivity {


    SurfaceView cameraPreview;
    BarcodeDetector barcodeDectector;
    CameraSource cameraSource;
    TextView txtResult;
    final int RequestCameraPermissionID = 1001;
    //FirebaseAuth mAuth=FirebaseAuth.getInstance();



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(ScanQr.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);
        txtResult = (TextView) findViewById(R.id.txtResult);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);

        barcodeDectector = new BarcodeDetector.Builder(ScanQr.this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(ScanQr.this, barcodeDectector)
                .setRequestedPreviewSize(600, 480)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(ScanQr.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(ScanQr.this,
                                new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID);
                        return;
                    }
                    //progressBar.dismiss();
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();

            }
        });

        barcodeDectector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                final String[] userId = new String[1];
                final String[] userEmail = new String[1];
                final int[] phoneNumber = new int[1];
                if (qrcodes.size() != 0) {
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            cameraSource.stop();
                            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            userId[0] = mFirebaseUser.getDisplayName();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Qr Validator");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                   String code= (String) dataSnapshot.getValue();
                                    if(code.equals(qrcodes.valueAt(0).displayValue))
                                    {
                                        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Students Present").child(userId[0]);
                                        ref2.setValue("true");
                                        Toast.makeText(ScanQr.this,"Uploaded Your details Succesfully....",Toast.LENGTH_LONG).show();

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    Toast.makeText(ScanQr.this,"check your internet connection.",Toast.LENGTH_LONG).show();

                                }
                            });



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
