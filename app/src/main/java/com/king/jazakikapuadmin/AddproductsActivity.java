package com.king.jazakikapuadmin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddproductsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mBtnSave;
    private EditText mEdtAmount;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private FirebaseAuth firebaseAuth;
    String mDepositType,mAmount,mDownloadUrl,alert_message,mFirebaseUserEmail,mDateTime;
    Spinner mSpinnerDepositType;
    SweetAlertDialog pDialog;
    DateFormat dateFormat;
    Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUserEmail = firebaseAuth.getCurrentUser().getEmail();
        dateFormat  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        mDate = new Date();
        mDateTime = dateFormat.format(mDate);

        mSpinnerDepositType = findViewById(R.id.spinner_deposit_options);
        mEdtAmount = findViewById(R.id.edt_amount);
        mSpinnerDepositType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDepositType = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDepositType = "Select type";
            }
        });

        mBtnSave = findViewById(R.id.btn_save);
        mEdtAmount = findViewById(R.id.edt_amount);
        mImageView = findViewById(R.id.imageView);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask !=null && mUploadTask.isInProgress()){
                    alert_message = "Upload in Progress";
                    alert_message(alert_message);
                }else {
                    uploadFile();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }else if (id==R.id.action_deposits){
            startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
        }else if (id==R.id.action_copy_account){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", "1280082097");
            clipboard.setPrimaryClip(clip);
            alert_message = "Copied 1280082097 to clipboard";
            alert_message(alert_message);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST &&resultCode==
                RESULT_OK && data!=null && data.getData()!=null){
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        mAmount = mEdtAmount.getText().toString().trim();
        if (mDepositType.equals("Select type")){
            alert_message = "Please Select type";
            alert_message(alert_message);
        }else if (mAmount.isEmpty()){
            alert_message = "Please enter amount "+mAmount;
            alert_message(alert_message);
            mEdtAmount.setError("Please enter amount");
        }else {
            if (mImageUri !=null){
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        +"."+getFileExtension(mImageUri));

                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper();
                pDialog.setTitleText("Saving");
                pDialog.setContentText("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                mUploadTask =fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    //Success
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Thread delay = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        delay.start();
                        pDialog.dismissWithAnimation();
                        alert_message = "Saving successful";
                        alert_message(alert_message);
                        mEdtAmount.setText("");
                        Glide.with(AddproductsActivity.this).load(R.mipmap.defaultimg).into(mImageView);
                        mDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Upload upload = new Upload(mDepositType,mAmount,mDownloadUrl,mFirebaseUserEmail,mDateTime,""+System.currentTimeMillis());
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(System.currentTimeMillis()+"").setValue(upload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    //Failure
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddproductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    //Updating the Progress Bar
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });

            }else {
                alert_message = "No file selected";
                alert_message(alert_message);
            }
        }
    }

    private void openImagesActivity(){
        Intent intent = new Intent(getApplicationContext(), AddproductsActivity.class);
        startActivity(intent);
    }

    public void alert_message(String message){
        final SweetAlertDialog notify;
        notify = new SweetAlertDialog(AddproductsActivity.this, SweetAlertDialog.WARNING_TYPE);
        notify.setTitleText("ALERT!!!")
                .setContentText(alert_message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        notify.dismissWithAnimation();
                    }
                })
                .show();

    }

}