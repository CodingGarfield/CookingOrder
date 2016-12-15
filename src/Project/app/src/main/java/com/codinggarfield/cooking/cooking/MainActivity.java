package com.codinggarfield.cooking.cooking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codinggarfield.cooking.cooking.JavaBean.bussiness;
import com.codinggarfield.cooking.cooking.dummy.DummyContent;
import com.shizhefei.view.indicator.BannerComponent;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private BannerComponent bannerComponent;
    private boolean mTwoPane;
    private RelativeLayout  infoRL;
    private TextView username;
    private SharedPreferences sharedPreferences;
    private Intent editInfo, settingIntent;
    private SharedPreferences.Editor Ed;
    private String usernamest="UserName";
    private FinalBitmap fb;
    android.support.v7.app.AlertDialog.Builder builder,find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        Ed=sharedPreferences.edit();
        usernamest=sharedPreferences.getString("username","");


        //网络下载
        fb = FinalBitmap.create(this);//初始化FinalBitmap模块
        fb.configLoadingImage(R.drawable.food1);


        //Intent
        settingIntent =new Intent(MainActivity.this,SettingsActivity.class);
        editInfo=new Intent(MainActivity.this,EditInfoActivity.class);

        final EditText inputServer = new EditText(this);
        builder = new android.support.v7.app.AlertDialog.Builder(this);
        find = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(inputServer);
        builder.setTitle("搜索商家");
        builder.setMessage("请在输入框输入商家名");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!"".equals(inputServer.getText().toString()))
                {

                    BmobQuery<bussiness> query = new BmobQuery<bussiness>();
                    query.addWhereEqualTo("name",inputServer.getText().toString());
                    query.findObjects(new FindListener<bussiness>() {
                        @Override
                        public void done(List<bussiness> list, BmobException e) {
                            if (e==null)
                            {
                                for (bussiness bussin : list) {
                                    find.setMessage("查找商家" + inputServer.getText().toString() + "的结果\n"
                                            + "\n店址："+bussin.getLocal()
                                            + "\n电话："+bussin.getPhone());
                                    find.show();
                                }
                            }
                            else
                            {
                                find.setMessage("找不到数据");
                                find.show();
                            }
                        }
                    });

                }
            }
        });



        //list
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,OrderActivity.class));
//                Snackbar.make(view, "查看已点菜单", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        username=(TextView)view.findViewById(R.id.username);
        username.setText(usernamest);
        infoRL=(RelativeLayout)view.findViewById(R.id.info_RelativeLayout);
        infoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新个人信息
                startActivity(editInfo);
            }
        });


        //轮播图
        ViewPager viewPager = (ViewPager) findViewById(R.id.banner_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.banner_indicator);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), Color.parseColor("#80ffffff"), 0, ScrollBar.Gravity.CENTENT_BACKGROUND));
        viewPager.setOffscreenPageLimit(2);

        bannerComponent = new BannerComponent(indicator, viewPager, false);
        bannerComponent.setAdapter(adapter);

        //默认就是800毫秒，设置单页滑动效果的时间
        bannerComponent.setScrollDuration(800);
        //设置播放间隔时间，默认情况是3000毫秒
        bannerComponent.setAutoPlayTime(2500);
    }



    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
//            if(btpic!=null)
//                holder.goodImageView.setImageBitmap(btpic);
            fb.display(holder.goodImageView,mValues.get(position).goodimageUrl);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        context.startActivity(intent);
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final ImageView goodImageView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                goodImageView=(ImageView)view.findViewById(R.id.listfood);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    static SimpleItemRecyclerViewAdapter madapter;

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                madapter = new MainActivity.SimpleItemRecyclerViewAdapter(DummyContent.ITEMS);
                recyclerView.setAdapter(madapter);
            }
        }.start();
    }

    public static void updata()
    {
        madapter.notifyDataSetChanged();
    }
    
    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Handle the camera action

        } else if (id == R.id.nav_order) {
            startActivity(new Intent(MainActivity.this,OrderActivity.class));
        } else if (id == R.id.nav_myinfo) {
            startActivity(editInfo);
        } else if (id == R.id.nav_exit) {
            BmobUser.logOut();   //清除缓存用户对象
            BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
            this.finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_search) {
            builder.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        return true;
    }




    @Override
    protected void onStart() {
        super.onStart();
        bannerComponent.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bannerComponent.stopAutoPlay();
    }

    private int[] images = {R.drawable.food1, R.drawable.food2, R.drawable.food3};

    private IndicatorViewPager.IndicatorViewPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(container.getContext());
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new ImageView(getApplicationContext());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            ImageView imageView = (ImageView) convertView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(images[position]);
            return convertView;
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return RecyclingPagerAdapter.POSITION_NONE;
//        }

        @Override
        public int getCount() {
            return images.length;
        }
    };


}
