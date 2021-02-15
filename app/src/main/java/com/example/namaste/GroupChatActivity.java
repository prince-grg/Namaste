package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.namaste.Adapters.ChatAdapter;
import com.example.namaste.Models.MessagesModel;
import com.example.namaste.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
            }
        });
        getSupportActionBar().hide();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.username.setText("Kasukabe Defence Group");

        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(messagesModels, this);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    MessagesModel model=snapshot1.getValue(MessagesModel.class);
                    messagesModels.add(model);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message=binding.msgTxt.getText().toString();
                final MessagesModel model=new MessagesModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.msgTxt.setText("");
                database.getReference().child("Group Chat")
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });



            }
        });

    }
}