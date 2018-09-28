package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Bubble;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;

import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_BUBBLE;


public class BubbleAdapter extends BaseQuickAdapter<Bubble, BaseViewHolder> {

    public BubbleAdapter(@Nullable List<Bubble> data) {
        super(R.layout.item_bubble, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Bubble item) {
        if(TextUtils.isEmpty(item.getImage_url())){
            helper.setVisible(R.id.tv_bubble,true);
            helper.setVisible(R.id.iv_bubble,false);
        }else {
            helper.setVisible(R.id.tv_bubble,false);
            helper.setVisible(R.id.iv_bubble,true);
            File stickerDir = new File(PATH_STICKER_BUBBLE);
            if (!stickerDir.exists()) {
                stickerDir.mkdirs();
            }
          /*  String path = PATH_STICKER_BUBBLE + "/" + item.getId();
            if (new File(path).exists()) {
                ImageView imageView =helper.getView(R.id.iv_bubble);
                NetWorkImageLoader.loadNetworkImage(null,path,imageView);
            } else {
                OkGo.<File>post(Urls.BASE_IMG + item.getImage_url())
                        .tag(this)
                        .execute(new FileCallback(PATH_STICKER_BUBBLE, item.getId() + "") {
                            @Override
                            public void onSuccess(Response<File> response) {
                                ImageView imageView =helper.getView(R.id.iv_bubble);
                                NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),imageView);
                            }
                        });
            }*/
            NetWorkImageLoader.loadNetworkImage(null,Urls.BASE_IMG + item.getImage_url(), (ImageView) helper.getView(R.id.iv_bubble));

            OkGo.<File>post(Urls.BASE_IMG + item.getImage_url())
                    .tag(this)
                    .execute(new FileCallback(PATH_STICKER_BUBBLE, item.getId() + "") {
                        @Override
                        public void onSuccess(Response<File> response) {
//                            ImageView imageView =helper.getView(R.id.iv_bubble);
//                            NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),imageView);
                        }
                    });
        }
        if (item.isSelect()) {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme_p));
        } else {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme));
        }
    }
}
