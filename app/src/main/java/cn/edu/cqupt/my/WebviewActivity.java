package cn.edu.cqupt.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebviewActivity extends AppCompatActivity {
    private Button btnexit;
    private WebView webview;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private CookieManager cookieManager = CookieManager.getInstance();
    public static void Actionstart(Context ctx,Bundle a)
    {
        Intent intent=new Intent(ctx,WebviewActivity.class);
        intent.putExtras(a);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        btnexit=findViewById(R.id.title_lift);
        btnexit.setBackgroundResource(R.drawable.exit1);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebviewActivity.this.onDestroy();
                WebviewActivity.this.finish();
            }
        });
        webview=findViewById(R.id.webview);
        initWebView();
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String scheme = Uri.parse(url).getScheme();//还需要判断host
                if (TextUtils.equals("uubang", scheme)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        //setcookie(getIntent().getExtras().getString("DestUrl"));
        //

        if (getIntent().getExtras().getString("POST")!=null) {
            String postdata="user="+LoginConfig.getID(MainActivity.ctx)+"&"+"password="+LoginConfig.getPWD(MainActivity.ctx);
            byte[] postbyte=postdata.getBytes();
            webview.postUrl(getIntent().getExtras().getString("DestUrl"),postbyte);
        }
        else {
            webview.loadUrl(getIntent().getExtras().getString("DestUrl"));
        }
    }
    private void initWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        //自适应屏幕

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        webview.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.i("test", "openFileChooser 1");
                WebviewActivity.this.uploadFile = uploadMsg;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
                Log.i("test", "openFileChooser 2");
                WebviewActivity.this.uploadFile = uploadMsgs;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.i("test", "openFileChooser 3");
                WebviewActivity.this.uploadFile = uploadMsg;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
                WebviewActivity.this.uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }

        });
    }
    private void setcookie(String url){

        cookieManager.setCookie(url, "user="+MainActivity.GetID());

        cookieManager.setCookie(url, "password="+LoginConfig.getPWD(MainActivity.ctx));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        //cookieManager.setCookie(url, StringCookie);


    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "上传照片"), 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* if(getIntent().getExtras().getString("DestUrl").equals("http://uubang.flyzhangyx.com/upload_user_head.php"))
        {
            MainActivity.update_pic=true;
        }*/
        webview.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (null != uploadFile) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFile.onReceiveValue(result);
                    uploadFile = null;
                }
                if (null != uploadFiles) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFiles.onReceiveValue(new Uri[]{result});
                    uploadFiles = null;
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (null != uploadFile) {
                    uploadFile.onReceiveValue(null);
                    uploadFile = null;
                }
            }
        }
    }

}

