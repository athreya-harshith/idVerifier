package com.example.idverifier;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link securityHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class securityHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public securityHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment securityHome.
     */
    // TODO: Rename and change types and number of parameters


    public static securityHome newInstance(String param1, String param2) {
        securityHome fragment = new securityHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static securityHome getInstance(Bundle bundle)
    {
       securityHome  scHome = new securityHome();
        scHome.setArguments(bundle);

        return scHome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            securityName = getArguments().getString("name");
        }
//        scanCode();
    }

    FloatingActionButton securityHomeScanBtn;
    Dialog scanDialog;
    ImageView scanDialogImg;
    TextView scanDialogUname,scanDialogUemail,scanDialogUbranch,scanDialogOutMode;
    Button scandialogAllowBtn,scanDialogRejectBtn;
    Vibrator vibrator;
    String uidAfterScan,uNameAfterScan,securityName;
    RecyclerView checkoutRecyclerView;
    checkOutAdapter cOutAdapter;

    public static final String ID = "id",GPASS = "gatePass";
    private  boolean checkId = false;// to identify whether the qr is id or gpass
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security_home, container, false);

        securityHomeScanBtn = view.findViewById(R.id.securityHomeScanBtn);
        scanDialog = new Dialog(getActivity());
        scanDialog.setContentView(R.layout.secruity_scan_dialog);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels*0.95);
        scanDialog.getWindow().setLayout(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        scanDialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getActivity().getResources(),R.drawable.custom_dialogue_shape,null));

        scanDialogImg = scanDialog.findViewById(R.id.securityScanStudentImage);
        scandialogAllowBtn = scanDialog.findViewById(R.id.securityScanAllowBtn);
        scanDialogRejectBtn = scanDialog.findViewById(R.id.securityScanRejectBtn);
        scanDialogUname = scanDialog.findViewById(R.id.securityScanUname);
        scanDialogUemail = scanDialog.findViewById(R.id.securityScanUemail);
        scanDialogUbranch = scanDialog.findViewById(R.id.securityScanUbranch);
        scanDialogOutMode = scanDialog.findViewById(R.id.scanDialogOutMode);
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        checkoutRecyclerView = view.findViewById(R.id.securityHomeCheckoutRecyclerView);
        checkoutRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<checkOut> options =
                new FirebaseRecyclerOptions.Builder<checkOut>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CheckOut"), checkOut.class)
                        .build();
        cOutAdapter = new checkOutAdapter(options);
        checkoutRecyclerView.setAdapter(cOutAdapter);
        securityHomeScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        scandialogAllowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if currentStatus is 1 then inside the campus else outside the campus
                FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uidAfterScan).child("currentStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue(Integer.class) == 1)
                        {
                            FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uidAfterScan).child("currentStatus").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        checkOut userOut ;
                                        if(checkId)
                                            userOut = new checkOut(uidAfterScan,uNameAfterScan,checkOut.ID_MODE,currentTime,currentDate,securityName);
                                        else
                                            userOut =  new checkOut(uidAfterScan,uNameAfterScan,checkOut.GATEPASS_MODE,currentTime,currentDate,securityName);

                                        FirebaseDatabase.getInstance().getReference().child("CheckOut").child(uidAfterScan).setValue(userOut).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                    Toast.makeText(getContext(),"Verified successfully for going out ",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    else
                                        Toast.makeText(getContext(),"Unable to Verify",Toast.LENGTH_SHORT).show();
                                }
                            });

                            if(!checkId)
                            {
                                // code here for the gatePass modification
                                FirebaseDatabase.getInstance().getReference().child("IssuedGatePass").child(uidAfterScan).child("validity").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(getContext(),"Updated the GPass Validity",Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(),"Failed to Update the GPass Validity",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        else {
                            // if not in campus then 2 cases 1.went out with ID, or 2.Went out with Gpass
                            // check through the CheckOut table(the mode filed ) and then handle the details
                            // for entering inside the campus, showing id or gatePass is enough
                            // check in checkOut table and take the validity expiration of gatePass if mode is gpass

                            FirebaseDatabase.getInstance().getReference().child("CheckOut").child(uidAfterScan).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String previousOutMode = snapshot.child("mode").getValue(String.class);

                                    if(previousOutMode.equals(securityHome.GPASS))
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("IssuedGatePass").child(uidAfterScan).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                    Toast.makeText(getContext(),"Issued Gate Pass has been deleted",Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(getContext(),"Failed in deleting Issued Gate Pass",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // remove from Student
                                        FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uidAfterScan).child("gPass").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                        // remove from firebase storage

                                        FirebaseStorage.getInstance().getReference().child("gatePassQr").child(uidAfterScan).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                    Toast.makeText(getContext(),"Revoked the GatePass Qr",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uidAfterScan).child("currentStatus").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                FirebaseDatabase.getInstance().getReference().child("CheckOut").child(uidAfterScan).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                            Toast.makeText(getContext(), "Verified successfully for getting in", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            else
                                                Toast.makeText(getContext(), "Unable to Verify", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        scanDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        scanDialogRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanDialog.dismiss();
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        cOutAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        cOutAdapter.stopListening();
    }
    private  void scanCode()
    {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to turn on the flash");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureQrCode.class);
        qrLauncher.launch(scanOptions);
    }

    ActivityResultLauncher<ScanOptions> qrLauncher  = registerForActivityResult(new ScanContract(), new ActivityResultCallback<ScanIntentResult>() {
        @Override
        public void onActivityResult(ScanIntentResult result) {
            if(result.getContents() != null)
            {
                if(vibrator.hasVibrator())
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        vibrator.vibrate(VibrationEffect.createOneShot(400,VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                    {
                        long[] pattern = {0,400,0,0};
                        vibrator.vibrate(pattern,-1);
                    }
                }
                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setTitle("Processing");
                pd.setMessage("Fetching the Student Details ....");
                String received = result.getContents();
                String [] contents = received.split("##",3);
                Log.d("The contents array ****** ",String.valueOf(contents.length));
                if(contents.length ==2 || contents.length ==3)
                {
                    if(contents.length==2)
                        checkId = true;// scanned a idCard
                    else
                        checkId = false;// scanned a QrCode
                    // this is for id content length is 2 uid##email
                    String receivedEmail = contents[1];
                    String receivedUid = contents[0];
//
                    uidAfterScan = receivedUid;
                    pd.show();
                    // to check whether the student is registered or not
                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(receivedEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean check = !task.getResult().getSignInMethods().isEmpty();
                            if(check)
                            {
                                // comes here if the student details exists
                                FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(receivedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user student = snapshot.getValue(user.class);
                                        if(student != null)
                                        {
                                            if(!student.getProfilePic().equals(""))
                                                Glide.with(getContext()).load(student.getProfilePic()).into(scanDialogImg);
                                            uNameAfterScan = student.getName();
                                            scanDialogUname.setText(student.getName());
                                            scanDialogUemail.setText(student.getEmail());
                                            if(!student.getBranch().equals(""))
                                                scanDialogUbranch.setText(student.getBranch());
                                            else
                                                scanDialogUbranch.setText("No Branch Mentioned");
                                            if(checkId)
                                                scanDialogOutMode.setText(securityHome.ID);
                                            else
                                                scanDialogOutMode.setText(securityHome.GPASS);
                                            pd.dismiss();
                                            scanDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error)
                                    {// it has email registered but the account is not present in realtime database
                                        pd.dismiss();
                                        Toast.makeText(getContext(),"No such User Exists",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                // comes here if the student is not registered
                                pd.dismiss();
                                Toast.makeText(getContext(),"Invalid QR code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // code here for content length 3 id uid##email##gpass
                }
                else
                {
                    Toast.makeText(getContext(),"Invalid Qr Code ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

}