package com.example.talha.rizq.Fragments_Tabs;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class tabPagesAdapter extends FragmentStatePagerAdapter {

    String[] tabArray = new String[]{"HOME","EVENTS","SUPPORT CASES"};
    int tabCount = 3;

    public tabPagesAdapter(FragmentManager fm) {
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
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                EventsFragment tab2 = new EventsFragment();
                return tab2;
            case 2:
                CasesFragment tab3 = new CasesFragment();
                return tab3;
        }



        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
