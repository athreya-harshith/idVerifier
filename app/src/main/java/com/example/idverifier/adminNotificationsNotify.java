package com.example.idverifier;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminNotificationsNotify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminNotificationsNotify extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminNotificationsNotify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminNotificationsNotify.
     */
    // TODO: Rename and change types and number of parameters
    public static adminNotificationsNotify newInstance(String param1, String param2) {
        adminNotificationsNotify fragment = new adminNotificationsNotify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextInputEditText adminNotifications;
    TextInputLayout textInputLayoutNotifications;
    String adminName ,adminUid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            adminName = getArguments().getString("name");
            adminUid = getArguments().getString("uid");
        }
    }

    public static adminNotificationsNotify getInstance(Bundle bundle)
    {
        adminNotificationsNotify adminNotificationNotify = new adminNotificationsNotify();
        adminNotificationNotify.setArguments(bundle);

        return adminNotificationNotify;
    }
    String enteredMsg;
    String notificationTitle,notificationText;

    RecyclerView adminNotificationsRecyclerView;
    notificationsAdapter notifyAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // for adjsuting the screen size
        if(getActivity() != null)
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notifications_notify, container, false);
        textInputLayoutNotifications = view.findViewById(R.id.adminNotificationsNotifyTextInputLayout);
        adminNotifications= view.findViewById(R.id.adminNotificationsNotify);

        adminNotificationsRecyclerView = view.findViewById(R.id.adminNotificationRecyclerView);
        textInputLayoutNotifications.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to disable the keyboard
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                enteredMsg = adminNotifications.getText().toString();
                if(enteredMsg.equals(""))
                    Toast.makeText(getContext(),"Enter some text to Notify",Toast.LENGTH_SHORT).show();
                else
                {
                    String[] contents = enteredMsg.split(":",2);
                    if(contents.length == 2)
                    {
                        notificationTitle = contents[0];
                        notificationText = contents[1];
                    }
                    else
                    {
                        notificationTitle="Admin Notification";
                        notificationText = contents[0];
                    }
                    adminNotifications.setText("");
                    notification newnotification = new notification(notificationTitle,notificationText,adminName);
                    FirebaseDatabase.getInstance().getReference().child("AdminNotifications").push().setValue(newnotification).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getContext(),"Notification Sent Successfully",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getContext(),"Failed to Send Notification",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                }
            }
        });

        WrapContentLinearLayoutManager wm = new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adminNotificationsRecyclerView.setLayoutManager(wm);
        FirebaseRecyclerOptions<notification> options =
                new FirebaseRecyclerOptions.Builder<notification>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AdminNotifications"), notification.class)
                        .build();
        notifyAdapter = new notificationsAdapter(options);
        adminNotificationsRecyclerView.setAdapter(notifyAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        notifyAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        notifyAdapter.stopListening();
    }
}