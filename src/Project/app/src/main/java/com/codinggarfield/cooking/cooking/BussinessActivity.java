package com.codinggarfield.cooking.cooking;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codinggarfield.cooking.cooking.JavaBean.bussiness;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BussinessActivity extends AppCompatActivity {

    Button save,exit;
    EditText bname,baddr,bphone;
    Snackbar successbar,failbar;
    bussiness newbussiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        bname=(EditText)findViewById(R.id.upbname);
        baddr=(EditText)findViewById(R.id.upblocal);
        bphone=(EditText)findViewById(R.id.upbphone);
        save=(Button)findViewById(R.id.savenbussiness_button);
        exit=(Button)findViewById(R.id.editb_exit_button);

        successbar=Snackbar.make(bname,getResources().getString(R.string.Edit_Success), Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        failbar=Snackbar.make(bname,getResources().getString(R.string.Edit_Failed), Snackbar.LENGTH_LONG)
                .setAction("Action", null);

        newbussiness=new bussiness();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(bname.getText().toString()))
                {
                    newbussiness.setName(bname.getText().toString());
                }
                if(!"".equals(baddr.getText().toString()))
                {
                    newbussiness.setLocal(baddr.getText().toString());
                }
                if(!"".equals(bphone.getText().toString()))
                {
                    newbussiness.setPhone(bphone.getText().toString());
                }
                newbussiness.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null)
                        {
                            successbar.show();
                        }
                        else
                        {
                            failbar.show();
                        }
                    }
                });
            }
        });
    }
}
