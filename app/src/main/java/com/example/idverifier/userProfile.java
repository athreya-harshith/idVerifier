package com.example.idverifier;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String ROLE="Role";

    public userProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static userProfile newInstance(String param1, String param2) {
        userProfile fragment = new userProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private user userData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            userData = new user();
            userData.setName(getArguments().getString("name"));
            userData.setEmail(getArguments().getString("email"));
            userData.setProfilePic(getArguments().getString("profilePic"));
        }
    }

    public static userProfile getInstance(String Role,Bundle bundle)
    {
        userProfile uProfile = new userProfile();
        bundle.putString(userProfile.ROLE,Role);
        uProfile.setArguments(bundle);

        return uProfile;
    }
    CardView userProfileCardView;
    ImageView userProfilePic;
    TextView rluserName,rluserEmail,userName,userEmail,userPhone,userBranch;
    private static final int PHOTO_REQUEST_CODE = 1;

    private Uri imagePath;
    Button editProfile;

    public FirebaseStorage storage;
    public FirebaseAuth auth ;
    public DatabaseReference dbref;

    private String Role;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        userProfileCardView = view.findViewById(R.id.userProfileCardView);
        userProfilePic = view.findViewById(R.id.userProfilePic);

        //in relative layout
        rluserName = view.findViewById(R.id.userProfileUserName);
        rluserEmail = view.findViewById(R.id.userProfileUserEmail);

        userName = view.findViewById(R.id.userProfileCardUserName);
        userEmail = view.findViewById(R.id.userProfileCardUserEmail);
        userPhone = view.findViewById(R.id.userProfileCardUserPhone);
        userBranch = view.findViewById(R.id.userProfileCardUserBranch);
        editProfile = view.findViewById(R.id.userProfileCardUserEditProfileBtn);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = auth.getCurrentUser();
        uid= user.getUid();

        //setting the data for user profile using bundle which is received through getArguments
        if(getArguments()!=null)
        {
            Role= getArguments().getString(userProfile.ROLE);

//            rluserName.setText(userData.getName());
//            rluserEmail.setText(userData.getEmail());
//
//            userName.setText(userData.getName());
//            userEmail.setText(userData.getEmail());
//
//            if(!userData.getProfilePic().equals(""))
//                Glide.with(getContext()).load(userData.getProfilePic()).into(userProfilePic);

        }



        FirebaseDatabase.getInstance().getReference().child("Users").child(Role).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getActivity() == null)
                    return;
               user data = snapshot.getValue(user.class);
               if(data != null)
               {
                   rluserName.setText(data.getName());
                   rluserEmail.setText(data.getEmail());
                   userName.setText(data.getName());
                   userEmail.setText(data.getEmail());
                   if(!data.getProfilePic().equals(""))
                       Glide.with(getContext()).load(data.getProfilePic()).into(userProfilePic);
                   else
                       userProfilePic.setImageResource(R.drawable.default_profile_pic);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        userProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,PHOTO_REQUEST_CODE);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                FirebaseStorage.getInstance().getReference().child("images").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
            }
        });
        return view;
    }

    private void updateProfilePicture(String url,String uid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(Role).child(uid).child("profilePic").setValue(url);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView()
    {
        Bitmap bitmap = null;
        try {
             bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),imagePath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        userProfilePic.setImageBitmap(bitmap);

        // upload image to the firestore
        if(imagePath == null)
        {
            Toast.makeText(getContext(),"Please upload image to make changes",Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog pd = new ProgressDialog(getContext());

        pd.setTitle("Uploading ... ");
        pd.show();
        FirebaseStorage.getInstance().getReference().child("images").child(uid).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                updateProfilePicture(task.getResult().toString(),uid);
                            }
                        }
                    });
                    Toast.makeText(getContext(),"Image Uploaded to Fire Storage", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Toast.makeText(getContext(),"Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                pd.setMessage("Uploading... "+(int)progress+" %");
            }
        });
    }
}