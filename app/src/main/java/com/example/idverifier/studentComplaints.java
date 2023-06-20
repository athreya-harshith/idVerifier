package com.example.idverifier;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link studentComplaints#newInstance} factory method to
 * create an instance of this fragment.
 */
public class studentComplaints extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public studentComplaints() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studentComplaints.
     */
    // TODO: Rename and change types and number of parameters
    public static studentComplaints newInstance(String param1, String param2) {
        studentComplaints fragment = new studentComplaints();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static studentComplaints getInstance(Bundle bundle)
    {
        studentComplaints complaints = new studentComplaints();
        complaints.setArguments(bundle);
        return complaints;
    }
    String studentName,studentUid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            studentName = getArguments().getString("name");
            studentUid = getArguments().getString("uid");
        }
    }

    TextInputEditText studentComplaints;
    TextInputLayout textInputLayoutComplaints;
    String enteredMsg,msgTitle,msgText;

    RecyclerView individualComplaints;

    complaintsAdapter complainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_complaints, container, false);
        textInputLayoutComplaints = view.findViewById(R.id.studentComplaintsTextInputLayout);
        studentComplaints = view.findViewById(R.id.studentCompaintsComplaint);
        individualComplaints = view.findViewById(R.id.studentComplaintsRecyclerView);
        textInputLayoutComplaints.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to disable keyboard
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                enteredMsg= studentComplaints.getText().toString();
                if(enteredMsg.equals(""))
                    Toast.makeText(getContext(),"Enter some Reason to Complain",Toast.LENGTH_SHORT).show();
                else
                {
                    String[] contents = enteredMsg.split(":",2);
                    if(contents.length == 2)
                    {
                        msgTitle = contents[0];
                        msgText = contents[1];
                    }
                    else
                    {
                        msgTitle="#Compaint";
                        msgText  = contents[0];
                    }
                    studentComplaints.setText("");
                    complaints complain = new complaints(msgTitle,msgText,studentName,studentUid);

                    ProgressDialog pd = new ProgressDialog(getContext());

                    pd.setTitle("Processing");
                    pd.setMessage("Raising The Complaint");
                    pd.show();

                    FirebaseDatabase.getInstance().getReference().child("Complaints").push().setValue(complain).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(getContext(),"Complained To Authority",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(),"Failed To Complain",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("IndividualComplaints").child(studentUid).push().setValue(complain).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(getContext(),"Complaint Registered By "+studentName,Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(),"Complaint Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        individualComplaints.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        FirebaseRecyclerOptions<complaints> options =
                new FirebaseRecyclerOptions.Builder<complaints>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("IndividualComplaints").child(studentUid), complaints.class)
                        .build();
        complainAdapter = new complaintsAdapter(options);
        individualComplaints.setAdapter(complainAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        complainAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        complainAdapter.stopListening();
    }
}