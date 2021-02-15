package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.namaste.Adapters.FragmentsAdapter;
import com.example.namaste.databinding.ActivityMainBinding;
import com.example.namaste.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;
   FirebaseAuth auth;
   String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
   auth=FirebaseAuth.getInstance();

   binding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
   binding.tablayout.setupWithViewPager(binding.viewpager);
  updateToken(FirebaseInstanceId.getInstance().getToken());



    }



    public void updateToken(String token)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken=new Token(token);
        ref.child(FirebaseAuth.getInstance().getUid()).setValue(mToken);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.log_out:
              auth.signOut();
              startActivity(new Intent(MainActivity.this,SignInActivity.class));
              finish();
              break;

            case R.id.Group_Chat:
                startActivity(new Intent(MainActivity.this,GroupChatActivity.class));
                break;

        }
        return true;

    }
}