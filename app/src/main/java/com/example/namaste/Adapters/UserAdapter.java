package com.example.namaste.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namaste.ChatDetailActivity;
import com.example.namaste.Models.Users;
import com.example.namaste.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    ArrayList<Users> list;
    Context context;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
   final Users users=list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_profile).into(holder.imageView);
        holder.name.setText(users.getUsername());

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                .orderByChild("timestamp").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot snapshot1:snapshot.getChildren())
                            {
                                holder.last.setText(snapshot1.child("message").getValue().toString());

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic", users.getProfilepic());
                intent.putExtra("userName", users.getUsername());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      ImageView imageView;
      TextView name,last;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profile_image);
            name=itemView.findViewById(R.id.name_id);
            last=itemView.findViewById(R.id.last_msg_id);

        }
    }
}
