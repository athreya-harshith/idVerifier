package com.example.idverifier;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminHome.
     */
    // TODO: Rename and change types and number of parameters
    public static adminHome newInstance(String param1, String param2) {
        adminHome fragment = new adminHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Bundle bundle;
    public static adminHome getInstance(Bundle bundle)
    {
        adminHome  adHome = new adminHome();
        adHome.setArguments(bundle);

        return adHome;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            bundle.putString("name",getArguments().getString("name"));
            bundle.putString("uid",getArguments().getString("uid"));
        }
    }

// starting the code for the viewpager for the adminHome
    TabLayout adminHomeTabLayout;
    ViewPager adminHomeViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        adminHomeTabLayout = view.findViewById(R.id.adminHomeTabLayout);
        adminHomeViewPager = view.findViewById(R.id.adminHomeViewPager);

//        Log.d("Inside the admin Home page pending Gatepass ","Checking why the ui is not behaving as required");
        adminHomeViewPagerAdapter adminHomePageAdapter = new adminHomeViewPagerAdapter(getChildFragmentManager());
        adminHomeViewPager.setAdapter(adminHomePageAdapter);
        adminHomeTabLayout.setupWithViewPager(adminHomeViewPager);
        return view;

    }
}