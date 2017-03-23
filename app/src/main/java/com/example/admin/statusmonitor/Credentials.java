package com.example.admin.statusmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

public class Credentials extends AppCompatActivity {
    ArrayList<String> idNames = new ArrayList<>();
          HashMap<String,String> hmIdValues;
    //HashMap<String, String> hmNameId = new HashMap<>();

    RequestQueue requestQueue;
    StringRequest strreq;
    Context c;
    CustomArrayAdapter cad;
    String url;
    String uniName;
    String[] arr;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
         lv = (ListView) findViewById(R.id.lv);
        c = this;

        requestQueue = Volley.newRequestQueue(this);
        Intent i = this.getIntent();
        url = i.getStringExtra("link");
        uniName = i.getStringExtra("uniName");
        //url = "https://www.wolftech.ncsu.edu/gradwatch/students/login.php";//"https://app.applyyourself.com/AYApplicantLogin/fl_ApplicantLogin.asp?id=ncsu-grad";



        strreq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("inside reponse");
                crawlUrl(url);
            }
        }
            , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error");

            }
        });

        requestQueue.add(strreq);

    }


    public void submit(View v)
    {
         hmIdValues = cad.getHashMapValues();
        System.out.println("hm sent"+hmIdValues);


        Intent itoWebFill = new Intent(this,WebFill.class);

        //itoWebFill.putExtra("hmNameValues",hmNameValues);
        itoWebFill.putStringArrayListExtra("idNames",idNames);
        itoWebFill.putExtra("hmIdValues",hmIdValues);
        itoWebFill.putExtra("link",url);
        itoWebFill.putExtra("uniName",uniName);
        this.startActivity(itoWebFill);


    }

    public void crawlUrl(String url)
    {
        WebView wv = (WebView)findViewById(R.id.wv);
        System.out.println("loading url= "+url);
        wv.loadUrl(url);
        (wv.getSettings()).setJavaScriptEnabled(true);
       // wv.setWebViewClient(new WebViewClient());//this is important else webview is not used and browser opens
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebView wv = (WebView)findViewById(R.id.wv);
                String script = "javascript:";
                script +="form = document.getElementsByTagName('form');"
                        +"ips = form[0].getElementsByTagName('input');"
                        +"arr=[];"
                        +"for(i=0;i<ips.length;i++) if(ips[i].type!='hidden'||ips[i].type!='submit') arr[i]=ips[i].id;"
                        +"window.obj.setIds(arr);";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    //script += "};";
                    wv.evaluateJavascript(script, null);
                    System.out.println("evaluated");
                } else {
                    //webView.loadUrl("window.obj.fillDetails()");
                    wv.loadUrl(script);
                    System.out.println("loaded");
                }




            }


        });
        wv.addJavascriptInterface(new Getter(),"obj");

       /* String script = "javascript:window.onload=function(){";
        script +="form = document.getElementsByTagName('form');"
                +"ips = form[0].getElementsByTagName('input');"
                +"arr=[];"
                +"for(i=0;i<ips.length;i++) if(ips[i].type!='hidden'||ips[i].type!='submit') arr[i]=ips[i].id;"
                +"window.obj.setIds(arr);};";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            //script += "};";
            wv.evaluateJavascript(script, null);
        } else {
            //webView.loadUrl("window.obj.fillDetails()");
            wv.loadUrl(script);
        }

        System.out.println("script is"+script);
        */


    }
    class Getter

    {
        @android.webkit.JavascriptInterface
        public void setIds(String[] s)
        {
            arr=s;
            System.out.println("Got ids"+s.toString());
            for(String sin : s)
            {
                System.out.println(sin);
                /*if(sin==null||sin.isEmpty())
                    continue;*/
                idNames.add(sin);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    cad = new CustomArrayAdapter(c, arr);
                    System.out.println("setting adapter");
                    lv.setAdapter(cad);



                }
            });




        }


    }
}
