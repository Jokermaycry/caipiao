package com.ailide.apartmentsabc.views.sticker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailide.apartmentsabc.model.StickerDraft;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScrapPaperActivity extends BaseActivity {

    @BindView(R.id.tv_edit)
    TextView mTvEdit;
    @BindView(R.id.tv_all)
    TextView mTvAll;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.rv_draft)
    RecyclerView mRvDradt;

    private ScrapPaperAdapter mAdapter;
    private List<StickerDraft> drafts;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_paper);
        ButterKnife.bind(this);
        initData();
        getData();
    }

    private void initData() {
        drafts = new ArrayList<>();
        mAdapter = new ScrapPaperAdapter(drafts);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mAdapter.isEdit()){
                    drafts.get(position).setSelect(!drafts.get(position).isSelect());
                    mAdapter.notifyDataSetChanged();
                }else {
                    EventBus.getDefault().post(new EventBusEntity(EventConstant.SHOW_DRAFT, drafts.get(position)));
                    finish();
                }
            }
        });
        mRvDradt.setLayoutManager(new LinearLayoutManager(this));
        mRvDradt.setAdapter(mAdapter);
    }

    public void getData() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                List<StickerDraft> data = (List<StickerDraft>) SPUtil.readObject(Contants.SP_STICKER_DRAFTS);
                if (data == null) {
                    data = new ArrayList<>();
                }
                drafts.clear();
                drafts.addAll(data);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @OnClick({R.id.iv_back,R.id.tv_edit, R.id.tv_all, R.id.tv_delete,R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit:
                mTvEdit.setVisibility(View.GONE);
                mTvAll.setVisibility(View.VISIBLE);
                mTvDelete.setVisibility(View.VISIBLE);
                mTvCancel.setVisibility(View.VISIBLE);
                mAdapter.setEdit(true);
                break;
            case R.id.tv_all:
                for (StickerDraft draft :
                        drafts) {
                    draft.setSelect(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_delete:
                mTvEdit.setVisibility(View.VISIBLE);
                mTvAll.setVisibility(View.GONE);
                mTvDelete.setVisibility(View.GONE);
                mTvCancel.setVisibility(View.GONE);
                for (int i = 0; i < drafts.size(); i++) {
                    if (drafts.get(i).isSelect()) {
                        drafts.remove(i);
                        i--;
                    }
                }
                mAdapter.setEdit(false);
                SPUtil.saveObject(Contants.SP_STICKER_DRAFTS, drafts);
                break;
            case R.id.tv_cancel:
                mTvEdit.setVisibility(View.VISIBLE);
                mTvAll.setVisibility(View.GONE);
                mTvDelete.setVisibility(View.GONE);
                mTvCancel.setVisibility(View.GONE);
                mAdapter.setEdit(false);
                break;
        }
    }
}
