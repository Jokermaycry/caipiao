package com.ailide.apartmentsabc.views.friendchat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.adapter.MessageAdapter;
import com.ailide.apartmentsabc.eventbus.DrawEvent;
import com.ailide.apartmentsabc.eventbus.JpushEvent;
import com.ailide.apartmentsabc.eventbus.SendEditEvent;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.BaseDateBean;
import com.ailide.apartmentsabc.model.ChatBean;
import com.ailide.apartmentsabc.model.FriendChatBean;
import com.ailide.apartmentsabc.model.PictureBean;
import com.ailide.apartmentsabc.model.Theme;
import com.ailide.apartmentsabc.model.Themes;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.DisplayUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.DrawView;
import com.ailide.apartmentsabc.utils.WordAndPicture;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.sticker.ChatStickerActivity;
import com.ailide.apartmentsabc.views.sticker.PrePrintActivity;
import com.ailide.apartmentsabc.views.sticker.ThemeAdapter;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_THEME;


public class FriendChatActivity extends BaseActivity {

    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.friend_recycle_view)
    RecyclerView friendRecycleView;
    @BindView(R.id.friend_chat_change_img)
    ImageView friendChatChangeImg;
    @BindView(R.id.friend_chat_send)
    TextView friendChatSend;
    @BindView(R.id.friend_chat_rl)
    RelativeLayout friendChatRl;
    @BindView(R.id.friend_chat_jianpan_ly)
    LinearLayout friendChatJianpanLy;
    @BindView(R.id.friend_chat_yuanbiji_ly)
    LinearLayout friendChatYuanbijiLy;
    @BindView(R.id.friend_chat_zhuti_ly)
    LinearLayout friendChatZhutiLy;
    @BindView(R.id.friend_chat_bianji_ly)
    LinearLayout friendChatBianjiLy;
    @BindView(R.id.friend_chat_all_ly)
    LinearLayout friendChatAllLy;
    @BindView(R.id.friend_recycle_view_two)
    RecyclerView friendRecycleViewTwo;
    @BindView(R.id.friend_chat_ly)
    RelativeLayout friendChatLy;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ly)
    LinearLayout ly;
    @BindView(R.id.huanhang)
    ImageView huanhang;
    @BindView(R.id.shanchu)
    ImageView shanchu;

    private DrawView drawView;
    private MessageAdapter recycleViewAdapter;
    private List<FriendChatBean> menuList = new ArrayList<>();
    private UserBean userBean;
    private int page = 0;
    private int fid;
    private LinearLayoutManager linearLayoutManager;
    public static int imgWith;
    private List<Theme> themes;
    private Bitmap photo;
    private ThemeAdapter adapter;
    private String hd;
    private Bitmap bitmap;
    private InputMethodManager imm;
    private int jianpanOrtuya=1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        setTouchHideInput(false);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        fid = getIntent().getIntExtra("fid", 0);
        tvIncludeTitle.setText(getIntent().getStringExtra("title"));
        photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.liaotian_biankuang);
        initRecycleView();
        getWH();
        hideJianPan();
        chatRecord();
        OkGo.<String>post(Urls.THEME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            themes.clear();
                            Themes data = JSON.parseObject(response.body(), Themes.class);
                            themes.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSoftInputShown()) {
                    hideJianPan();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                OkGo.<String>post(Urls.CHAT_RECORD)//
                        .tag(this)//
                        .params("mid", userBean.getData().getId())//
                        .params("fid", fid)
                        .params("page", page)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (isFinishing())
                                    return;
                                dismissLoading();
                                swipeRefreshLayout.setRefreshing(false);
                                Logger.e("body", response.body());
                                if (response.isSuccessful()) {
                                    BaseDateBean baseDateBean = JSON.parseObject(response.body(), BaseDateBean.class);
                                    if (baseDateBean.getStatus() == 1) {
                                        ChatBean chatBean = JSON.parseObject(response.body(), ChatBean.class);
                                        if(chatBean.getData().size()<=0){
                                            swipeRefreshLayout.setEnabled(false);
                                        }else {
                                            for (int i = 0; i < chatBean.getData().size(); i++) {
                                                menuList.add(0,chatBean.getData().get(i));
                                            }
                                            recycleViewAdapter.setNewData(menuList);
                                        }
                                    } else {
                                        toast(baseDateBean.getMsg());
                                    }
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getWH() {
        // 获取创建的宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        // 创建一个DrawView，该DrawView的宽度、高度与该Activity保持相同
        drawView = new DrawView(this, DisplayUtil.dip2px(this,250), DisplayUtil.dip2px(this,250));

        friendChatRl.addView(drawView);
        drawView.requestFocus();
    }

    private void chatRecord() {
        page=0;
        showLoading("加载中...");
        OkGo.<String>post(Urls.CHAT_RECORD)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("fid", fid)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("body", response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(), BaseDateBean.class);
                            if (baseDateBean.getStatus() == 1) {
                                ChatBean chatBean = JSON.parseObject(response.body(), ChatBean.class);
                                menuList.addAll(chatBean.getData());
                                recycleViewAdapter.setNewData(menuList);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayoutManager.scrollToPositionWithOffset(menuList.size() - 1, 0);
                                    }
                                },500);
                            } else {
                                toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }

    private void sendMessage(String content, String content_yu) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SEND_CHAT)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("fid", fid)
                .params("content", content)
                .params("content_yu", content_yu)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("body", response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(), BaseDateBean.class);
                            if (baseDateBean.getStatus() == 1) {
                                edit.setText("");
                                edit.setCursorVisible(true);
                                hd = "";
                                FriendChatBean friendChatBean = JSON.parseObject(baseDateBean.getData(), FriendChatBean.class);
                                menuList.add(friendChatBean);
                                recycleViewAdapter.setNewData(menuList);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayoutManager.scrollToPositionWithOffset(menuList.size() - 1, 0);
                                    }
                                },500);
                            } else {
                                toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }

    private void post(String path) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.UPLOAD)//
                .tag(this)//
                .params("image", new File(path))//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("body", response.body());
                        if (response.isSuccessful()) {
                            PictureBean pictureBean = JSON.parseObject(response.body(), PictureBean.class);
                            if (pictureBean.getStatus() == 1) {
                                if(TextUtils.isEmpty(hd)){
                                    hd=pictureBean.getData().getImg_url();
                                }
                                sendMessage(pictureBean.getData().getImg_url(), hd);
                            } else {
                                toast(pictureBean.getMsg());
                            }
                        }
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    // 缩放
    public Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        //获取图片的宽高
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        Logger.e("dddddd", originWidth + " &&" + originHeight);

        //这里缩放我们的尺寸，缩放多少自己去定义
        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        //进行缩放
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
                originHeight, matrix, true);
        return resizedBitmap;
    }

    @OnClick({R.id.iv_include_back, R.id.friend_chat_change_img, R.id.friend_chat_send, R.id.friend_chat_jianpan_ly, R.id.friend_chat_yuanbiji_ly, R.id.friend_chat_zhuti_ly, R.id.friend_chat_bianji_ly,
            R.id.huanhang, R.id.shanchu, R.id.ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.friend_chat_change_img:
                friendChatAllLy.setVisibility(View.VISIBLE);
                friendRecycleViewTwo.setVisibility(View.GONE);
                friendChatLy.setVisibility(View.GONE);
                hideJianPan();
                break;
            case R.id.friend_chat_send:
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    toast("输入内容不能为空");
                    return;
                }
                edit.setCursorVisible(false);
//                RelativeLayout relativeLayout = new RelativeLayout(this);
//                RelativeLayout.LayoutParams linearParams =  (RelativeLayout.LayoutParams)relativeLayout.getLayoutParams();
//                linearParams.height = WordAndPicture.loadBitmapFromView(edit).getHeight() +20;
//                linearParams.width = WordAndPicture.loadBitmapFromView(edit).getWidth()+20;
//                relativeLayout.setLayoutParams(linearParams);
//                relativeLayout.setGravity(RelativeLayout.CENTER_IN_PARENT);
//                relativeLayout.setBackgroundResource(R.drawable.bg_theme1);
//                ImageView imageView = new ImageView(this);
//                imageView.setImageBitmap(WordAndPicture.loadBitmapFromView(edit));
//                relativeLayout.addView(imageView);
//                post(WordAndPicture.saveMyBitmap("1",WordAndPicture.loadBitmapFromViewTwo(relativeLayout)));

//                LinearLayout temp=(LinearLayout)findViewById(R.id.LinearLayout的id);
//                Drawable d=Drawable.createFromPath(图片路径);
//                temp.setBackgroundDrawable(d);
//                photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_theme1);
                post(WordAndPicture.saveMyBitmap("1", WordAndPicture.getPicturePicture(resizeImage(photo, WordAndPicture.loadBitmapFromView(edit).getWidth(), WordAndPicture.loadBitmapFromView(edit).getHeight() + 20), WordAndPicture.loadBitmapFromView(edit))));
                break;
            case R.id.friend_chat_jianpan_ly:
                jianpanOrtuya=1;
                friendChatAllLy.setVisibility(View.GONE);
                friendRecycleViewTwo.setVisibility(View.GONE);
                friendChatLy.setVisibility(View.GONE);
                showJianPan();
                break;
            case R.id.friend_chat_yuanbiji_ly:
                jianpanOrtuya=2;
                friendChatAllLy.setVisibility(View.GONE);
                friendRecycleViewTwo.setVisibility(View.GONE);
                friendChatLy.setVisibility(View.VISIBLE);
                hideJianPan();
                break;
//            case R.id.friend_chat_zhuti_ly:
//                friendChatAllLy.setVisibility(View.GONE);
//                friendRecycleViewTwo.setVisibility(View.VISIBLE);
//                friendChatLy.setVisibility(View.GONE);
//                hideJianPan();
//                break;
            case R.id.friend_chat_bianji_ly:
                Intent intent = new Intent(this, ChatStickerActivity.class);
                jumpToOtherActivity(intent, false);
                break;
            case R.id.huanhang:
//                Editable editable = edit.getText();//获得文本内容
//                int index = edit.getSelectionStart();//获得光标所在位置
//                editable.insert(index, "\n");
                int keyCode1 = KeyEvent.KEYCODE_ENTER;
                KeyEvent keyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode1);
                edit.dispatchKeyEvent(keyEvent1);
                break;
            case R.id.shanchu:
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                edit.dispatchKeyEvent(keyEvent);
                break;
            case R.id.ly:
                edit.setSelection(edit.getText().toString().length());
                if(jianpanOrtuya==1){
                    friendChatAllLy.setVisibility(View.GONE);
                    friendRecycleViewTwo.setVisibility(View.GONE);
                    friendChatLy.setVisibility(View.GONE);
                    showJianPan();
                }else {
                    friendChatAllLy.setVisibility(View.GONE);
                    friendRecycleViewTwo.setVisibility(View.GONE);
                    friendChatLy.setVisibility(View.VISIBLE);
                    hideJianPan();
                }
                break;
        }
    }

    private void showJianPan() {
        if (imm != null) {
            edit.requestFocus();
            edit.post(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(edit, 0);
                }
            });
        }
    }

    private void hideJianPan() {
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        try {
//            Class<EditText> cls = EditText.class;
//            Method setSoftInputShownOnFocus;
//            setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
//            setSoftInputShownOnFocus.setAccessible(true);
//            setSoftInputShownOnFocus.invoke(edit, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }
//        if (softInputHeight > 0) {
//            sp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
//        }
        return softInputHeight;
    }

    private PopupWindow popupWindow;

    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, tvIncludeTitle, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        pop_equitment_add.setText("连 接 设 备");
        pop_equitment_not.setText("取 消 打 印");
        equipment_tv.setText("还没有连接到设备哦");
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(FriendChatActivity.this, MyEquitmentActivity.class), false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void initRecycleView() {
        linearLayoutManager = new LinearLayoutManager(this);
        friendRecycleView.setLayoutManager(linearLayoutManager);
        friendRecycleView.setNestedScrollingEnabled(false);
        friendRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new MessageAdapter(menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        friendRecycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (PrintFragment.printPP != null&&PrintFragment.printPP.isConnected()) {
                    if (!TextUtils.isEmpty(menuList.get(position).getContent_yu())) {
                        Intent intent = new Intent(FriendChatActivity.this, PrePrintActivity.class);
                        intent.putExtra("path", Urls.BASE_IMG + menuList.get(position).getContent_yu());
                        jumpToOtherActivity(intent, false);
                    } else {
                        Intent intent = new Intent(FriendChatActivity.this, PrePrintActivity.class);
                        intent.putExtra("path", Urls.BASE_IMG + menuList.get(position).getContent());
                        jumpToOtherActivity(intent, false);
                    }
                } else {
                    showPopView();
                }
            }
        });
        recycleViewAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_left_head_img:
                    case R.id.item_right_head_img:
                        Intent intent = new Intent(FriendChatActivity.this, FriendDetailActivity.class);
                        intent.putExtra("note_num", menuList.get(position).getNote_num());
                        jumpToOtherActivity(intent, false);
                        break;
                }
            }
        });

        themes = new ArrayList<>();
        adapter = new ThemeAdapter(themes);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        friendRecycleViewTwo.setLayoutManager(manager);
        friendRecycleViewTwo.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < themes.size(); i++) {
                    if (i == position) {
                        themes.get(position).setSelect(!themes.get(position).isSelect());
                        if (themes.get(position).isSelect()) {
                            if (themes.get(position) == null) {
                                toast("主题图片不存在");
                            } else {
                                final String path = PATH_STICKER_THEME + "/" + themes.get(position).getNineName();
                                if (new File(path).exists()) {
                                    photo = BitmapFactory.decodeFile(path);
                                    if (photo == null) {
                                        toast("主题图片不存在");
                                        return;
                                    }
                                    byte[] chunk = photo.getNinePatchChunk();
                                    if (NinePatch.isNinePatchChunk(chunk)) {
                                    } else {
                                        toast("主题不是.9图片");
                                        return;
                                    }
                                } else {
                                    OkGo.<File>post(Urls.BASE_IMG + themes.get(position).getAn_image())
                                            .tag(this)
                                            .execute(new FileCallback(PATH_STICKER_THEME, themes.get(position).getNineName()) {
                                                @Override
                                                public void onSuccess(Response<File> response) {
                                                    photo = BitmapFactory.decodeFile(path);
                                                    if (photo == null) {
                                                        toast("主题图片不存在");
                                                        return;
                                                    }
                                                    byte[] chunk = photo.getNinePatchChunk();
                                                    if (NinePatch.isNinePatchChunk(chunk)) {
                                                    } else {
                                                        toast("主题不是.9图片");
                                                        return;
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    } else {
                        themes.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DrawEvent event) {
        imgWith = drawView.getPaintBitmap().getWidth();
        Logger.e("imgWith", imgWith + "");
        SpannableString ss = new SpannableString("<img img>");
        ImageSpan imageSpan = new ImageSpan(this, drawView.getPaintBitmap(), ImageSpan.ALIGN_BOTTOM);
        ss.setSpan(imageSpan, 0, ("<img img>").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = edit.getText();//获得文本内容
        int index = edit.getSelectionStart();//获得光标所在位置
        editable.insert(index, ss);
        drawView.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SendEditEvent event) {
        WindowManager wm1 = this.getWindowManager();
        final int width1 = wm1.getDefaultDisplay().getWidth();
        final int height1 = wm1.getDefaultDisplay().getHeight();
//        bitmap = BitmapFactory.decodeFile(event.getImg());
        Glide.with(this)
                .load(event.getImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap1, GlideAnimation glideAnimation) {
                        bitmap = bitmap1;
                        if (bitmap.getWidth() > width1 * 2 / 3) {
                            bitmap = resizeImage(bitmap, width1 * 2 / 3, bitmap.getHeight() * (width1 * 2 / 3) / bitmap.getWidth());
                        }
                        if (bitmap.getHeight() > height1 / 2) {
                            bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), height1 / 2);
                        }
                        showLoading("加载中...");
                        OkGo.<String>post(Urls.UPLOAD)//
                                .tag(this)//
                                .params("image", new File(event.getImg()))//
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        if (isFinishing())
                                            return;
                                        dismissLoading();
                                        Logger.e("body", response.body());
                                        if (response.isSuccessful()) {
                                            PictureBean pictureBean = JSON.parseObject(response.body(), PictureBean.class);
                                            if (pictureBean.getStatus() == 1) {
                                                hd = pictureBean.getData().getImg_url();
                                                post(WordAndPicture.saveMyBitmap("1", bitmap));
                                            } else {
                                                toast(pictureBean.getMsg());
                                            }
                                        }
                                    }
                                });
                    }});

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JpushEvent event) {
        chatRecord();
    }

}
