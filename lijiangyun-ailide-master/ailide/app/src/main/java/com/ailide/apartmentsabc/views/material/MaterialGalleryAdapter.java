package com.ailide.apartmentsabc.views.material;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.Urls;

import java.util.List;

public class MaterialGalleryAdapter extends FragmentPagerAdapter {

    private List<Material> data;

    public MaterialGalleryAdapter(FragmentManager fm, List<Material> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return MaterialPicFragment.create(Urls.BASE_IMG +data.get(position).getImage_url());
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
