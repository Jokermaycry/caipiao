package com.ailide.apartmentsabc.tools;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class Urls {
    public static String BASE = "http://admin.ileadtek.com/public/"; //数据接口拼接
    public static String BASE_IMG = "http://admin.ileadtek.com/public/upload/"; //上传图片拼接
    public static String LOGIN= BASE + "index.php/api/login/index"; // 登录
    public static String SETTING = BASE + "index.php/api/Set/index"; //设置
    public static String SETUSERINFO = BASE + "index.php/api/login/edit_user"; // 个人中心编辑
    public static String UPLOAD = BASE + "index.php/api/login/upload"; // 图片上传
    public static String COMMON_PROBEMLE = BASE + "index.php/api/Set/common_pro";//常见问题
    public static String FRIEND_LIST = BASE + "index.php/api/Friend/friend_list";//好友列表
    public static String DELETE_FRIEND = BASE + "index.php/api/Friend/friend_delete";//拒绝或者删除好友
    public static String ADD_RRIEND = BASE + "index.php/api/Friend/friend_add";//添加好友
    public static String ADOPT_FRIEND = BASE + "index.php/api/Friend/friend_adopt";//好友通过
    public static String CHECK_FRIEND = BASE + "index.php/api/Friend/friend_find"; //查找好友
    public static String TEN_REIEND = BASE + "index.php/api/Friend/friend_ten";//好友十连抽
    public static String CLEAN_CHAT = BASE + "index.php/api/Friend/clean_chat";//清空好友聊天记录
    public static String CHAT_RECORD = BASE + "index.php/api/Friend/friend_chat";//好友聊天记录
    public static String SEND_CHAT = BASE + "index.php/api/Friend/send_chat";//发送聊天记录
    public static String SEARCH_MY_FRIEND = BASE + "index.php/api/Friend/friend_self"; //查找自己现有的好友
    public static String WHISPERS_LIST = BASE + "index.php/api/Whisper/whispers"; //悄悄话列表
    public static String WHISPERS_NEW = BASE + "index.php/api/Whisper/index"; //悄悄话（新建时页面）
    public static String WHISPERS_NEW_ADD = BASE + "index.php/api/Whisper/whisper_add"; //新建悄悄话(点击分享时调用)
    public static String REPLY_WHISPER = BASE + "index.php/api/Whisper/whiser_relation"; //发出悄悄话之后的回复列表
    public static String DELETE_WHISPER = BASE + "index.php/api/Whisper/whisper_delone"; //单个删除悄悄话
    public static String LOADING_PICTURE = BASE + "index.php/api/Set/qidong"; //启动页图片
    public static String ADDORREMARK_NAME = BASE + "index.php/api/Friend/remark_name"; //添加或者修改备注名

    public static String MATERIALCOM_INDEX= BASE + "index.php/api/Materialcom/index";//共享素材
    public static String MATERIALCOM_MCOLLECT = BASE + "index.php/api/Materialcom/mcollect";//共享素材--单个收藏取消
    public static String EMOTICON = BASE + "index.php/api/Robot/emoticon";//表情包所有
    public static String TEXT = BASE + "index.php/api/Robot/text";//文本框
    public static String TTF = BASE + "index.php/api/Robot/ttf";//字体素材
    public static String THEME = BASE + "index.php/api/Robot/theme";//主题素材
    public static String MATERIA_CLASS = BASE + "index.php/api/Materialcom/gclass";//共享素材，获取所有类型
    public static String MATERIA_TAG = BASE + "index.php/api/Materialcom/matag";//共享素材--获取分组素材信息(分页) 和 搜索素材
    public static String MATERIA_TAG_MORE = BASE + "index.php/api/Materialcom/tagmore";//共享素材--共享素材--更多（分组详情）
    public static String MATERIA_TAG_COLLECT = BASE + "index.php/api/Materialcom/tagcollect";//共享素材--收藏（整组收藏）
    public static String MATERIA_TAG_DCOLLECT = BASE + "index.php/api/Materialcom/tagdcollect";//共享素材--取消收藏（整组收藏）
    public static String MATERIA_COLLECT_LIST = BASE + "index.php/api/Materialcom/listcollect";//共享素材--获取分组素材信息(分页) 和 搜索素材
    public static String MATERIA_COLLECT = BASE + "index.php/api/Materialcom/collect";//共享素材--单个收藏
    public static String MATERIA_MCOLLECT = BASE + "index.php/api/Materialcom/mcollect";//共享素材--单个收藏取消
    public static String MATERIA_PRINT = BASE + "index.php/api/Materialcom/print_it";//素材打印调用，增加打印次数
    public static String WEB = BASE + "index.php/api/web/index";//网页打印初始化数据
    public static String WEB_TAG_ADD = BASE + "index.php/api/Web/tag_add";//网页打印--添加分组
    public static String WEB_TAG_DELETE = BASE + "index.php/api/Web/tag_delete";//网页打印--分组删除
    public static String WEB_ADD = BASE + "index.php/api/Web/web_add";//网页打印--添加网页
    public static String WEB_DELETE = BASE + "index.php/api/Web/web_delete";//网页打印--删除网页
    public static String STICKY = BASE + "index.php/api/Robot/sticky";//机器素材--便利清单


}
