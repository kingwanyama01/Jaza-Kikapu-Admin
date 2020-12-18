package com.king.jazakikapuadmin;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private TextView tvRegister,mTvForgotPassword;
    EditText mEdtMail,mEdtPass;
    Button mBtnLogin,btRegister;
    String alert_message;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister  = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tvRegister);
//        btRegister.setOnClickListener(this);
        mEdtMail = findViewById(R.id.edt_mail);
        mEdtPass = findViewById(R.id.edt_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mTvForgotPassword = findViewById(R.id.tvForgot);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait...");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            finish();
            Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
            startActivity(intent);
        }

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,password_confirm;
                email = mEdtMail.getText().toString().trim();
                password = mEdtPass.getText().toString().trim();
                if (email.isEmpty()){
                    alert_message = "Please enter email";
                    alert_message(alert_message);
                    mEdtMail.setError(alert_message);

                }else if (password.isEmpty()){
                    alert_message = "Please enter password";
                    alert_message(alert_message);
                    mEdtPass.setError(alert_message);

                }else if (password.length()<6){
                    alert_message = "Password must be 6 characters or more";
                    alert_message(alert_message);
                    mEdtPass.setError(alert_message);
                }else {
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){

                                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                                finish();

                            }else {

                                alert_message = "Login Failed. Incorrect email or password";
                                alert_message(alert_message);
                            }
                        }
                    });
                }
            }
        });
        mTvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (v==mTvForgotPassword){
                    Intent intent   = new Intent(MainActivity.this,PasswordActivity.class);
                    Pair[] pairs    = new Pair[1];
                    pairs[0] = new Pair<View,String>(tvRegister,"tvRegister");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (v==tvRegister){
                    Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
                    Pair[] pairs    = new Pair[1];
                    pairs[0] = new Pair<View,String>(tvRegister,"tvRegister");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                }
            }
        });



    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onClick(View v) {
//        if (v==btRegister){
//            Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
//            Pair[] pairs    = new Pair[1];
//            pairs[0] = new Pair<View,String>(tvRegister,"tvRegister");
//            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
//            startActivity(intent,activityOptions.toBundle());
//        }
//    }

    public void alert_message(String message){
        final SweetAlertDialog notify;
        notify = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
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
