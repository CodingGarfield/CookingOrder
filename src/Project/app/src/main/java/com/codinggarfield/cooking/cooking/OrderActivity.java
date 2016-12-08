package com.codinggarfield.cooking.cooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codinggarfield.cooking.cooking.JavaBean.UserBuy;
import com.codinggarfield.cooking.cooking.dummy.DummyContent;

import net.tsz.afinal.FinalBitmap;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class OrderActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    UserBuy userBuys;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    SharedPreferences sharedPreferences1;


    TextView nowprice;
    public static View mainview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        nowprice=(TextView)findViewById(R.id.nowPrice);

        sharedPreferences1 = getSharedPreferences("nowcode", Context.MODE_PRIVATE);
        int orderprice=1;
        if (sharedPreferences1!=null)
        orderprice = sharedPreferences1.getInt("orderprice", 0);

        nowprice.setText(String.valueOf(orderprice));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.Order_Actionbar));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mainview=mViewPager;
//        db = FinalDb.create(OrderActivity.this,"user_buy_table",true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle Price=new Bundle();
                Price.putString("price",nowprice.getText().toString());
                Intent payIntent=new Intent(OrderActivity.this,PayActivity.class);
                payIntent.putExtras(Price);
                startActivity(payIntent);
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                editor.clear();
                editor.commit();
//                Snackbar.make(view, "已经加入豪华午餐", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(OrderActivity.this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */


        UserBuy userBuys;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private SharedPreferences sharedPreferences12;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private FinalBitmap fb;
        List<String> foodsUrl;

        private DummyContent.DummyItem mItem1;

        int oi1=1;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_order, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView IV=(ImageView)rootView.findViewById(R.id.food);
            String foodsUrl ="http://bmob-cdn-7882.b0.upaiyun.com/2016/12/02/b5f11a5b49d34b94b756617222a7ffbf.jpg";
            //网络下载
            fb = FinalBitmap.create(getActivity());//初始化FinalBitmap模块
            fb.configLoadingImage(R.drawable.food1);

            sharedPreferences12 = getActivity().getSharedPreferences("nowcode", Context.MODE_PRIVATE);
            Set<String> foodlistUrlset=new LinkedHashSet();
            foodlistUrlset=sharedPreferences12.getStringSet("foodlistUrlset",foodlistUrlset);
                //获取网络图片
            int oi=1;
                for (String foodUrl : foodlistUrlset) {
                    if (oi == getArguments().getInt(ARG_SECTION_NUMBER)) {
//                        if(oi1<foodlistUrlset.size()) {
                            Log.i("image", foodUrl);
                            fb.display(IV, foodUrl);
                            oi1++;
                            System.out.println("oi:::::::::" + oi + ":::::::" + foodlistUrlset.size());
//                        }
//                        else
//                        {
//                        }
                    }
                    oi++;
                }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            Set foodlistset = new LinkedHashSet();
            sharedPreferences1 = getSharedPreferences("nowcode", Context.MODE_PRIVATE);
            foodlistset=sharedPreferences1.getStringSet("foodlistset",foodlistset);
            int count=3;
            count=foodlistset.size();
            return count;
        }
    }
}
