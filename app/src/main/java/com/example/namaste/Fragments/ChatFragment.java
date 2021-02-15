package com.example.namaste.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.namaste.Adapters.UserAdapter;
import com.example.namaste.Models.Users;
import com.example.namaste.R;
import com.example.namaste.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }
    ArrayList<Users>  list=new ArrayList<>();
    FirebaseDatabase database;
  FragmentChatBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater,container,false);
     database=FirebaseDatabase.getInstance();
        final UserAdapter adapter=new UserAdapter(list,getContext());
     binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
  database.getReference().child("users").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
          list.clear();
          for(DataSnapshot dataSnapshot:snapshot.getChildren())
          {
              Users users=dataSnapshot.getValue(Users.class);
              users.getUserId(dataSnapshot.getKey());
              if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
              list.add(users);

          }
          adapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
  });

        return binding.getRoot();
    }
}