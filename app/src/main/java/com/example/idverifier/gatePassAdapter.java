package com.example.idverifier;
//this is used as adapter for issued gatePass
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class gatePassAdapter extends FirebaseRecyclerAdapter<gatePass,gatePassAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    FragmentActivity activity;
    ProgressDialog pd;
    public gatePassAdapter(@NonNull FirebaseRecyclerOptions<gatePass> options, Context context, FragmentActivity activity){
        super(options);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull gatePassAdapter.myViewHolder holder, int position, @NonNull gatePass model) {
        holder.name.setText(model.getName());
        holder.destination.setText(model.getToDestination());
        holder.reason.setText(model.getReason());
        holder.date.setText(model.getTravelDate());

        FirebaseStorage.getInstance().getReference().child("images").child(model.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    if(activity == null )
                        return;
                    else
                        Glide.with(context).load(task.getResult()).into(holder.gpassImg);
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("GatePassRequests").child(model.getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(view.getContext(), "Request Ignored Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        pd = new ProgressDialog(context);
        pd.setTitle("Processing");
        pd.setMessage("Issuing the Gate Pass");
        holder.issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("The model Contents are ...",model.getEmail()+model.getUid());
                generateQR(model.getUid(),model.getEmail(),model);
            }
        });

    }
    private void generateQR(String uid,String email,gatePass model)
    {
//        String text = String.valueOf(qrText.getText());
        pd.show();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            if(TextUtils.isEmpty(uid) || TextUtils.isEmpty(email))
            {
                pd.dismiss();
                Toast.makeText(context,"No Proper uid  or Email obtained for Generating Gate Pass",Toast.LENGTH_SHORT).show();
                return;
            }
            String id = uid+"##"+email+"##"+"gpass";
            BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE,750,750);//gives the matrix value
            // we must convert that to bitmap value to view as image
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage.getInstance().getReference().child("gatePassQr").child(uid).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if(task.isSuccessful())
                            {
                                String  url = task.getResult().toString();
                                updateUsergPass(url,uid,model);
                                model.setValidity(0);// 0-> out -> 1->in -> remove the gpass
                                Toast.makeText(context,"Updated Qr For Gate Pass",Toast.LENGTH_SHORT).show();
                                pd.setMessage("Updating Qr in the Entry");
                                removeFromRequests(uid,model);
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

    private void removeFromRequests(String uid,gatePass model) {

        FirebaseDatabase.getInstance().getReference().child("GatePassRequests").child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //here adding the time of issuing the gatePass
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String issuedDateAndTime = currentTime+"  "+currentDate;
                    model.setIssuedTime(issuedDateAndTime);
                    addToIssued(uid,model);
                }

            }
        });
    }

    private void addToIssued(String uid, gatePass model)
    {
        FirebaseDatabase.getInstance().getReference().child("IssuedGatePass").child(uid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(context,"Issued The Gate Pass",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void updateUsergPass(String url,String uid,gatePass model)
    {
        model.setGpassId(url);//here gpassId is url to the QR
        FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(uid).child("gPass").setValue(model);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_gatepass_request_recycler_view,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView gpassImg;
        TextView name,destination,date,reason;
        Button issue,reject;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            gpassImg = itemView.findViewById(R.id.gpassRequestImg);

            name = itemView.findViewById(R.id.gpassStudentName);
            destination = itemView.findViewById(R.id.gpassDestination);
            date = itemView.findViewById(R.id.gpassTravellingDate);
            reason = itemView.findViewById(R.id.gpassReason);
            issue = itemView.findViewById(R.id.gpassIssueBtn);
            reject = itemView.findViewById(R.id.gpassRejectBtn);


        }


    }


}
