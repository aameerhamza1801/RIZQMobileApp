package com.example.talha.rizq.Fragments_Tabs;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class adminTabPagesAdapter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[]{"EVENTS","SUPPORT CASES"};
    int tabCount = 2;

    public adminTabPagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                AdminEventsFragment tab1 = new AdminEventsFragment();
                return tab1;
            case 1:
                AdminCasesFragment tab2 = new AdminCasesFragment();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
