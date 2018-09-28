package com.ailide.apartmentsabc.tools;

import android.os.Environment;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.ailide.apartmentsabc.model.JsonBean;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by daguye on 16/6/24.
 */

public class AreaUtil {

    public static String readSDFile(String fileName){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
//        String SDPATH = Environment.getExternalStorageDirectory().getPath();
        String res=null;
        File file = new File(Contants.FILE_PATH + "/"+fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            /* 准备一个字节数组用户装即将读取的数据 */
            byte[] buffer = new byte[fis.available()];

            /* 开始进行文件的读取 */
            fis.read(buffer);
            fis.close();
            /* 将字节数组转换成字符创， 并转换编码的格式 */
            res = EncodingUtils.getString(buffer, "UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    //解析数据省市区
    public static void initJsonData(String JsonData, ArrayList<JsonBean> options1Items, ArrayList<ArrayList<String>> options2Items, ArrayList<ArrayList<ArrayList<String>>> options3Items){
        if(TextUtils.isEmpty(JsonData)){
            return;
        }
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items.addAll(jsonBean) ;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getChildRen().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getChildRen().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getChildRen().get(c).getChildRen() == null
                        ||jsonBean.get(i).getChildRen().get(c).getChildRen().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getChildRen().get(c).getChildRen().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getChildRen().get(c).getChildRen().get(d).getName();

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public static ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}
