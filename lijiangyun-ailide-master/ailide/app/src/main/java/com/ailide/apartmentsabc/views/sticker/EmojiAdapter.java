package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Emoji;
import com.ailide.apartmentsabc.tools.Contants;
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

import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_EMOJI;

public class EmojiAdapter extends BaseQuickAdapter<Emoji, BaseViewHolder> {

    public EmojiAdapter(@Nullable List<Emoji> data) {
        super(R.layout.item_emoji, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Emoji item) {
        File stickerDir = new File(Contants.PATH_STICKER_EMOJI);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
      /*  String path = PATH_STICKER_EMOJI + "/" + item.getId();
        if (new File(path).exists()) {
            NetWorkImageLoader.loadNetworkImage(null,path,(ImageView) helper.getView(R.id.iv_emoji));
        } else {
            OkGo.<File>post(Urls.BASE_IMG + item.getImage())
                    .tag(this)
                    .execute(new FileCallback(PATH_STICKER_EMOJI, item.getId() + "") {
                        @Override
                        public void onSuccess(Response<File> response) {
                            NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),(ImageView) helper.getView(R.id.iv_emoji));
                        }
                    });
        }*/
        OkGo.<File>post(Urls.BASE_IMG + item.getImage())
                .tag(this)
                .execute(new FileCallback(PATH_STICKER_EMOJI, item.getId() + "") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        NetWorkImageLoader.loadNetworkImage(null,response.body().getAbsolutePath(),(ImageView) helper.getView(R.id.iv_emoji));
                    }
                });
        if (item.isSelect()) {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme_p));
        } else {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_theme));
        }
    }
}