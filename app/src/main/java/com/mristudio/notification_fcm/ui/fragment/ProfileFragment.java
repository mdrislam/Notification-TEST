package com.mristudio.notification_fcm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.model.User;
import com.mristudio.notification_fcm.ui.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView email,name,topic;
    private Button logout;
    private CircleImageView image;
    private View view;
    private ProgressBar loader;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        image = view.findViewById(R.id.image);
        logout = view.findViewById(R.id.logout);
        loader = view.findViewById(R.id.progress_bar);
        topic = view.findViewById(R.id.topic);


        loader.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child("User").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        loader.setVisibility(View.GONE);
                        User user = dataSnapshot.getValue(User.class);

                        email.setText(user.getEmail());
                        name.setText(user.getName());
                        Picasso.get().load(user.getImage()).into(image);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loader.setVisibility(View.GONE);
                    }
                });


        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(FirebaseAuth.getInstance().getUid())
                .child("topic")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String s = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            s = s+snapshot.child("title").getValue().toString()+",";
                        }

                        topic.setText("Topic : "+s);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                HashMap<String,Object> map = new HashMap<>();
                map.put("token","");

                FirebaseDatabase.getInstance().getReference()
                        .child("User")
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                               FirebaseDatabase.getInstance().getReference()
                                       .child("User")
                                       .child(FirebaseAuth.getInstance().getUid())
                                       .child("topic")
                                       .addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                              if(dataSnapshot.exists()){
                                                  int i = 0;

                                                  for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                      FirebaseMessaging.getInstance().unsubscribeFromTopic(snapshot.child("title").getValue().toString());

                                                      i++;
                                                      if(i==dataSnapshot.getChildrenCount()){
                                                          loader.setVisibility(View.GONE);
                                                          FirebaseAuth.getInstance().signOut();
                                                          startActivity(new Intent(getContext(), LoginActivity.class));
                                                          getActivity().finish();
                                                      }

                                                  }
                                              }
                                              else {
                                                  loader.setVisibility(View.GONE);
                                                  FirebaseAuth.getInstance().signOut();
                                                  startActivity(new Intent(getContext(), LoginActivity.class));
                                                  getActivity().finish();
                                              }

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                           }
                                       });


                            }
                        });
            }
        });


        return view;
    }
}
