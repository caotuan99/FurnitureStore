package com.example.furniturestore.Module;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.furniturestore.Fragment.Fragment_Addmin;
import com.example.furniturestore.Fragment.Fragment_TuVan;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_TuVan();
            case 1:
                return new Fragment_Addmin();
            default: return  new Fragment_TuVan();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title="Tư vấn";
                break;
            case 1:
                title="Người quản lí";
                break;
        }
        return title;
    }
}
