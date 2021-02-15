package com.example.namaste.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.namaste.Fragments.CallFragment;
import com.example.namaste.Fragments.ChatFragment;
import com.example.namaste.Fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch(position)
       {
           case 1:return new StatusFragment();
           case 2:return new CallFragment();
           default:return new ChatFragment();
       }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0)
        {
            title="CHATS";
        }
        if(position==1)
        {
            title="STATUS";
        }
        if(position==2)
        {
            title="CALLS";
        }
        return title;
       // return super.getPageTitle(position);
    }
}
