package com.mristudio.notification_fcm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.model.Notification;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotificationAdapter extends FirebaseRecyclerAdapter<Notification, NotificationAdapter.NotificationViewHolder> {


    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
            holder.title.setText(model.getTitle());
            holder.description.setText(model.getDescription());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView title,description;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}
