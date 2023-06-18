package com.example.idverifier;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminHomePendingGatePass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminHomePendingGatePass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminHomePendingGatePass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminHomePendingGatePass.
     */
    // TODO: Rename and change types and number of parameters
    public static adminHomePendingGatePass newInstance(String param1, String param2) {
        adminHomePendingGatePass fragment = new adminHomePendingGatePass();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView gpassPendingRecyclerView;
    gatePassAdapter gpAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home_pending_gate_pass, container, false);
        gpassPendingRecyclerView = view.findViewById(R.id.pendingGpassRecyclerView);
        gpassPendingRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FirebaseRecyclerOptions<gatePass> options =
                new FirebaseRecyclerOptions.Builder<gatePass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("GatePassRequests"), gatePass.class)
                        .build();
        gpAdapter = new gatePassAdapter(options,getContext(),getActivity());
        gpassPendingRecyclerView.setAdapter(gpAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        gpAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        gpAdapter.stopListening();
    }
}