package com.mristudio.notification_fcm.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.adapter.TopicAdapter;
import com.mristudio.notification_fcm.model.Topic;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private TopicAdapter adapter;


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topic, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Topic> options =
                new FirebaseRecyclerOptions.Builder<Topic>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Topic"), Topic.class)
                        .build();


        adapter = new TopicAdapter(options);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


//      "data": {
//        "title": "forground Check this Mobile (title)",
//        "body": "forground Rich Notification testing (body)"
//        "click_action":"com.mristudio.notificationdemo.MyClickAction",
//        "image": "https://camo.githubusercontent.com/f8ea5eab7494f955e90f60abc1d13f2ce2c2e540/68747470733a2f2f662e636c6f75642e6769746875622e636f6d2f6173736574732f323037383234352f3235393331332f35653833313336322d386362612d313165322d383435332d6536626439353663383961342e706e67"
//      }

//     https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages


}
