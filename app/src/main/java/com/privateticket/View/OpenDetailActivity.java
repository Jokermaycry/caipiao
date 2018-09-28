package com.privateticket.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.OpenDetailBO;
import com.privateticket.Model.ResultBO;
import com.daotian.R;
import com.privateticket.Utils.TimeUtils;
import com.privateticket.Utils.ToastUtil;
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

public class OpenDetailActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refresh_listview)
    PullToRefreshListView refreshListview;
    @BindView(R.id.buy_btn)
    TextView buyBtn;

    private Activity mActivity;
    private int page = 1;
    private List<OpenDetailBO> list = new ArrayList<>();
    private MyApdater myApdater;
    private String sh_name;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opendetail);
        ButterKnife.bind(this);
        mActivity=this;
        sh_name=getIntent().getStringExtra("sh_name");
        name=getIntent().getStringExtra("name");
        title.setText(name+"开奖详情");
        initView();
        getList("");
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
        myApdater= new MyApdater(mActivity, R.layout.list_item_open_detial, list);
        refreshListview.setAdapter(myApdater);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getList(final String s) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sh_name",sh_name);
        paramMap.put("page",page);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getOpenListDetail, new AsyncHttpResponseHandler() {
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
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                List<OpenDetailBO> result_list=JSON.parseArray(resultBO.getResultData(),OpenDetailBO.class);
                if(result_list==null){
                    return;
                }
                if(s.equals("more") && result_list.size()<1){
                    ToastUtil.toast(mActivity,"没有更多啦");
                    return;
                }
                list.addAll(result_list);
                myApdater.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                refreshListview.onRefreshComplete();
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public class MyApdater extends CommonAdapter<OpenDetailBO> {

        public MyApdater(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final OpenDetailBO info) {

            holder.setText(R.id.log_num,"第"+info.getLog_num()+"期");
            holder.setText(R.id.open_time, TimeUtils.formatTimeMinute(Long.valueOf(info.getOpen_time())*1000));
            List<String> nums =JSON.parseArray(info.getOpen_num(),String.class);
            String num="";
            for(int i=0;i<nums.size();i++){
                num=num+" "+nums.get(i);
            }
            holder.setText(R.id.open_num,num);
        }
    }

    @OnClick({R.id.back, R.id.buy_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_btn:
                if(TextUtils.isEmpty(sh_name)){
                    ToastUtil.toast(mActivity,"彩票信息有误，稍后重试");
                    return;
                }
                Intent in=new Intent(mActivity,ElevenForFiveActivity.class);
                in.putExtra("sh_name",sh_name);
                startActivity(in);
                break;
        }
    }
}
