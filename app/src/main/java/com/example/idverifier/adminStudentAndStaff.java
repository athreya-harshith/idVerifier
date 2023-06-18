package com.example.idverifier;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminStudentAndStaff#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminStudentAndStaff extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public adminStudentAndStaff() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminStudentAndStaff.
     */
    // TODO: Rename and change types and number of parameters
    public static adminStudentAndStaff newInstance(String param1, String param2) {
        adminStudentAndStaff fragment = new adminStudentAndStaff();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FloatingActionButton FASwitchUserBtn,FAAddUserBtn;
    ExtendedFloatingActionButton ExtendedFABtn;

    String[] roles = {"Student","Security","Administrator"};
    Dialog addUserDialog;

    TextInputEditText addUsersDialogUserName,addUsersDialogueUserEmail;
    AutoCompleteTextView addUsersDialogUserRole;
    String role;
    Button addUserDialogAddBtn;
    ProgressBar addUserProgressBar;
    ArrayAdapter<String> adapterRoles;

    FirebaseAuth auth ;
    boolean checkAllBtn= false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_student_and_staff, container, false);
        ExtendedFABtn = view.findViewById(R.id.adminSAndSExtendedFloatingActionBtn);
        FASwitchUserBtn = view.findViewById(R.id.adminSAndSFloatingActionBtnSwitchUser);
        FAAddUserBtn = view.findViewById(R.id.adminSAndSFloatingActionBtnAddUser);

        FAAddUserBtn.setVisibility(View.GONE);
        FASwitchUserBtn.setVisibility(View.GONE);

        checkAllBtn = false;
        ExtendedFABtn.shrink();
        //firebase auth
        auth = FirebaseAuth.getInstance();
        // addUserDialog

        addUserDialog = new Dialog(getActivity());//use getActivity for fragments
        addUserDialog.setContentView(R.layout.add_users_dialogue_box);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels*0.95);
        addUserDialog.getWindow().setLayout(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        addUserDialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getActivity().getResources(),R.drawable.custom_dialogue_shape,null));
        addUsersDialogUserName = addUserDialog.findViewById(R.id.addUsersDialogueUserName);
        addUsersDialogueUserEmail = addUserDialog.findViewById(R.id.addUsersDialogueUserEmail);
        addUserDialogAddBtn = addUserDialog.findViewById(R.id.addUsersDialogueAddBtn);
        addUsersDialogUserRole = addUserDialog.findViewById(R.id.addUsersDialogueUserRole);
        addUserProgressBar = addUserDialog.findViewById(R.id.addUsersDialogueProgressBar);
        adapterRoles = new ArrayAdapter<>(getActivity(),R.layout.user_roles_list,roles);

        addUsersDialogUserRole.setAdapter(adapterRoles);

        addUsersDialogUserRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                role = adapterView.getItemAtPosition(i).toString();
            }
        });
        //extended floating action button
        ExtendedFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkAllBtn)
                {
                    FAAddUserBtn.show();
                    FASwitchUserBtn.show();

                    ExtendedFABtn.extend();
                    checkAllBtn = true;
                }
                else
                {
                    FAAddUserBtn.hide();
                    FASwitchUserBtn.hide();

                    ExtendedFABtn.shrink();
                    checkAllBtn = false;
                }
            }
        });
        //showing the dialog box when FAAdduserbtn is pressed
        FAAddUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserDialog.show();
            }
        });

        // for adding the user from dialog box
        addUserDialogAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName="",userEmail="";
                if(!TextUtils.equals(addUsersDialogUserName.getText(),null))
                {
                   userName = addUsersDialogUserName.getText().toString();
                }
                if(!TextUtils.equals(addUsersDialogueUserEmail.getText(),null))
                {
                    userEmail = addUsersDialogueUserEmail.getText().toString();
                }
                if(userName.equals("")|| userEmail.equals(""))
                {
                    Toast.makeText(getContext(),"Credentials Cannot Be Empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String password =  userEmail.substring(0,userEmail.length()-10);
                    addUserProgressBar.setVisibility(View.VISIBLE);
                    if(password.length() <6 )
                    {
                        addUsersDialogueUserEmail.setText("");
                        userEmail = "";
                        Toast.makeText(getContext(),"Use Email having minimum 6 characters before @gmail.com",Toast.LENGTH_SHORT).show();
                        addUserProgressBar.setVisibility(View.GONE);
                        // here no need to dismiss the dialog
                    }
                    else
                    {
                        try {
                            registerUser(userName,userEmail,password);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(),"Some Exception is Caught while registering the users",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        addUserDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                addUsersDialogueUserEmail.setText("");
                addUsersDialogUserName.setText("");
                addUsersDialogUserRole.clearListSelection();
            }
        });
        return view;
    }

    private void registerUser(String userName, String userEmail, String password) throws Exception
    {

        auth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    String uid = task.getResult().getUser().getUid();
                    user add = new user(userName,uid,userEmail,role);

                    FirebaseDatabase.getInstance().getReference().child("Users").child(role).child(uid).setValue(add).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            addUserProgressBar.setVisibility(View.GONE);
                            addUserDialog.dismiss();
                            addUsersDialogUserName.setText("");
                            addUsersDialogueUserEmail.setText("");
                            if(role=="Student")
                            {
                                generateQR(uid,userEmail);
                            }
                            Toast.makeText(getContext(),"User with "+uid+"created ",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            addUserProgressBar.setVisibility(View.GONE);
                            addUserDialog.dismiss();
                            addUsersDialogUserName.setText("");
                            addUsersDialogueUserEmail.setText("");
                            Toast.makeText(getContext(),"Unable to Create the user ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    addUserProgressBar.setVisibility(View.GONE);
                    addUserDialog.dismiss();
                    addUsersDialogUserName.setText("");
                    addUsersDialogueUserEmail.setText("");
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getContext(),"The user with Credentials already Exists",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //method to generate QR
    private void generateQR(String uid,String email)
    {
//        String text = String.valueOf(qrText.getText());

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            if(TextUtils.isEmpty(uid) || TextUtils.isEmpty(email))
            {
                Toast.makeText(getContext(),"No Proper uid  or Email obtained for Generating QR",Toast.LENGTH_SHORT).show();
                return;
            }
            String id = uid+"##"+email;
            BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE,750,750);//gives the matrix value
            // we must convert that to bitmap value to view as image
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage.getInstance().getReference().child("StudentId").child(uid).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if(task.isSuccessful())
                            {
                                String  url = task.getResult().toString();
                                updateUserId(url,uid);
                                Toast.makeText(getContext(),"Even Updated the Qr ID",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });



        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void updateUserId(String url,String uid)
    {
       FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uid).child("id").setValue(url);
    }
}