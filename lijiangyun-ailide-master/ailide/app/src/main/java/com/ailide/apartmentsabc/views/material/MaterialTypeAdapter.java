package com.ailide.apartmentsabc.views.material;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.Urls;

import java.util.List;

public class MaterialTypeAdapter extends FragmentPagerAdapter {

    private List<Fragment> data;

    public MaterialTypeAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
