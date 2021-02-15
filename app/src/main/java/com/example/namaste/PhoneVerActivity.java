package com.example.namaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.namaste.databinding.ActivityPhoneVerBinding;

public class PhoneVerActivity extends AppCompatActivity {
   ActivityPhoneVerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneVerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.editTextPhone.requestFocus();
        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=binding.editTextPhone.getText().toString();
                if(number==null||number.length()!=10)
                {Toast.makeText(PhoneVerActivity.this, "Please Enter a Valid number", Toast.LENGTH_SHORT).show();}
                else {
                    Intent intent=new Intent(PhoneVerActivity.this,OtpActivity.class);
                    intent.putExtra("number",number);
                    startActivity(intent);

                }
            }
        });


    }
}