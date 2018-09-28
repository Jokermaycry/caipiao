package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Emoji;
import com.ailide.apartmentsabc.model.EmojiGroup;
import com.ailide.apartmentsabc.model.EmojiGroups;
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

public class EmojiToolFragment extends BaseSimpleFragment {

    @BindView(R.id.rv_emoji_title)
    RecyclerView mRvEmojiTitle;
    @BindView(R.id.rv_emoji)
    RecyclerView mRvEmoji;

    private EmojiAdapter mEmojiAdapter;
    private EmojiTitleAdapter mTitleAdapter;
    private List<Emoji> emojis;
    private List<EmojiGroup> groups;
    private int nowGroup = -1;
    private int selectGroup = -1;
    private int selectPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emoji_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        groups = new ArrayList<>();
        mTitleAdapter = new EmojiTitleAdapter(groups);
        LinearLayoutManager emojiTitleManager = new LinearLayoutManager(mActivity);
        emojiTitleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvEmojiTitle.setLayoutManager(emojiTitleManager);
        mRvEmojiTitle.setAdapter(mTitleAdapter);
        mTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < groups.size(); i++) {
                    if (i == position) {
                        groups.get(position).setSelect(true);
                        nowGroup = i;
                        emojis.clear();
                        emojis.addAll(groups.get(i).getData());
                        mEmojiAdapter.notifyDataSetChanged();
                    } else {
                        groups.get(i).setSelect(false);
                    }
                }
                mTitleAdapter.notifyDataSetChanged();
            }
        });
        emojis = new ArrayList<>();
        mEmojiAdapter = new EmojiAdapter(emojis);
        mEmojiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (selectGroup !=nowGroup && selectPosition != -1) {
                    groups.get(selectGroup).getData().get(selectPosition).setSelect(false);
                }
                for (int i = 0; i < emojis.size(); i++) {
                    if (i == position) {
                        emojis.get(position).setSelect(!emojis.get(position).isSelect());
                        if (emojis.get(position).isSelect()) {
                            selectPosition = position;
                            selectGroup = nowGroup;
                        } else {
                            selectGroup = -1;
                            selectPosition = -1;
                        }
                    } else {
                        emojis.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager emojiManager = new LinearLayoutManager(mActivity);
        emojiManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvEmoji.setLayoutManager(emojiManager);
        mRvEmoji.setAdapter(mEmojiAdapter);
    }

    @Override
    protected void initData() {
        OkGo.<String>post(Urls.EMOTICON)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            EmojiGroups emojiGroups = JSON.parseObject(response.body(), EmojiGroups.class);
                            groups.clear();
                            groups.addAll(emojiGroups.getData());
                            if (groups.size() > 0) {
                                nowGroup = 0;
                                groups.get(0).setSelect(true);
                                mTitleAdapter.notifyDataSetChanged();
                                emojis.clear();
                                emojis.addAll(groups.get(0).getData());
                                mEmojiAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    protected void setListener() {
    }

    public void init() {
        if (groups.size() > 0) {
            if (nowGroup != -1) {
                groups.get(nowGroup).setSelect(false);
                groups.get(0).setSelect(true);
            }
            if (selectPosition != -1) {
                groups.get(selectGroup).getData().get(selectPosition).setSelect(false);
            }
            selectGroup = -1;
            selectPosition = -1;
            nowGroup = 0;
            mTitleAdapter.notifyDataSetChanged();
            emojis.clear();

            emojis.addAll(groups.get(0).getData());
            mEmojiAdapter.notifyDataSetChanged();
        }
    }

    public void onClickOk() {
        EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_EMOJI, ""));
    }

    @OnClick({R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (selectPosition == -1) {
                    EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_EMOJI, ""));
                } else {
                    emojis.get(selectPosition).setSelect(false);
                    mEmojiAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_EMOJI, groups.get(selectGroup).getData().get(selectPosition).getId() + ""));
                }
                break;
        }
    }
}