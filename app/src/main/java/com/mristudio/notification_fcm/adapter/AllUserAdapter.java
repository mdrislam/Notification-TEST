package com.mristudio.notification_fcm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.model.User;
import com.mristudio.notification_fcm.ui.activity.SendNotificationActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AllUserAdapter extends FirebaseRecyclerAdapter<User, AllUserAdapter.AllUserViewHolder> {


    public AllUserAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final AllUserViewHolder holder, final int position, @NonNull User model) {
          holder.email.setText(model.getEmail());
          holder.name.setText(model.getName());
          Picasso.get().load(model.getImage()).into(holder.image);

          holder.main.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 String id = getRef(position).getKey();
                 Intent intent = new Intent(holder.main.getContext(),SendNotificationActivity.class);
                 intent.putExtra("id",id);
                  intent.putExtra("type","user");
                  holder.main.getContext().startActivity(intent);
              }
          });
    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false);
        return new AllUserViewHolder(view);
    }

    class AllUserViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,email;
        LinearLayout main;

        public AllUserViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            main = itemView.findViewById(R.id.main);
        }
    }
}
