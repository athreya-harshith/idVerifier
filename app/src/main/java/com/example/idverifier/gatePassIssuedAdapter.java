package com.example.idverifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

//this is used as adapter for showing issued gate pass
public class gatePassIssuedAdapter extends FirebaseRecyclerAdapter<gatePass,gatePassIssuedAdapter.myViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    FragmentActivity activity;

    public gatePassIssuedAdapter(@NonNull FirebaseRecyclerOptions<gatePass> options, Context context, FragmentActivity activity) {
        super(options);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull gatePass model) {
        holder.name.setText(model.getName());
        holder.destination.setText(model.getToDestination());
        holder.reason.setText(model.getReason());
        holder.travellDate.setText(model.getTravelDate());
        holder.issuedDate.setText(model.getIssuedTime());//here its issued date and time also
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_gatepass_recycler_view,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView gpassQrImg;
        TextView name,destination,travellDate,reason,issuedDate;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            gpassQrImg = itemView.findViewById(R.id.issuedGpassQr);
            name = itemView.findViewById(R.id.issuedGpassName);
            destination = itemView.findViewById(R.id.issuedGpassDestination);
            travellDate = itemView.findViewById(R.id.issuedGpassDateOfTravelling);
            reason = itemView.findViewById(R.id.issuedGpassReason);
            issuedDate = itemView.findViewById(R.id.issuedGpassIssuedTime);
        }
    }
}
