package com.example.idverifier;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class adminNotificationsViewPagerAdapter extends FragmentPagerAdapter {
    Bundle receivedBundle;
    public adminNotificationsViewPagerAdapter(@NonNull FragmentManager fm, Bundle bundle) {
        super(fm);
        receivedBundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0 )
            return  new adminNotificationsComplaints();
        else
            return  adminNotificationsNotify.getInstance(receivedBundle);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        if(position == 0 )
            return "Complaints";
        else
            return "Notify ";
    }
}
