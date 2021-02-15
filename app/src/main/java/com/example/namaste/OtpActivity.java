package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.namaste.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
     ActivityOtpBinding binding;
     FirebaseAuth auth;
     ProgressDialog dialog;
     String verificationIdu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         auth=FirebaseAuth.getInstance();
         dialog=new ProgressDialog(this);
         dialog.setMessage("Sending otp....");
         dialog.setTitle("Please wait ");
         dialog.setCancelable(false);
         dialog.show();
        String phonenumber=getIntent().getStringExtra("number");
        String nn="+91"+phonenumber;
        Toast.makeText(this, ""+nn, Toast.LENGTH_SHORT).show();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(nn)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Save the verification id somewhere
                        // ...
                        verificationIdu=verificationId;
                        dialog.dismiss();
                       // Toast.makeText(OtpActivity.this, "I am in Codesent ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                      //  Toast.makeText(OtpActivity.this, "I am in Verify Complete", Toast.LENGTH_SHORT).show();
                        // ...
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Toast.makeText(OtpActivity.this, "I am in Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);





      binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(OtpActivity.this, "clicked", Toast.LENGTH_SHORT).show();
              String entered=binding.otpView.getText().toString();

              if(entered==null||entered.length()!=6)
              {
                  Toast.makeText(OtpActivity.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
              }
              else {

                  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIdu, entered);
                  signInWithPhoneAuthCredential(credential);
              }
          }
      });



    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

            startActivity(new Intent(OtpActivity.this,ProfileActivity.class));

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}