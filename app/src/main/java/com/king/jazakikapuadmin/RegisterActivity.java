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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout llayout;
    private Animation animation;
    SweetAlertDialog notify;
    String alert_message;
    EditText mEdtMail,mEdtPass,mEdtPassConfirm;
    Button tvSignUp;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEdtMail = findViewById(R.id.edt_mail);
        mEdtPass = findViewById(R.id.edt_password);
        mEdtPassConfirm = findViewById(R.id.edt_password_confirm);
        tvSignUp = findViewById(R.id.btn_signup);

        llayout = findViewById(R.id.llayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        llayout.setAnimation(animation);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Registering\nPlease Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,password_confirm;
                email = mEdtMail.getText().toString().trim();
                password = mEdtPass.getText().toString().trim();
                password_confirm = mEdtPassConfirm.getText().toString().trim();
                if (email.isEmpty()){
                    notify = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
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

                }else if (password.isEmpty()){
                    notify = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alert_message = "Please enter password";
                    notify.setTitleText("ALERT!!!")
                            .setContentText(alert_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    notify.dismissWithAnimation();
                                }
                            })
                            .show();
                    mEdtPass.setError(alert_message);

                }else if (password_confirm.isEmpty()){
                    notify = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alert_message = "Please enter confirm password";
                    notify.setTitleText("ALERT!!!")
                            .setContentText(alert_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    notify.dismissWithAnimation();
                                }
                            })
                            .show();
                    mEdtPassConfirm.setError(alert_message);

                }else if (password.length()<6){
                    notify = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alert_message = "Password must be 6 characters or more";
                    notify.setTitleText("ALERT!!!")
                            .setContentText(alert_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    notify.dismissWithAnimation();
                                }
                            })
                            .show();
                    mEdtPass.setError(alert_message);

                }else if (!password.equals(password_confirm)){
                    notify = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alert_message = "Passwords don't match";
                    notify.setTitleText("ALERT!!!")
                            .setContentText(alert_message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    notify.dismissWithAnimation();
                                }
                            })
                            .show();
                    mEdtPass.setError(alert_message);
                    mEdtPassConfirm.setError(alert_message);
                }else {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()){
                                       startActivity(new Intent(getApplicationContext(), ProductAdd.class));
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
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
