package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Theme;
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

import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_THEME_SHOW;

public class ThemeAdapter extends BaseQuickAdapter<Theme, BaseViewHolder> {

    public ThemeAdapter(@Nullable List<Theme> data) {
        super(R.layout.item_theme, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Theme item) {
        File stickerDir = new File(PATH_STICKER_THEME_SHOW);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
       /* String path = PATH_STICKER_THEME_SHOW + "/" + item.getName();
        if (new File(path).exists()) {
            NetWorkImageLoader.loadNetworkImage(null,path,(ImageView) helper.getView(R.id.iv_theme));
        } else {
            OkGo.<File>post(Urls.BASE_IMG + item.getImage_url())
                    .tag(this)
                    .execute(new FileCallback(PATH_STICKER_THEME_SHOW, item.getName()) {
                        @Override
                        public void onSuccess(Response<File> response) {
                            NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),(ImageView) helper.getView(R.id.iv_theme));
                        }
                    });
        }*/
        NetWorkImageLoader.loadNetworkImage(null,Urls.BASE_IMG + item.getImage_url(),(ImageView) helper.getView(R.id.iv_theme));

        OkGo.<File>post(Urls.BASE_IMG + item.getImage_url())
                .tag(this)
                .execute(new FileCallback(PATH_STICKER_THEME_SHOW, item.getName()) {
                    @Override
                    public void onSuccess(Response<File> response) {
//                        NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),(ImageView) helper.getView(R.id.iv_theme));
                    }
                });
        if(item.isSelect()){
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme_p));
        }else {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme));
        }
    }
}
