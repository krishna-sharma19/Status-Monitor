package com.example.admin.statusmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.admin.statusmonitor.R.id.wv;

public class WebFill extends AppCompatActivity {
    WebView webView;
    HashMap<String,String> hmIdValues;
    ArrayList<String> idNames;
    String script="";
    View gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_fill);
        Intent i = this.getIntent();
        hmIdValues = (HashMap<String,String>)i.getSerializableExtra("hmIdValues");;
        idNames = i.getStringArrayListExtra("idNames");

        String uniName = i.getStringExtra("uniName");
        String url = i.getStringExtra("link");

        webView = (WebView) findViewById(wv);
        System.out.println("loading url= "+url);


        webView.loadUrl(url);

         gif = findViewById(R.id.imgGif);
        int idImg = i.getIntExtra("imgId",0);
        gif.setBackgroundResource(idImg);
        (webView.getSettings()).setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());/* {
                                       @Override
                                       public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                           //Required functionality here
                                           System.out.println("js alert!!");
                                           return super.onJsAlert(view, url, message, result);
                                       }
                                   });*/
        webView.getSettings().setSupportMultipleWindows(true);

        //webView.getSettings().setAllowFileAccess(true);
        Toast.makeText(this,"Please remember to log out or you won't be able to use it again",Toast.LENGTH_LONG).show();
         //script = "javascript:";
        System.out.println("ids"+idNames.toString());
        for (String id : idNames) {
            String value = hmIdValues.get(id);

            if (value == null || value.isEmpty()) {
                System.out.println("value is null or empty");
                continue;
            }

            else
            {
                script += "  document.getElementById('"
                        + id
                        + "').value='"
                        + value
                        + "';";
            }
        }
        System.out.println("script is "+script);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                gif.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);



                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            //script += "};";
                            webView.evaluateJavascript(script, null);
                            System.out.println("evaluated");
                        } else {
                            //webView.loadUrl("window.obj.fillDetails()");
                            webView.loadUrl("javascript:"+script);
                            System.out.println("loaded");
                        }




            }


        });

        System.out.println("in the end-----");
        if(i.getStringExtra("flag")==null||!i.getStringExtra("flag").equals("directed")) {
            uniName=uniName.toUpperCase();
            University uni = new University(this, uniName,url,hmIdValues,idNames);
            uni.open();

            uni.insertUniLink();
            uni.insertUniDetails();
            //uni.deleteAll();
            uni.close();
            Toast.makeText(this,"Added Successfully",Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onBackPressed() {
     finish();
    }

        @Override
    public boolean onKeyDown(int keyCode,KeyEvent keyEvent)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, keyEvent);
    }
        }



