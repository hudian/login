package com.example.administrator.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.login.sqlitdemo.UserInfo;
import com.example.administrator.login.sqlitdemo.UserInfoDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    UserInfoDao mUserInfoDao;
    UserInfo mUserInfo;
    private final String loginUrl = "file:///android_asset/login.html";
    private WebView mWebView;
    private WebSettings webSettings;
    String time;
    boolean isSeclet = true;

    private static final String tag = "MainActivity";
    public final static int TAKE_PHOTO = 1;
    public final static int PHOTO_ZOOM = 0;
    public final static int PHOTO_RESULT = 2;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir;
    private ImageView avatar;
    private Uri picUri;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewInit();
        mWebView.addJavascriptInterface(new JsInteration() {
        }, "demo");
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(loginUrl);


    }

    private void webViewInit() {
        mWebView = (WebView) findViewById(R.id.web_view);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        mWebView.clearCache(true);
    }

    public class JsInteration {
        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void Register(final String url, String username, String pswd) {
            //向数据库中写数据
            mUserInfoDao = new UserInfoDao(MainActivity.this);
            mUserInfo = new UserInfo();
            if (mUserInfoDao.queryPswdByUsername(username) != null) {
                Toast.makeText(getApplicationContext(), "账号已存在",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mUserInfo.setUsernaem(username);
            mUserInfo.setPswd(pswd);
            mUserInfoDao.insert(mUserInfo);
            Toast.makeText(getApplicationContext(), "注册成功：" + "账号" + username +
                    "密码" + pswd, Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(url);
                }
            });
        }

        @JavascriptInterface
        public void login(String username, String mpswd) {

            mUserInfoDao = new UserInfoDao(MainActivity.this);
            String pswd = mUserInfoDao.queryPswdByUsername(username);

            if (mpswd.equals(pswd)) {
                Toast.makeText(getApplicationContext(), "登陆成功",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "登陆失败，请重新输入",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @JavascriptInterface
        public void selectBeginTime() {

            time = "";

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                    startActivityForResult(intent,20);
                }

            });
        }

        @JavascriptInterface
        public void selectPicture(){

            picUri = null;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(IMAGE_UNSPECIFIED);
                    Intent wrapperIntent=Intent.createChooser(intent, null);
                    startActivityForResult(wrapperIntent, PHOTO_ZOOM);
                }

            });
        }

        @JavascriptInterface
        public String doPost(){
            HttpURLConnection conn = null;
            String myip = "10.45.34.179";


            try {
                File pic = new File(picUri.getPath());
                FileInputStream fis = new FileInputStream(pic);
                byte[] picdata = new byte[0];
                fis.read(picdata);
                URL url = new URL("http://" + myip + "/myService/servlet/LoginServlet?");
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(5000);
                conn.setDoOutput(true);

                OutputStream out = conn.getOutputStream();
                out.write(picdata);
                out.flush();
                out.close();
                fis.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == 200) {
                    InputStream is = conn.getInputStream();
                    String state = getStringFromInpitStream(is);
                    return state;
                }else{

                    Log.i(tag, "faled" + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            return null;
        }

        @JavascriptInterface
        public String getTime(){
            while (isSeclet) {
                if (!time.isEmpty()){
                    return time;
                }
            }
            return null;
        }

        @JavascriptInterface
        public Uri getPicUri(){
            while (isSeclet) {
                if (picUri != null){
                    return picUri;
                }
            }
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 20){
            Toast.makeText(MainActivity.this, data.getStringExtra("time"),
                    Toast.LENGTH_SHORT).show();//得到返回结果);
            MainActivity.super.onActivityResult(requestCode, resultCode, data);
            time = data.getStringExtra("time");
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_ZOOM) {
                picUri = data.getData();
                Toast.makeText(MainActivity.this,picUri.toString(),Toast.LENGTH_LONG).show();
//                photoZoom(data.getData());
            }
            if (requestCode == TAKE_PHOTO) {
                File picture = new File(Environment.getExternalStorageDirectory() + "/" + imageDir);
                photoZoom(Uri.fromFile(picture));
            }

            if (requestCode == PHOTO_RESULT) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    avatar.setImageBitmap(photo);
                }
            }
        }
    }
    // 图片缩放
    public void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    public static String getStringFromInpitStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while((len = is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        is.close();
        String html = baos.toString();
        baos.close();
        return html;
    }
}


