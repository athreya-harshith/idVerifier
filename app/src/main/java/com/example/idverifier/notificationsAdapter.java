package com.example.idverifier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class notificationsAdapter extends FirebaseRecyclerAdapter<notification,notificationsAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public notificationsAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_recycler_view,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull notification model) {
        holder.notificationTitle.setText(model.getNotificationTitle());
        holder.notificationText.setText(model.getNotificationMessage());
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView notificationTitle, notificationText;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationTitle = itemView.findViewById(R.id.notificationsTitle);
            notificationText = itemView.findViewById(R.id.notificationsText);

        }
    }
}
