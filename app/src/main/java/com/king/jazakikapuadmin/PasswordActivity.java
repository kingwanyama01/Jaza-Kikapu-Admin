package com.king.jazakikapuadmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PasswordActivity extends AppCompatActivity {

    private LinearLayout llayout;
    private Animation animation;
    SweetAlertDialog notify;
    String alert_message;
    EditText mEdtMail;
    Button mBtnReset;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mEdtMail = findViewById(R.id.edt_mail);
        mBtnReset = findViewById(R.id.btn_reset);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reseting\nPlease wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        llayout = findViewById(R.id.llayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        llayout.setAnimation(animation);



        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;
                email = mEdtMail.getText().toString().trim();

                if (email.isEmpty()){
                    notify = new SweetAlertDialog(PasswordActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alert_message = "Please enter email";
                    notify.setTitleText("ALERT!!!")
                            .setContentText(alert_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    notify.dismissWithAnimation();
                                }
                            })
                            .show();
                    mEdtMail.setError(alert_message);
                }else {
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(PasswordActivity.this, "Password Reset email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }else {
                                Toast.makeText(PasswordActivity.this, "Error in sending Password Reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
