package com.codinggarfield.cooking.cooking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.codinggarfield.cooking.cooking.JavaBean.MyUser;
import com.codinggarfield.cooking.cooking.JavaBean.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> ,WheelPicker.OnItemSelectedListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor Ed;

    BmobQuery<User> query;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    Resources res;
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //默认用户为普通用户
    private String usertype="user";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        res= getResources();
        //
//        sharedPreferences
        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        Ed=sharedPreferences.edit();




        ///云端初始化
        query = new BmobQuery<>();

        //User表
//        user=new User();
        if(isFolderExists(Environment.getExternalStorageDirectory()+"/Cooking_Image/")) {
            Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.head);
            try {
                saveMyBitmap("head", bitmap);
            } catch (NullPointerException e) {
                System.out.println("获取空对象");
            }
        }
        if(isFolderExists(Environment.getExternalStorageDirectory()+"/Cooking_Image_goods/")) {
            Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.food1);
            try {
                saveMyBitmap("head", bitmap);
            } catch (NullPointerException e) {
                System.out.println("获取空对象");
            }
        }


        //选择器
        WheelPicker wheelCenter = (WheelPicker) findViewById(R.id.main_wheel_center);
        wheelCenter.setOnItemSelectedListener(this);
        List<String> data = new ArrayList<>();
        data.add(getResources().getString(R.string.wheel_user));
        data.add(getResources().getString(R.string.wheel_business));
        data.add(getResources().getString(R.string.wheel_admin));
        wheelCenter.setData(data);
        //判断自动登录
        MyUser user1 = BmobUser.getCurrentUser(MyUser.class);
        if(user1 != null){
            // 允许用户使用应用
                if("user".equals(user1.getUsertype()))
                {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else if ("business".equals(user1.getUsertype()))
                {
                    startActivity(new Intent(LoginActivity.this, EditgoodsActivity.class));
                    finish();
                }
                else if ("admin".equals(user1.getUsertype()))
                {
                    startActivity(new Intent(LoginActivity.this, ChartsActivity.class));
                    finish();
                }
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…

        }

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mRegister = (Button)findViewById(R.id.register_button);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    boolean isFolderExists(String strFolder)
    {
        File file = new File(strFolder);

        File destDir = new File(strFolder);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (!file.exists())
        {
            if (file.mkdir())
            {
                return true;
            }
            else
                return false;
        }
        return true;
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
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    //选择器
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
//        switch (picker.getId()) {
//            case R.id.main_wheel_center:
//                text = "Center:";
//                break;
//        }
        switch (position)
        {
            case 0://用户
                usertype="user";
                break;
            case 1://商人
                usertype="business";
                break;
            case 2://管理员（公司）
                usertype="admin";
                break;
            default:
                break;
        }
//        Toast.makeText(this, text + String.valueOf(data), Toast.LENGTH_SHORT).show();
        Snackbar.make(mProgressView, getResources().getString(R.string.Login_snackbar_send)+String.valueOf(data), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        //判断登录名
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private String tPassword="";
        private String tusertype="";

        UserLoginTask(String email, String password) {
            mUsername = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            MyUser user=new MyUser();
            user.setUsername(mUsername);
            user.setPassword(mPassword);
            user.setUsertype(usertype);
            user.login(new SaveListener<BmobUser>() {

                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e==null){
                        //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        MyUser user1 = BmobUser.getCurrentUser(MyUser.class);
                        if((user1.getUsertype()).equals(usertype)&&usertype.equals("user")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        else if((user1.getUsertype()).equals(usertype)&&usertype.equals("business"))
                        {
                            startActivity(new Intent(LoginActivity.this, EditgoodsActivity.class));
                            finish();
                        }
                        else if((user1.getUsertype()).equals(usertype)&&usertype.equals("admin"))
                        {
                            startActivity(new Intent(LoginActivity.this, ChartsActivity.class));
                            finish();
                        }
                        else
                        {
                        }
//                        Ed.putString("username",mUsername);
//                        Ed.putString("password",mPassword);
//                        Ed.putString("usertype",usertype);
//                        Ed.commit();
                    }else{
                    }
                }
            });

//            ///云端数据库查询
//            query.setLimit(1).addWhereEqualTo("username",mUsername)
//                    .findObjects(new FindListener<User>() {
//                        @Override
//                        public void done(List<User> object, BmobException e) {
//                            if (e == null) {
//                                System.out.println(""+tPassword);
//                                // 找得到
//                                for (User user : object) {
//                                    tPassword=user.getPassword();
//                                    tusertype=user.getUsertype();
//                                }
////                                System.out.println("数据库："+tPassword+"///输入："+mPassword);
//                                if (success) {
//                                    if(tPassword.equals(mPassword)) {
//                                        if(tusertype.equals(usertype)) {
//                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                            finish();
//                                        }
//                                        else if(tusertype.equals(usertype))
//                                        {
//
//                                        }
//                                        else if(tusertype.equals(usertype))
//                                        {
//                                            startActivity(new Intent(LoginActivity.this, ChartsActivity.class));
//                                            finish();
//                                        }
//                                        else
//                                        {
//
//                                        }
//                                        Ed.putString("username",mUsername);
//                                        Ed.putString("password",mPassword);
//                                        Ed.putString("usertype",usertype);
//                                        Ed.commit();
//
//                                        //更新登录状态
////                                        user.setState("online");
////                                        user.update(new UpdateListener() {
////
////                                            @Override
////                                            public void done(BmobException e) {
////                                                if(e==null){
////                                                    Log.i("bmob","更新成功");
////                                                }else{
////                                                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
////                                                }
////                                            }
////                                        });
//                                    }
//                                    else
//                                    {
//                                        System.out.println("//正确密码是"+tPassword);
//                                    }
//                                }
//                                else
//                                {
//                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                                    mPasswordView.requestFocus();
//                                }
//                            } else {
//                                // 找不到
//                                System.out.println("找不到"+tPassword);
//                                mUsernameView.setError(getString(R.string.error_invalid_username));
//                                mUsernameView.requestFocus();
//                            }
//                        }
//                    });
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

