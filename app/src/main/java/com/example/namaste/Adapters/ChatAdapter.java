package com.example.namaste.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namaste.Models.MessagesModel;
import com.example.namaste.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ChatAdapter extends RecyclerView.Adapter {

 ArrayList<MessagesModel>messagesModels;
 Context context;
 String recId;
  int SENDER_VIEW_TYPE=1;
  int RECIEVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context, String recId) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new RecieverViewHolder(view);
        }

        //return null;
    }


    @Override
    public int getItemViewType(int position) {
       if(messagesModels.get(position).getiId().equals(FirebaseAuth.getInstance().getUid()))
       {
           return SENDER_VIEW_TYPE;
       }
       else {return RECIEVER_VIEW_TYPE;}
      //  return super.getItemViewType(position);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     final MessagesModel messagesModel=messagesModels.get(position);

     holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View view) {

             new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you Sure?")
                     .setPositiveButton("yes",new DialogInterface.OnClickListener(){

                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {

                             FirebaseDatabase database=FirebaseDatabase.getInstance();
                             String sender=FirebaseAuth.getInstance().getUid()+recId;
                            database.getReference().child("chats").child(sender)
                                    .child(messagesModel.getMessageId())
                                    .setValue(null);
                             Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();




                         }
                     }).setNegativeButton("no",new DialogInterface.OnClickListener(){
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.dismiss();
                 }
             }).show();

      return false;
         }
     });
     if(holder.getClass()==SenderViewHolder.class)
     {
         ((SenderViewHolder)holder).Sendermsg.setText(messagesModel.getMessage());
         ((SenderViewHolder)holder).Sendertime.setText(getDate(messagesModel.getTimestamp()));

     }
     else {
         ((RecieverViewHolder)holder).recievermsg.setText(messagesModel.getMessage());
         ((RecieverViewHolder)holder).recieverTime.setText(getDate(messagesModel.getTimestamp()));
     }


    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }




    public  class  RecieverViewHolder extends RecyclerView.ViewHolder{
      TextView recievermsg,recieverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievermsg=itemView.findViewById(R.id.msg_reciever);
            recieverTime=itemView.findViewById(R.id.time_reciever);


        }
    }




    public class SenderViewHolder extends RecyclerView.ViewHolder{
  TextView Sendermsg,Sendertime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            Sendermsg=itemView.findViewById(R.id.msg_sender);
            Sendertime=itemView.findViewById(R.id.time_sender);
        }
    }
    private String getDate(Long tie)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a" );
        return   formatter.format(new Date(Long.parseLong(String.valueOf(tie))));

    }



}
