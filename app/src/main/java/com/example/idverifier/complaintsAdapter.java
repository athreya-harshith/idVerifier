package com.example.idverifier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class complaintsAdapter  extends FirebaseRecyclerAdapter<complaints,complaintsAdapter.myViewHolder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public complaintsAdapter(@NonNull FirebaseRecyclerOptions<complaints> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaints_recycler_view,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull complaints model) {
        holder.compaintTitle.setText(model.getComplaintTitle());
        holder.complainedBy.setText(model.getComplainedBy());
        holder.compaintText.setText(model.getComplaintText());

    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView compaintTitle, compaintText,complainedBy;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            compaintTitle = itemView.findViewById(R.id.complaintTitle);
            complainedBy = itemView.findViewById(R.id.complainedBy);
            compaintText = itemView.findViewById(R.id.complaintText);

        }
    }

}
