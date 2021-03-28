package com.mristudio.notification_fcm.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.adapter.NotificationAdapter;
import com.mristudio.notification_fcm.model.Notification;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestApiFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    public RestApiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rest_api, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notification")
                                .child(FirebaseAuth.getInstance().getUid()).child("RestApi"), Notification.class)
                        .build();

        adapter = new NotificationAdapter(options);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
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
}
