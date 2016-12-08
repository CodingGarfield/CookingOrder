package com.codinggarfield.cooking.cooking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codinggarfield.cooking.cooking.JavaBean.MyUser;
import com.codinggarfield.cooking.cooking.JavaBean.UserBuy;
import com.codinggarfield.cooking.cooking.dummy.DummyContent;

import net.tsz.afinal.FinalBitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    BmobQuery<UserBuy> query;
    UserBuy userBuy;
    MyUser user1;
    private FinalBitmap fb;
    Snackbar successorder,failorder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //网络下载
        fb = FinalBitmap.create(getActivity());//初始化FinalBitmap模块
        fb.configLoadingImage(R.drawable.food1);

        userBuy = new UserBuy();

        sharedPreferences = getActivity().getSharedPreferences("nowcode", Context.MODE_PRIVATE);


        user1 = BmobUser.getCurrentUser(MyUser.class);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
                fb.display(appBarLayout,mItem.goodimageUrl);
            }
        }
    }

    static List<Bitmap> bitmapList;
    public List<Bitmap> getBitmapList()
    {
        return bitmapList;
    }

    public void updataBuyData()
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        editor = sharedPreferences.edit();//获取编辑器
        Set foodlistset=new LinkedHashSet();
        Set foodlistUrlset=new LinkedHashSet();
        int orderprice=0;
        try{
            orderprice=sharedPreferences.getInt("orderprice",0);
        }catch (NullPointerException e)
        {
        }
        foodlistset=sharedPreferences.getStringSet("foodlistset",foodlistset);
        foodlistUrlset=sharedPreferences.getStringSet("foodlistUrlset",foodlistUrlset);
        addone("foodlistset",mItem.content,foodlistset,editor);
        addone("foodlistUrlset",mItem.goodimageUrl,foodlistUrlset,editor);
        editor.putString("code",date+user1.getUsername());
        editor.putString("Username",user1.getUsername());
        editor.putInt("orderprice",orderprice+Integer.parseInt(mItem.goodprice));
        editor.commit();
//        bitmapList.add();
    }

    public void addone(String key,String string,Set channelSet,SharedPreferences.Editor spEditor){
        channelSet = new LinkedHashSet<String>(channelSet);
        channelSet.add(string);
        spEditor.putStringSet(key, channelSet);
        spEditor.commit();
    }

    public void uploadBuyData()
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        userBuy.setUsername(sharedPreferences.getString("Username",user1.getUsername()));
        userBuy.setOrdercode(sharedPreferences.getString("code",date+user1.getUsername()));
        int price = 0;
        List<String> foodlist = null,foodlistUrl=null;
        userBuy.setFoodlist(new ArrayList<String>(sharedPreferences.getStringSet("foodlist",null)));
        userBuy.setOrderprice(sharedPreferences.getInt("orderprice",0));
        userBuy.setFoodlistUrl(new ArrayList<String>(sharedPreferences.getStringSet("foodlistUrl",null)));
        userBuy.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null)
                {
                    //成功订餐
                    successorder.show();
                }
                else
                {
                    //订餐失败
                    failorder.show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.item_address)).setText(mItem.goodaddress);
        }

        return rootView;
    }
}
