package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class HtmlBean {

    /**
     * status : 1
     * data : {"id":1,"kefu":"3","official_link":"23","wechat":"23","weibo":"23","about_us":"<p>23<\/p>","instructions":"<p>23<\/p>","app":{"id":1,"edition":"10.01","remark":"测试1","appfor":1,"file":"admin/20180102/1bfc70e01a428fea2794aca039f08114.apk","file_name":"普京国际.apk","create_time":1514879319},"ios":{"id":2,"edition":"10.03","remark":"苹果1","appfor":2,"file":"admin/20180102/ef6a2e787c5b0bdf79d74c85bf0ec707.ipa","file_name":"普京国际.ipa","create_time":1514879365}}
     * msg : 成功
     */

    private int status;
    private DataBean data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * id : 1
         * kefu : 3
         * official_link : 23
         * wechat : 23
         * weibo : 23
         * about_us : <p>23</p>
         * instructions : <p>23</p>
         * app : {"id":1,"edition":"10.01","remark":"测试1","appfor":1,"file":"admin/20180102/1bfc70e01a428fea2794aca039f08114.apk","file_name":"普京国际.apk","create_time":1514879319}
         * ios : {"id":2,"edition":"10.03","remark":"苹果1","appfor":2,"file":"admin/20180102/ef6a2e787c5b0bdf79d74c85bf0ec707.ipa","file_name":"普京国际.ipa","create_time":1514879365}
         */

        private int id;
        private String kefu;
        private String official_link;
        private String wechat;
        private String weibo;
        private String about_us;
        private String instructions;
        private AppBean app;
        private IosBean ios;
        private String mall;

        public String getMall() {
            return mall;
        }

        public void setMall(String mall) {
            this.mall = mall;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKefu() {
            return kefu;
        }

        public void setKefu(String kefu) {
            this.kefu = kefu;
        }

        public String getOfficial_link() {
            return official_link;
        }

        public void setOfficial_link(String official_link) {
            this.official_link = official_link;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }

        public String getAbout_us() {
            return about_us;
        }

        public void setAbout_us(String about_us) {
            this.about_us = about_us;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public AppBean getApp() {
            return app;
        }

        public void setApp(AppBean app) {
            this.app = app;
        }

        public IosBean getIos() {
            return ios;
        }

        public void setIos(IosBean ios) {
            this.ios = ios;
        }

        public static class AppBean {
            /**
             * id : 1
             * edition : 10.01
             * remark : 测试1
             * appfor : 1
             * file : admin/20180102/1bfc70e01a428fea2794aca039f08114.apk
             * file_name : 普京国际.apk
             * create_time : 1514879319
             */

            private int id;
            private String edition;
            private String remark;
            private int appfor;
            private String file;
            private String file_name;
            private int create_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEdition() {
                return edition;
            }

            public void setEdition(String edition) {
                this.edition = edition;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getAppfor() {
                return appfor;
            }

            public void setAppfor(int appfor) {
                this.appfor = appfor;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getFile_name() {
                return file_name;
            }

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }
        }

        public static class IosBean {
            /**
             * id : 2
             * edition : 10.03
             * remark : 苹果1
             * appfor : 2
             * file : admin/20180102/ef6a2e787c5b0bdf79d74c85bf0ec707.ipa
             * file_name : 普京国际.ipa
             * create_time : 1514879365
             */

            private int id;
            private String edition;
            private String remark;
            private int appfor;
            private String file;
            private String file_name;
            private int create_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEdition() {
                return edition;
            }

            public void setEdition(String edition) {
                this.edition = edition;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getAppfor() {
                return appfor;
            }

            public void setAppfor(int appfor) {
                this.appfor = appfor;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getFile_name() {
                return file_name;
            }

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }
        }
    }
}
