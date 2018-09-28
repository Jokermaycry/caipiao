package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Bubble;
import com.ailide.apartmentsabc.model.Bubbles;
import com.ailide.apartmentsabc.model.Ttf;
import com.ailide.apartmentsabc.model.Ttfs;
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

public class TemplateToolFragment extends BaseSimpleFragment {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.rv_bubble)
    RecyclerView mRvBubble;

    private List<Bubble> bubbles;
    private BubbleAdapter bubbleAdapter;
    private int currentPage = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_template_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        bubbles = new ArrayList<>();
        bubbles.add(new Bubble());
        bubbleAdapter = new BubbleAdapter(bubbles);
        bubbleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_BUBBLE, bubbles.get(position)));
            }
        });
        bubbleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getBubble(currentPage + 1);
            }
        }, mRvBubble);
        mRvBubble.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mRvBubble.setAdapter(bubbleAdapter);
    }

    @Override
    protected void initData() {
        getBubble(0);
    }

    public void getBubble(final int page) {
        OkGo.<String>post(Urls.TEXT)
                .tag(this)
                .params("page", page + "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            currentPage = page;
                            if (page == 0) {
                                bubbles.clear();
                                bubbles.add(new Bubble());
                            }
                            Bubbles data = JSON.parseObject(response.body(), Bubbles.class);
                            bubbles.addAll(data.getData());
                            if (page == 0) {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvBubble.getLayoutParams();
                                if (bubbles.size() > 4) {
                                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, mActivity.getResources().getDisplayMetrics());
                                } else {
                                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, mActivity.getResources().getDisplayMetrics());
                                }
                                mRvBubble.setLayoutParams(params);
                            }
                            bubbleAdapter.notifyDataSetChanged();
                            if (data.getData().size() < 8) {
                                bubbleAdapter.loadMoreEnd();
                                bubbleAdapter.loadMoreEnd(true);
                            } else {
                                bubbleAdapter.loadMoreComplete();
                            }
                        } else {
                            bubbleAdapter.loadMoreEnd();
                            bubbleAdapter.loadMoreEnd(true);
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
}