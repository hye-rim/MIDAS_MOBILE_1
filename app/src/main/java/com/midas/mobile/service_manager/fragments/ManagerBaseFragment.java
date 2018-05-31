package com.midas.mobile.service_manager.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by hyerim on 2018. 5. 26....
 */

public class ManagerBaseFragment extends Fragment {

    public static ManagerBaseFragment newInstance(int page) {
        ManagerBaseFragment fragment = null;
        switch (page) {
            case 0:
                fragment = new UserManagermentFragment();
                break;
            case 1:
                fragment = new ReservationFragment();
                break;
            case 2:
                fragment = new MenuManagementFragment();
                break;
            case 3:
                fragment = new ChartFragment();
                break;
        }
        return fragment;
    }

}
