package com.codinggarfield.cooking.cooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;

public class PayActivity extends AppCompatActivity implements View.OnClickListener{

    Button AliPayBtn,WechatPayBtn;
    TextView Price,tv,order;
    String Name="",Boby="",Order="";//Name 商品名/Boby 描述/Order 查询
    String APPID="11e93b5e5fad3225bebec3fde45839e7";

    ProgressDialog dialog;

    Boolean PayState=false;//判断支付是否成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        BP.init(this,APPID);
        AliPayBtn=(Button)findViewById(R.id.alipay_button);
        WechatPayBtn=(Button)findViewById(R.id.wechat_pay_button);
        Price=(TextView)findViewById(R.id.price);
        Bundle myBundelForGetName=this.getIntent().getExtras();
        String price=myBundelForGetName.getString("price");
        if(price!=null) {
            Price.setText(price);
        }

        AliPayBtn.setOnClickListener(this);
        WechatPayBtn.setOnClickListener(this);
    }

    // 默认为0.02
    double getPrice() {
        double price = 0.01;
        try {
            price = Double.parseDouble(this.Price.getText().toString());
        } catch (NumberFormatException e) {
        }
        return price;
    }
    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }
    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在获取订单...");
        final String name = getName();

        BP.pay(name, getBody(), getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
//                tv.append(name + "'s pay status is unknow\n\n");
                hideDialog();
                PayState=false;
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
//                tv.append(name + "'s pay status is success\n\n");
                hideDialog();
                PayState=true;
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
                PayState=false;
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            PayActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                hideDialog();
                PayState=false;
            }
        });
    }

    // 执行订单查询
    void query() {
        showDialog("正在查询订单...");
        final String orderId = getOrder();

        BP.query(orderId, new QListener() {

            @Override
            public void succeed(String status) {
                Toast.makeText(PayActivity.this, "查询成功!该订单状态为 : " + status,
                        Toast.LENGTH_SHORT).show();
//                tv.append("pay status of" + orderId + " is " + status + "\n\n");
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PayActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
//                tv.append("query order fail, error code is " + code
//                        + " ,reason is \n" + reason + "\n\n");
                hideDialog();
            }
        });
    }
    // 商品名(可不填)
    String getName() {
        return this.Name;
    }

    // 商品详情(可不填)
    String getBody() {
        return this.Boby;
    }

    // 支付订单号(查询时必填)
    String getOrder() {
        return this.Order;
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.alipay_button:
                pay(true);
                break;
            case R.id.wechat_pay_button:
                pay(false);
                break;
            default:
                break;
        }
    }
}
