package com.codinggarfield.cooking.cooking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codinggarfield.cooking.cooking.JavaBean.goods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditgoodsActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int NONE = 0;
    public static final int PHOTO_CAMERA = 1;// 相机拍照
    public static final int PHOTO_COMPILE = 2; // 编辑图片
    public static final int PHOTO_RESOULT = 3;// 结果
    // 调用startActivityResult，返回之后的回调函数
    private String ImageName="";
    goods goods;
    BmobFile bmobFile;

    Snackbar successbar,failbar;

    EditText upgoodname,upgoodprice,upgoodintroduction,upgoodsurplus;
    ImageView upgoodImage;
    Button savegood,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgoods);
        upgoodname=(EditText)findViewById(R.id.upgoodname);
        upgoodprice=(EditText)findViewById(R.id.upgoodprice);
        upgoodintroduction=(EditText)findViewById(R.id.upgoodintroduction);
        upgoodsurplus=(EditText)findViewById(R.id.upgoodsurplus);
        upgoodImage=(ImageView) findViewById(R.id.upgoodimage);
        savegood=(Button)findViewById(R.id.savegood_button);
        exit=(Button)findViewById(R.id.edit_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                finish();
            }
        });

        goods=new goods();

        successbar=Snackbar.make(upgoodname,getResources().getString(R.string.Edit_Success), Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        failbar=Snackbar.make(upgoodname,getResources().getString(R.string.Edit_Failed), Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        upgoodImage.setOnClickListener(this);
        savegood.setOnClickListener(this);
    }

    public static String getStringToday() {
        String dateString ="/Cooking_Image_goods/foodimage";
        return dateString;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.upgoodimage:
                //读写照片权限
                int hasWriteContactsPermission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    Activity activty=this;
                    ActivityCompat.requestPermissions(activty,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    return;
                }
                //拍照权限
                int hasCAMPermission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasCAMPermission = checkSelfPermission(Manifest.permission.CAMERA);
                }
                if (hasCAMPermission != PackageManager.PERMISSION_GRANTED) {

                    Activity activty=this;

                    ActivityCompat.requestPermissions(activty,new String[] {Manifest.permission.CAMERA},1);
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(EditgoodsActivity.this,android.R.style.Theme_Holo_Light_Dialog);
//                builder.setIcon(R.drawable.foodimage);
                builder.setTitle("选择商品来源");
                //    指定下拉列表的显示数据
                final String[] nicks = {"图库", "拍照"};
                //    设置一个下拉的列表选择项
                builder.setItems(nicks, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                // 设置调用系统相册的意图(隐式意图)
                                Intent intent = new Intent();

                                //设置值活动//android.intent.action.PICK
                                intent.setAction(Intent.ACTION_PICK);

                                //设置类型和数据
                                intent.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");

                                // 开启系统的相册
                                startActivityForResult(intent, PHOTO_COMPILE);
                                break;
                            case 1:
                                //设置图片的名称
                                ImageName = "/" + getStringToday() + ".jpg";

                                // 设置调用系统摄像头的意图(隐式意图)
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                //设置照片的输出路径和文件名

                                File file=new File(Environment.getExternalStorageDirectory(), ImageName);

                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                //开启摄像头
                                startActivityForResult(intent2, PHOTO_CAMERA);
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                break;
            case R.id.savegood_button:

                if(!"".equals(upgoodname.getText().toString()))
                {
                    goods.setGoodname(upgoodname.getText().toString());
                }
                if(!"".equals(upgoodsurplus.getText().toString()))
                {
                    goods.setSurplusgoods(Integer.valueOf(upgoodsurplus.getText().toString()));
                }
                if(!"".equals(upgoodprice.getText().toString()))
                {
                    goods.setGoodprice(upgoodprice.getText().toString());
                }
                if(!"".equals(upgoodintroduction.getText().toString()))
                {
                    goods.setGoodintrodution(upgoodintroduction.getText().toString());
                }if((new File(Environment.getExternalStorageDirectory()+getStringToday()+ ".jpg")).exists()) {

                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            goods.setGoodImageUrl(bmobFile.getFileUrl());
                            goods.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
//                        toast("更新用户信息成功");
                                        successbar.show();
                                    }else{
//                        toast("更新用户信息失败:" + e.getMessage());
                                        failbar.show();
                                    }
                                }
                            });
                        }
                        else{
                            goods.setGoodImageUrl("");
                            Log.i("bmob",e.toString());
                        }
                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });
            }
            else
            {
                Log.i("Bmob","文件不存在");
            }

                break;
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;


        if (data == null)
            return;

        // 通过照相机拍照的图片出理
        if (requestCode == PHOTO_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            int REQUEST_EXTERNAL_STORAGE = 1;
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int permission = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        EditgoodsActivity.this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
            File picture = new File(Environment.getExternalStorageDirectory()
                    + ImageName);
            //裁剪图片
            startPhotoZoom(Uri.fromFile(picture));
        }

        // 读取相册裁剪图片
        if (requestCode == PHOTO_COMPILE) {
            //裁剪图片
            startPhotoZoom(data.getData());

        }


        // 裁剪照片的处理结果
        if (requestCode == PHOTO_RESOULT) {

            Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG,90, stream);// (0 - 100)压缩文件

                saveMyBitmap("foodimage",photo);

                //转换圆形
//                Bitmap btrd=toRoundBitmap(photo);
//                Drawable drawable =new BitmapDrawable(btrd);

                //设置图片显示内容
                upgoodImage.setImageBitmap(photo);
                bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+getStringToday()+ ".jpg"));
//                NavigationDrawerFragment.setfoodimageimg();
//                getActivity().getActionBar().setIcon(drawable);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_RESOULT);
    }
    public void saveMyBitmap(String bitName,Bitmap mBitmap){

        @SuppressLint("SdCardPath")
        File f = new File(Environment.getExternalStorageDirectory()+"/Cooking_Image_goods/" + bitName + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // DebugMessage.put("在保存图片时出错："+e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
