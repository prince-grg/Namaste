package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.namaste.Models.Users;
import com.example.namaste.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
   storage=FirebaseStorage.getInstance();
   auth=FirebaseAuth.getInstance();
   database=FirebaseDatabase.getInstance();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
            }
        });
          binding.addProBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent();
                  intent.setAction(Intent.ACTION_GET_CONTENT);
                  intent.setType("image/*");
                  startActivityForResult(intent,33);

              }
          });
  database.getReference().child("users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
          Users users=snapshot.getValue(Users.class);
          Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_profile)
                  .into(binding.profieImg);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
  });

  binding.saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          String name=binding.etName.getText().toString();
          String status=binding.etStatus.getText().toString();
          if(name==null||status==null)
              Toast.makeText(SettingActivity.this, "Enter valid info", Toast.LENGTH_SHORT).show();
          else{
          database.getReference().child("users").child(auth.getUid()).child("username").setValue(name);
          database.getReference().child("users").child(auth.getUid()).child("status").setValue(status);
          Toast.makeText(SettingActivity.this, "Updated", Toast.LENGTH_SHORT).show();
      }}
  });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
            Uri sFile=data.getData();
            binding.profieImg.setImageURI(sFile);


            final StorageReference reference= storage.getReference().child("profile pictures")
                    .child(auth.getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SettingActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                               .child("profilepic").setValue(uri.toString());
                    }
                });



                }
            });
        }
    }

}