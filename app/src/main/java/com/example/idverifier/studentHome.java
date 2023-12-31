package com.example.idverifier;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link studentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class studentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public studentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static final String Role = "Student";
    public static studentHome getInstance(Bundle bundle)
    {
        studentHome studentHome = new studentHome();
        studentHome.setArguments(bundle);

        return studentHome;
    }
    public static studentHome newInstance(String param1, String param2) {
        studentHome fragment = new studentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String studentIdQrUrl,studentUid,studentName,studentEmail;
    ImageView studentIdQr;

    FloatingActionButton studentHomeRequestGatePass;
    Dialog gatePassDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            // obtaining the Qrid of student
            studentIdQrUrl = getArguments().getString("id");
            studentUid = getArguments().getString("uid");
            studentName = getArguments().getString("name");
            studentEmail = getArguments().getString("email");
        }
    }

    DatabaseReference dbref;
    TextView studentHomeUname,studentHomeUemail,studentHomeUserCurrentStatus;

    TextInputEditText toDestination,reason;

    TextView selectedDate;
    LinearLayout dateLayout;

    DatePickerDialog.OnDateSetListener dateSetListener;
    Button requestGpassBtn;
    public static final String IN = "Inside Campus",OUT = "Outside Campus";

    LinearLayout gPassLayout,gPassRootLayout,issuedGpassLayout;
    TextView gPassStatus;

    LinearLayout expandForGatePass;
    Dialog gpDialog;
    public static final String REQUESTED="GatePass Requested",ISSUED="GatePass Issued";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        studentIdQr = view.findViewById(R.id.studentIdQr);
        studentHomeUname = view.findViewById(R.id.studentHomeUserName);
        studentHomeUemail = view.findViewById(R.id.studentHomeUserEmail);
        studentHomeUserCurrentStatus = view.findViewById(R.id.studentHomeUserCurrentStatus);
        studentHomeRequestGatePass = view.findViewById(R.id.studentHomeRequestGatePass);
        gPassRootLayout = view.findViewById(R.id.gPassRootLayout);
        gPassLayout = view.findViewById(R.id.studentHomeGpassLayout);
        gPassStatus = view.findViewById(R.id.studentHomeGpassStatus);

        expandForGatePass = view.findViewById(R.id.gpExpandForGatePass);
        gatePassDialog =  new Dialog(getActivity());
        gatePassDialog.setContentView(R.layout.request_gatepass_dialog_box);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels*0.95);
        gatePassDialog.getWindow().setLayout(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        gatePassDialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getActivity().getResources(),R.drawable.custom_dialogue_shape,null));

        toDestination = gatePassDialog.findViewById(R.id.toDestination);
        reason = gatePassDialog.findViewById(R.id.reason);
        requestGpassBtn = gatePassDialog.findViewById(R.id.requestGatePassBtn);
        dateLayout = gatePassDialog.findViewById(R.id.selectDate);
        selectedDate = gatePassDialog.findViewById(R.id.selectedDate);

        // gatePass Dialog for the student
        gpDialog = new Dialog(getActivity());
        gpDialog.setContentView(R.layout.gatepass_dialog);
        gpDialog.getWindow().setLayout(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        gpDialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getActivity().getResources(),R.drawable.custom_dialogue_shape,null));

        studentHomeUname.setText(getArguments().getString("name"));
        studentHomeUemail.setText(getArguments().getString("email"));

        dbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(studentUid);

        if(studentIdQrUrl != "")
        {

            Glide.with(getContext()).load(studentIdQrUrl).into(studentIdQr);
        }

        dbref.child("currentStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Integer.class) == 1)
                    studentHomeUserCurrentStatus.setText(studentHome.IN);
                else
                    studentHomeUserCurrentStatus.setText(studentHome.OUT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if(snapshot.getKey().equals("currentStatus"))
                {
                    if(snapshot.getValue(Integer.class) == 1)
                        studentHomeUserCurrentStatus.setText(studentHome.IN);
                    else
                        studentHomeUserCurrentStatus.setText(studentHome.OUT);
//                    Toast.makeText(getContext(),previousChildName,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        studentHomeRequestGatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatePassDialog.show();
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year= cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year, month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month++;
                selectedDate.setText(day+"/"+month+"/"+year);
            }
        };

        requestGpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDest = toDestination.getText().toString();
                String reasonTo = reason.getText().toString();
                String date = selectedDate.getText().toString();

                if(toDest.equals("") || reasonTo.equals("")||date.equals(""))
                {
                    Toast.makeText(getContext(),"Enter all the details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    gatePass gp = new gatePass(studentUid,studentName,studentEmail,toDest,reasonTo,date);
                    FirebaseDatabase.getInstance().getReference().child("GatePassRequests").child(studentUid).setValue(gp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(getContext(),"Request Has Been Sent",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(),"Failed To request ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                gatePassDialog.dismiss();
            }
        });


        expandForGatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (gPassLayout.getVisibility() == View.GONE)?View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(gPassRootLayout,new AutoTransition());
                gPassLayout.setVisibility(v);
            }
        });

        // for updating the gatePass status
        FirebaseDatabase.getInstance().getReference().child("GatePassRequests").addChildEventListener(new ChildEventListener() {
            View view;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(getActivity() == null)
                    return;
                if(snapshot.getKey().equals(studentUid))
                {
                    gPassStatus.setText(studentHome.REQUESTED);

                    gatePass gp;
                    gp = snapshot.getValue(gatePass.class);
                    view = getLayoutInflater().inflate(R.layout.student_gatepass_requested,null,false);
                    TextView tv = view.findViewById(R.id.gpassDestination);
                    tv.setText(gp.getToDestination());
                    tv = view.findViewById(R.id.gpassTravellingDate);
                    tv.setText(gp.getTravelDate());
                    tv = view.findViewById(R.id.gpassReason);
                    tv.setText(gp.getReason());
                    gPassLayout.addView(view);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getKey().equals(studentUid))
                {
                    gPassStatus.setText("No Gate Passes");
                    gPassLayout.removeView(view);
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("IssuedGatePass").addChildEventListener(new ChildEventListener() {
            View view;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(getActivity()== null)
                    return;
                if(snapshot.getKey().equals(studentUid))
                {
                    gPassStatus.setText(studentHome.ISSUED);

                    gatePass gp;
                    gp = snapshot.getValue(gatePass.class);
                    view = getLayoutInflater().inflate(R.layout.issued_gatepass,null,false);
                    // continue from here after dinner
                    ImageView gPassQr = view.findViewById(R.id.issuedGpassQr);
                    Glide.with(getContext()).load(gp.getGpassId()).into(gPassQr);
                    TextView tv = view.findViewById(R.id.issuedGpassDestination);
                    tv.setText(gp.getToDestination());
                    tv = view.findViewById(R.id.issuedGpassDateOfTravelling);
                    tv.setText(gp.getTravelDate());
                    tv = view.findViewById(R.id.issuedGpassReason);
                    tv.setText(gp.getReason());
                    tv = view.findViewById(R.id.issuedGpassIssuedTime);
                    tv.setText(gp.getIssuedTime());
                    gPassLayout.addView(view);
                    issuedGpassLayout = view.findViewById(R.id.issuedGpassLayout);

                    showGatePassDialog(gp,issuedGpassLayout);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.getKey().equals(studentUid))
                {
                    gPassStatus.setText("No Gate Passes");
                    gPassLayout.removeView(view);
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    private void showGatePassDialog(gatePass gpIssued,LinearLayout issuedGpassLayout) {
        ImageView gpQr = gpDialog.findViewById(R.id.gPassDialogQr);

        TextView tv = gpDialog.findViewById(R.id.gpDialogToDestinatin);
        tv.setText(gpIssued.getToDestination());
        tv = gpDialog.findViewById(R.id.gpDialogReason);
        tv.setText(gpIssued.getReason());
        tv = gpDialog.findViewById(R.id.gpDialogTravelDate);
        tv.setText(gpIssued.getTravelDate());
        tv = gpDialog.findViewById(R.id.gpDialogIssuedDate);
        tv.setText(gpIssued.getIssuedTime());
        issuedGpassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null)
                    Glide.with(getContext()).load(gpIssued.getGpassId()).into(gpQr);
                gpDialog.show();
            }
        });
    }
}