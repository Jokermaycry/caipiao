package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Bubbles;
import com.ailide.apartmentsabc.model.Theme;
import com.ailide.apartmentsabc.model.Themes;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ThemeToolFragment extends BaseSimpleFragment {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.rv_theme)
    RecyclerView mRvTheme;

    private ThemeAdapter adapter;
    private List<Theme> themes;
    private Theme selectTheme;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        themes = new ArrayList<>();
        adapter = new ThemeAdapter(themes);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < themes.size(); i++) {
                    if (i == position) {
                        themes.get(position).setSelect(!themes.get(position).isSelect());
                        if (themes.get(position).isSelect()) {
                            selectTheme = themes.get(position);
                            EventBus.getDefault().post(new EventBusEntity(EventConstant.CHANGE_THEME, themes.get(position)));
                        } else {
                            selectTheme = null;
                            EventBus.getDefault().post(new EventBusEntity(EventConstant.CHANGE_THEME, null));
                        }
                    } else {
                        themes.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvTheme.setLayoutManager(manager);
        mRvTheme.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        OkGo.<String>post(Urls.THEME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            themes.clear();
                            Themes data = JSON.parseObject(response.body(), Themes.class);
                            themes.addAll(data.getData());
                            initSelectTheme();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void setListener() {
    }

    public void init() {
        mLlContent.setVisibility(View.VISIBLE);
    }

    public void setSelect(Theme theme){
        selectTheme = theme;
        initSelectTheme();
    }

    public void initSelectTheme(){
        if(themes != null && selectTheme != null){
            for (int i = 0; i < themes.size(); i++) {
                if (selectTheme.getId() == themes.get(i).getId()) {
                    themes.get(i).setSelect(true);
                } else {
                    themes.get(i).setSelect(false);
                }
            }
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
    }
}