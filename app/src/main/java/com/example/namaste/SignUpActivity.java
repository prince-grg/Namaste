package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.namaste.Models.Users;
import com.example.namaste.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding  binding;
    FirebaseDatabase database;
    private FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase mdatabase;
    ProgressDialog progressDialog,pg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg1.show();
                signIn();
            }
        });
          binding.txtSignUpphone.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(SignUpActivity.this,PhoneVerActivity.class));
              }
          });
        mdatabase=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(SignUpActivity.this);
        pg1=new ProgressDialog(SignUpActivity.this);
        pg1.setTitle("Siging in");
        pg1.setMessage("Creating");
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating acc");
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        binding.txtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Users user=new Users(binding.txtName.getText().toString(),binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                            String nu=task.getResult().getUser().getUid();
                            user.setUserId(nu);
                            database.getReference().child("users").child(nu).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Sign Up Sucessfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
    int RC_SIGN_IN=65;
    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pg1.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          // Log.d("TAG"", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                               Users users=new Users();
                               users.setUserId(user.getUid());
                               users.setMail(user.getEmail());
                               users.setProfilepic(user.getPhotoUrl().toString());
                               users.setUsername(user.getDisplayName());

                               mdatabase.getReference().child("users").child(user.getUid()).setValue(users);
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            Toast.makeText(SignUpActivity.this, "Successfully sign up", Toast.LENGTH_SHORT).show();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           Log.w("TAG", "signInWithCredential", task.getException());
                           //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

}