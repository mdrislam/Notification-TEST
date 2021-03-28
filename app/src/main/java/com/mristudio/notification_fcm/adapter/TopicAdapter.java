package com.mristudio.notification_fcm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.model.Topic;
import com.mristudio.notification_fcm.ui.activity.SendNotificationActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class TopicAdapter extends FirebaseRecyclerAdapter<Topic, TopicAdapter.TopicViewHolder> {


    public TopicAdapter(@NonNull FirebaseRecyclerOptions<Topic> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TopicViewHolder holder, int position, @NonNull Topic model) {

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.main.getContext(), SendNotificationActivity.class);
                intent.putExtra("id",model.getTitle());
                intent.putExtra("type","topic");
                holder.main.getContext().startActivity(intent);
            }
        });


        holder.topic.setText(model.getTitle());
        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((String)holder.subscribe.getTag()).equals("unsbscribe")){
                    subscribe(holder.subscribe,model.getTitle());
                }
                else {
                    unSubscribe(holder.subscribe,model.getTitle());
                }
            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .child("topic")
                .orderByChild("title")
                .equalTo(model.getTitle())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0){
                            holder.subscribe.setTag(dataSnapshot.getChildren().iterator().next().getKey());
                            holder.subscribe.setColorFilter(ContextCompat.getColor(holder.subscribe.getContext(),R.color.colorPrimary));
                        }
                        else {
                            holder.subscribe.setTag("unsbscribe");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void unSubscribe(ImageView subscribe, String title) {
       FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .child("topic")
                .child((String)subscribe.getTag())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        subscribe.setTag("unsbscribe");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(title);
                        subscribe.setColorFilter(ContextCompat.getColor(subscribe.getContext(),R.color.gray));
                    }
                });
    }

    private void subscribe(ImageView subscribe, String title) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .child("topic")
                .push();

        HashMap<String,Object> map = new HashMap<>();
        map.put("title",title);

        reference.setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        subscribe.setColorFilter(ContextCompat.getColor(subscribe.getContext(),R.color.colorPrimary));
                        FirebaseMessaging.getInstance().subscribeToTopic(title);
                        subscribe.setTag(reference.getKey());
                    }
                });



    }


    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic, parent, false);
        return new TopicViewHolder(view);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder{

        TextView topic;
        ImageView subscribe;
        LinearLayout main;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            subscribe = itemView.findViewById(R.id.subscribe);
            topic = itemView.findViewById(R.id.topic);
            main = itemView.findViewById(R.id.main);
        }
    }
}
