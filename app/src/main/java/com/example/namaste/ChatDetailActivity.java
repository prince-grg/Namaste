package com.example.namaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.namaste.Adapters.ChatAdapter;
import com.example.namaste.Models.MessagesModel;
import com.example.namaste.databinding.ActivityChatDetailBinding;
import com.example.namaste.notifications.APIService;
import com.example.namaste.notifications.Client;
import com.example.namaste.notifications.MyResponse;
import com.example.namaste.notifications.Data;
import com.example.namaste.notifications.Sender;
import com.example.namaste.notifications.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


         database=FirebaseDatabase.getInstance();
         auth=FirebaseAuth.getInstance();
         final String senderId=auth.getUid();
         final String recieveId=getIntent().getStringExtra("userId");
         final String usrname=getIntent().getStringExtra("userName");
         String profilepic=getIntent().getStringExtra("profilePic");
         binding.username.setText(usrname);
        Picasso.get().load(profilepic).placeholder(R.drawable.ic_profile).into(binding.profileImage);
          binding.backBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(ChatDetailActivity.this,MainActivity.class));
              }
          });
     final ArrayList<MessagesModel> messagesModels=new ArrayList<>();
     final ChatAdapter chatAdapter=new ChatAdapter(messagesModels,this,recieveId);
     binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        final String senderRoom=senderId+recieveId;
        final String recieverRoom=recieveId+senderId;
        layoutManager.setStackFromEnd(true);


        database.getReference().child("chats")
                .child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    MessagesModel model=snapshot1.getValue(MessagesModel.class);
                    model.setMessageId(snapshot1.getKey());
                    messagesModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();
                binding.chatRecyclerView.scrollToPosition(chatAdapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   UpdateToken();


       binding.btnSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String messsage=binding.msgTxt.getText().toString();
               //Toast.makeText(ChatDetailActivity.this, ""+recieveId, Toast.LENGTH_SHORT).show();
               FirebaseDatabase.getInstance().getReference().child("Tokens").child(recieveId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       String usertoken=dataSnapshot.getValue(String.class);
                       sendNotifications(usertoken,usrname,messsage);


                       // Toast.makeText(ChatDetailActivity.this, "i am in data ", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });




               final MessagesModel model=new MessagesModel(senderId,messsage);
               model.setTimestamp(new Date().getTime());
               binding.msgTxt.setText("");
               database.getReference().child("chats").child(senderRoom)
                       .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       database.getReference().child("chats").child(recieverRoom)
                               .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {

                           }
                       });
                   }
               });
           }
       });
    }
    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        Sender sender = new Sender(data, usertoken);
       // Toast.makeText(this, "I am in send "+usertoken, Toast.LENGTH_SHORT).show();
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ChatDetailActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }

                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}