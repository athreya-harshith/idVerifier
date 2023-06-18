package com.example.idverifier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class checkOutAdapter extends FirebaseRecyclerAdapter<checkOut,checkOutAdapter.myViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public checkOutAdapter(@NonNull FirebaseRecyclerOptions<checkOut> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull checkOut model)
    {
        holder.sname.setText(model.getName());
        holder.mode.setText(model.getMode());
        holder.verifierName.setText(model.getOutVerifier());
        holder.outTime.setText(model.getOutTime()+"          "+model.getOutDate());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_out_recyler_view,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView sname,verifierName,outTime,mode;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            sname = itemView.findViewById(R.id.checkoutrvname);
            verifierName = itemView.findViewById(R.id.checkoutoutverifier);
            outTime = itemView.findViewById(R.id.checkoutrvtimedate);
            mode = itemView.findViewById(R.id.checkoutrvmode);
        }
    }
}
