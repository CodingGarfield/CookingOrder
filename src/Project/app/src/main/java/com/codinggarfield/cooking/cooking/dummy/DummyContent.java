package com.codinggarfield.cooking.cooking.dummy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.codinggarfield.cooking.cooking.JavaBean.goods;
import com.codinggarfield.cooking.cooking.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static  List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static  Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

//    private static  int COUNT = 25;
    private static  String TAG = "";

    static {
        // Add some sample items.

         int count = 0;
        BmobQuery<goods> query=new BmobQuery<goods>();
        query.addWhereNotEqualTo("surplusgoods",0);
//        query.setLimit(5);
        query.findObjects(new FindListener<goods>() {
            @Override
            public void done(List<goods> object, BmobException e) {
                if(e==null){
//                    toast("查询成功：共"+object.size()+"条数据。");
                    Log.i("bmob","查询成功：共"+object.size()+"条数据。");
                    int countsum=object.size();
                    int ii=0;
                    for ( goods good : object) {
                        ii++;
                        Log.i("bmob","查询成功："+good.getGoodname());
                        Log.i("bmob","查询成功："+good.getGoodprice());
                        Log.i("bmob","查询成功："+good.getGoodImageUrl());
                        addItem(createDummyItem(ii, good.getGoodname(), good.getGoodprice(),good.getGoodintrodution(), good.getGoodImageUrl()));
                    }
                    MainActivity.updata();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Bitmap returnBitmap(String url) {

        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static DummyItem createDummyItem( int position,  String goodname,  String goodprice,String goodaddress,String goodimageUrl) {

//        Bitmap btimage=getHttpBitmap(goodImage);
        return new DummyItem(String.valueOf(position), goodname, makeDetails(position,goodprice,goodaddress),goodimageUrl,goodprice,goodaddress);
    }

    private static String makeDetails(int position,String goodprice ,String goodaddress) {
        StringBuilder builder = new StringBuilder();
        builder.append(goodprice).append("\n");
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;
        public String details;
        public String goodimageUrl;
        public String goodprice;
        public Bitmap goodImage;
        public String goodaddress;



        public DummyItem(String id, String content, String details,String goodimageUrl,String goodprice,String goodaddress) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.goodprice = goodprice;
            this.goodimageUrl = goodimageUrl;
            this.goodaddress = goodaddress;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
