package com.mristudio.notification_fcm.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mristudio.notification_fcm.ui.fragment.CloudFunctionFragment;
import com.mristudio.notification_fcm.ui.fragment.RestApiFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RestApiFragment();
            case 1:
                return new CloudFunctionFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
               return  "RestApi";
            case 1:
                return "CloudFunction";
        }
        return null;
    }
}
