package com.example.achuan.newsimooc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.achuan.newsimooc.model.bean.NewsBean;

import java.util.List;

/**
 * Created by achuan on 16-10-3.
 * 功能：实现列表中数据的适配
 * 技巧：提高ListView的显示效果,滑动时,停止加载数据;停止时,只加载当前显示范围类的数据
 * 注意事项：第一次显示列表时并没有发生滑动事件,此时并不会触发滑动状态改变的监听事件,所以需要
 *　　　　　在scroll方法中添加一个初始化第一屏的图片加载事件
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    //定义一个全局变量,保证只创建一个LruCache对象
    private ImageLoader mImageLoader;
    private int mStart,mEnd;//定义item加载的起始和结束的位置序号
    public static String[] URLS;//存储所有的图片链接地址
    private boolean mFirstIn;//是否第一次显示的标志


    //
    private List<NewsBean> mList;//创建集合来保存传入的数据集
    private LayoutInflater mInflater;//创建布局装载对象来获取相关控件（类似于findViewById()）

    /*创建构造方法*/
    public NewsAdapter(Context context, List<NewsBean> list, ListView  listview) {
        mList=list;
        //通过获取context来初始化mInflater对象
        mInflater=LayoutInflater.from(context);


        mImageLoader=new ImageLoader(listview);

        //存储所有的图片访问链接地址
        URLS=new String[list.size()];
        for (int i = 0; i <list.size() ; i++) {
            URLS[i]=list.get(i).getNewsIconUrl();
        }

        //为标志变量设置true,标志第一次显示
        mFirstIn=true;
        //别忘了对列表进行注册
        listview.setOnScrollListener(this);
    }
    /**************下面四个方法是直接继承来的方法**********/
    //适配器中数据集中的个数
    public int getCount() {
        return mList.size();
    }
    //获取数据集中与指定索引对应的数据项
    public Object getItem(int position) {
        return mList.get(position);
    }
    //获取指定行对应的ID
    public long getItemId(int position) {
        return position;
    }
    //获取每一个Item的显示内容
    /***********2.1主要编写该方法实现布局控件的设置************/
    public View getView(int position, View convertView, ViewGroup parent) {
        /***文艺式（viewHolder方法)***/
        ViewHolder viewHolder;//先创建实例
        if(convertView==null)//如果缓冲池中没有东西,才创建新的view
        {
            viewHolder =new ViewHolder();
            //获得（父）布局，并且加载一个item时只会创建一次view
            convertView=mInflater.inflate(R.layout.item_layout,null);
            //通过viewHolder的对象来获得（子）控件，并保存到viewHolder对象中
            viewHolder.icon= (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.title= (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.content= (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);//将viewHolder对象与convertView进行关联
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();//获得关联的对象（包括缓冲的控件）
        }
        //再通过viewHolder中缓冲的控件添加先关数据
        NewsBean bean=mList.get(position);//从数据源集合中获得对象


        //将item中的图片替换显示成网络加载的图片
        String url=bean.getNewsIconUrl();
        /*通过设置tag来保证图片和url的对应显示,防止网络加载时的时序错乱*/
        viewHolder.icon.setTag(url);
        /*****实现网络图片加载的方法*****/
        //方法一：使用Thread
        //new ImageLoader().showImageByThread(viewHolder.icon,url);
        //方法二：使用AsyncTask
        mImageLoader.showImageByAsyncTask(viewHolder.icon,url);

        //先将图片设置为系统的图片,默认显示该图片
        //viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.title.setText(bean.getNewsTitle());
        viewHolder.content.setText(bean.getNewsContent());
        return convertView;
    }

    //方法三：图片加载优化,滑动时不加载,滑动停止时才加载
    @Override//注意:该方法是在发送滑动的情况下才会执行的,也就是说初次显示列表时该方法并不会调用
    //所以我们还需要在列表显示最开始的界面时进行第一屏的图片加载
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if(i==SCROLL_STATE_IDLE)//滑动停止的状态,加载数据
        {
            //将指定范围中的图片加载显示出来
            mImageLoader.loadImages(mStart,mEnd);
        }
        else {//其他状态停止加载数据
            mImageLoader.cancelAllTasks();
        }
    }
    //方法参数介绍：1当前界面的view,2起始的item,3当前界面的item的数目,4全部item的数目
    @Override
    public void onScroll(AbsListView absListView,int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart=firstVisibleItem;
        mEnd=mStart+visibleItemCount;
        /***第一次显示列表时会先加载第一屏的图片***/
        if(mFirstIn&&visibleItemCount>0){
            mFirstIn=false;//已经不是第一次加载列表
            mImageLoader.loadImages(mStart,mEnd);
        }
    }

    /*2.3编写内部类：ViewHolder类,避免重复的findViewById(),节约资源*/
    private static class ViewHolder
    {
        //对应item中的3个控件
        public ImageView icon;
        public TextView title;
        public TextView content;
    }
}
