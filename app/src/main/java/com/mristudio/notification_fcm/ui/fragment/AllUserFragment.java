package com.mristudio.notification_fcm.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.adapter.AllUserAdapter;
import com.mristudio.notification_fcm.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserFragment extends Fragment {

    View view;
    private RecyclerView alluser;
    private AllUserAdapter adapter;

    public AllUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_all_user, container, false);
        // Inflate the layout for this fragment

        alluser = view.findViewById(R.id.all_user);
        alluser.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), User.class)
                        .build();

        adapter = new AllUserAdapter(options);

        alluser.setAdapter(adapter);

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
