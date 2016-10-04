package com.example.achuan.newsimooc.model.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.achuan.newsimooc.model.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by achuan on 16-10-3.
 * 功能：网络请求及相关数据处理的方法
 */
public class HttpUtil {

    /***3-使用HttpURLConnection通过url链接获取网络图片***/
    public static Bitmap getBitmapFromURL(String urlString)
    {
        Bitmap bitmap=null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;//输入流的引用变量
        try {
            URL url=new URL(urlString);
            connection= (HttpURLConnection) url.openConnection();//打开网络连接
            //将简单的字节流包装成缓存流
            inputStream=new BufferedInputStream(connection.getInputStream());
            /*将输入流解析成bitmp类型的数据*/
            bitmap= BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭网络连接
            if(connection!=null) {
                connection.disconnect();
            }
            //最后记得关闭输入流
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /***2-对传入的链接进行流读取,并JSON解析后获取最终数据***/
    public static List<NewsBean> getJsonData(String url)  {
        List<NewsBean> newsBeanList;//声明一个引用变量
        InputStreamReader isr;//字符流
        InputStream is;//字节流
        StringBuilder response=new StringBuilder();//创建一个字符数组来存储JSON字符数据
        try {
            URL my_url = new URL(url);
            is=my_url.openStream();//打开资源的流,获取字节流
            isr=new InputStreamReader(is,"utf-8");//字节流转化为字符流
            BufferedReader reader=new BufferedReader(isr);//用BufferedReader的方式将字符流读取出来
            String line;
            while ((line=reader.readLine())!=null) {
                response.append(line);//将数据逐个读取后添加到数组中
            }
            //关闭输入流
            if(is!=null) is.close();
            if(isr!=null) isr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //引用变量指向解析出来的实例对象
        newsBeanList=HttpUtil.parseJsonWithJSONObject(response.toString());
        return newsBeanList;
    }

    /***１－JSON数据解析：使用JSONObject***/
    public static List<NewsBean> parseJsonWithJSONObject(String jsonData) {
        List<NewsBean> newsBeanList=new ArrayList<>();//定义一个集合来存储解析出来的每组数据
        NewsBean newsBean;
        try{
            JSONObject jsonObject;
            jsonObject=new JSONObject(jsonData);//先获取整个JSON数据对象
            JSONArray jsonArray=jsonObject.getJSONArray("data");//获取"data"部分的集合
            //循环遍历数组,取出每个元素,每个元素都是一个JSONObject对象,并取出元素对应的数据信息
            for (int i = 0; i <jsonArray.length() ; i++) {
                jsonObject=jsonArray.getJSONObject(i);
                String newsIconUrl=jsonObject.getString("picSmall");
                String newsTitle=jsonObject.getString("name");
                String newsContent=jsonObject.getString("description");
                //创建单组数据的实例对象
                newsBean=new NewsBean(newsIconUrl,newsTitle,newsContent);
                //Log.d("lyc",newsBean.getNewsContent());
                newsBeanList.add(newsBean);//存储实例到集合中
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return newsBeanList;
    }

}
