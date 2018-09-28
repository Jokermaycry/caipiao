package com.daotian.View;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.FlowBO;
import com.daotian.Model.ResultBO;
import com.daotian.R;
import com.daotian.Utils.ToastUtil;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class SteamActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refresh_listview)
    PullToRefreshListView refreshListview;

    private MyApdater myApdater;
    private int page = 1;
    private List<FlowBO> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam);
        ButterKnife.bind(this);
        initView();
        getList("");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        refreshListview.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();
                getList("");
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getList("more");
            }
        });
        myApdater= new MyApdater(SteamActivity.this, R.layout.list_item_steam, list);
        refreshListview.setAdapter(myApdater);
    }

    private void getList(final String s) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page",page);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getMemberFlow, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                refreshListview.onRefreshComplete();
                Log.e("result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(SteamActivity.this, resultBO.getResultMsg());
                    return;
                }
                List<FlowBO> result_list=JSON.parseArray(resultBO.getResultData(),FlowBO.class);
                if(result_list==null){
                    return;
                }
                if(s.equals("more") && result_list.size()<1){
                    ToastUtil.toast(SteamActivity.this,"没有更多啦");
                    return;
                }
                list.addAll(result_list);
                myApdater.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                refreshListview.onRefreshComplete();
                ToastUtil.toast(SteamActivity.this, "请求失败:" + arg1);
            }
        });
    }


    public class MyApdater extends CommonAdapter<FlowBO> {

        public MyApdater(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final FlowBO info) {

            TextView fee=holder.getView(R.id.fee);
            if(info.getFee_type().equals("1")){
                fee.setText("+"+info.getFee());
                fee.setTextColor(Color.parseColor("#FC3043"));
            }else{
                fee.setText("-"+info.getFee());
                fee.setTextColor(Color.parseColor("#66CDAA"));
            }
            holder.setText(R.id.name, info.getType_name());
            holder.setText(R.id.time, info.getCreate_time());
            holder.setText(R.id.lest, "余额："+info.getThe_balance());
        }
    }

}
