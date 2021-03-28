package com.mristudio.notification_fcm.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mristudio.notification_fcm.R;
import com.mristudio.notification_fcm.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabLayout tabLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_notification, container, false);
        // Inflate the layout for this fragment

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabLayout);

        viewPager.setAdapter(new TabAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
}
