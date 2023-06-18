package com.example.idverifier;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminNotifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminNotifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminNotifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminNotifications.
     */
    // TODO: Rename and change types and number of parameters
    public static adminNotifications newInstance(String param1, String param2) {
        adminNotifications fragment = new adminNotifications();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static adminNotifications getInstance(Bundle bundle)
    {
        adminNotifications adminNoti = new adminNotifications();
        adminNoti.setArguments(bundle);

        return adminNoti;
    }

    Bundle notifcationBundle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            notifcationBundle = new Bundle();
            notifcationBundle.putString("name",getArguments().getString("name"));
            notifcationBundle.putString("uid",getArguments().getString("uid"));
        }
    }
    TabLayout adminNotificationsTabLayout;
    ViewPager adminNotificationsViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notifications, container, false);
        adminNotificationsTabLayout = view.findViewById(R.id.adminNotificationsTabLayout);
        adminNotificationsViewPager = view.findViewById(R.id.adminNotificationsViewPager);

        adminNotificationsViewPagerAdapter adminNotificationsAdapter = new adminNotificationsViewPagerAdapter(getChildFragmentManager(),notifcationBundle);
        adminNotificationsViewPager.setAdapter(adminNotificationsAdapter);
        adminNotificationsTabLayout.setupWithViewPager(adminNotificationsViewPager);
        return view;
    }
}