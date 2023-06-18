package com.example.idverifier;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class adminHomeViewPagerAdapter extends FragmentPagerAdapter {

    public adminHomeViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return  new adminHomePendingGatePass();
        else
            return  new adminHomeIssuedGatePass();
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
            return "Pending Requests";
        else
            return "Issued ";
    }
}
