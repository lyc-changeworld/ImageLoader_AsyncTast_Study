package com.example.achuan.newsimooc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.achuan.newsimooc.model.bean.NewsBean;
import com.example.achuan.newsimooc.model.http.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //执行网络请求的网址,将返回JSON数据,后续将解析它们
    private static String URL="http://www.imooc.com/api/teacher?type=4&num=30";
    @BindView(R.id.lv_main)
    ListView mLvMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new NewsAsyncTask().execute(URL);//执行网络请求的任务
    }
    //AsyncTask<1,2,3> 3个参数的介绍：1传入给后台任务的数据的类型,2进度值类型,3返回值类型
    class NewsAsyncTask  extends AsyncTask<String,Void,List<NewsBean>> {
        //后台任务执行前的初始化操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //后台子线程执行耗时操作的方法
        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            return HttpUtil.getJsonData(strings[0]);
        }
        /*//doInBackground方法中执行publishProgress()进行进度显示后执行该方法
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }*/
        //后台任务执行完后执行该方法,并传递执行后的结果进来
        @Override
        protected void onPostExecute(List<NewsBean> newsBeens) {
            super.onPostExecute(newsBeens);
            //创建一个适配器
            NewsAdapter adapter=new NewsAdapter(MainActivity.this,newsBeens,mLvMain);
            mLvMain.setAdapter(adapter);//为列表添加适配器
        }
    }

}
