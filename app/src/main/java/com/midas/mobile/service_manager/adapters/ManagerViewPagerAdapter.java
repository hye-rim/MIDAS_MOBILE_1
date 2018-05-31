package com.midas.mobile.service_manager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.midas.mobile.service_manager.fragments.ManagerBaseFragment;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class ManagerViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = ManagerViewPagerAdapter.class.getSimpleName();

    private int mTabCount = 4;
    private String mTabTitle[] = new String[]{"유저", "예약", "메뉴", "통계"};

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public ManagerViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.mTabCount = tabCount;


    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return ManagerBaseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

