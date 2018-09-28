package com.ailide.apartmentsabc.tools;

import android.os.Environment;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class Contants {
    public static String VERSION="v1";
    public static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static int SELECT_CITY = 1;
    public static String VERSION2="v2";
    public static int IMG_WAN=10;


    public final static String SP_STICKER_DRAFTS = "SP_STICKER_DRAFTS";
    public final static String SP_LABEL_DRAFTS = "SP_LABEL_DRAFTS";
    public final static String SP_BILL = "SP_BILL";
    public final static String PATH_STICKER = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker";
    public final static String PATH_LABEL = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/label";
    public final static String PATH_STICKER_DRAFT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/draft";
    public final static String PATH_LABEL_DRAFT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/label/draft";
    public final static String PATH_STICKER_EMOJI = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/emoji";
    public final static String PATH_STICKER_BUBBLE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/bubble";
    public final static String PATH_STICKER_THEME = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/theme";
    public final static String PATH_STICKER_THEME_SHOW = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/theme/show";
    public final static String PATH_STICKER_TTF_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/sticker/ttf/file";
    public final static String PATH_BILL = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ailide/bill";
}
