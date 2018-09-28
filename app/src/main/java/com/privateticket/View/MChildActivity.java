package com.privateticket.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daotian.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.App;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.ResultBO;
import com.privateticket.Model.UserBO;

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
 * 我的子账号
 * Created by yzx on 16/11/15.
 */

public class MChildActivity extends AppCompatActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.child_listview)
    ListView childListview;
    @BindView(R.id.back)
    ImageView back;
    private List<UserBO> list = new ArrayList<>();
    private Adapter mAdapter;
    private Activity mActivity;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychild);
        ButterKnife.bind(this);
        mActivity = this;
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        initView();
        getList();
    }

    private void getList() {
        dialog.show();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getChildList, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                dialog.dismiss();
                Log.e("init_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                List<UserBO> result_list = JSON.parseArray(resultBO.getResultData(), UserBO.class);
                list.clear();
                list.addAll(result_list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                dialog.dismiss();
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

    private void initView() {
        mAdapter = new Adapter(mActivity, R.layout.list_item_mychild, list);
        childListview.setAdapter(mAdapter);
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
    public void onClick() {
        finish();
    }


    /**
     * 列表适配器
     */
    public class Adapter extends CommonAdapter<UserBO> {

        public Adapter(Context context, int layoutId, List<UserBO> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final UserBO info) {
            holder.setText(R.id.name,info.getAccount());
            holder.setText(R.id.balance,"剩余彩豆："+info.getBalance());
            holder.setText(R.id.frz_balance,"冻结彩豆："+info.getFreeze_balance());

            TextView recharge = holder.getView(R.id.recharge_btn);
            recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRechargePop(info.getId(),info.getAccount());
                }
            });

        }
    }

    /**
     * 充值
     */
    private void rechargeMoney(String user_id, String price) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("toid", user_id);
        paramMap.put("balance", price);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.giveChildMoney, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                ToastUtil.toast(mActivity, resultBO.getResultMsg());
                if (resultBO.getResultId() != 0) {
                    return;
                }
                getList();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

    /**
     * 充值弹窗
     */
    public void showRechargePop(final String user_id,String acount) {
        // 一个自定义的布局，作为显示的内容
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_recharge, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView child_name = (TextView) view.findViewById(R.id.child_name);
        final EditText money_edit = (EditText) view.findViewById(R.id.price_edit);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView comfirm = (TextView) view.findViewById(R.id.comfirm);
        TextView mybalance = (TextView) view.findViewById(R.id.my_balance);
        child_name.setText("打钱到子账号:"+acount);
        mybalance.setText("(可用余额:"+ App.mUser.getBalance()+")");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(user_id)) {
                    ToastUtil.toast(mActivity, "找不到这个子账号，请与管理员联系!");
                    return;
                }
                if (TextUtils.isEmpty(money_edit.getText().toString())) {
                    ToastUtil.toast(mActivity, "请输入正确的金额");
                    return;
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }

                rechargeMoney(user_id, money_edit.getText().toString());
            }
        });
        popupWindow.setTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
