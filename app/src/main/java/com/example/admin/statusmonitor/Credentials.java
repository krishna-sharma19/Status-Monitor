package com.example.admin.statusmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
    private boolean isFirst=true;
    private boolean hasMultipleForms=false;

    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        System.out.println("inside oncreate");
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
                System.out.println("outside crawl url");
            }
        }
            , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error");
                if(!Credentials.isNetworkAvailable(c)) {
                    new AlertDialog.Builder(c)
                            .setMessage("Kindly make sure that your phone is connected to a working network! ")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) c).finish();

                                }
                            })
                            .show();





                }
                else
                {
                    new AlertDialog.Builder(c)
                            .setMessage("Malformed URL! ")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) c).finish();

                                }
                            })
                            .show();
                }

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
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void crawlUrl(String url)
    {
        System.out.println("inside crawlUrl");
         wv = (WebView)findViewById(R.id.wv);
        System.out.println("loading url= "+url);
        wv.loadUrl(url);
        (wv.getSettings()).setJavaScriptEnabled(true);
       // wv.setWebViewClient(new WebViewClient());//this is important else webview is not used and browser opens
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
                View v = findViewById(R.id.imgLoadingGif);
                v.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                WebView wv = (WebView) findViewById(R.id.wv);
                String script="";// = "javascript:";
                script += "form=document.getElementsByTagName('form');"
                        + "var x=0; "
                        + "if(form.length>1){x=window.obj.multipleFormsSelector(form.length);}"
                        + "else{"
                        + "ips=form[x].getElementsByTagName('input');"
                        + "arr=[];"
                        + "for(i=0;i<ips.length;i++) {if(ips[i].type!='hidden'&&ips[i].type!='submit'&&ips[i].type!='button'&&ips[i].type!='radio'&&ips[i].type!='checkbox') arr[i]=ips[i].id;}"
                        + "window.obj.setIds(arr);"
                        + "}"
                ;

                runScriptOnWebView(script);
                System.out.println("after running old dcript");
                if (hasMultipleForms) {
                    System.out.println("running new cript ");

                }
            }
        });
        wv.addJavascriptInterface(new Getter(),"obj");

    }
    public void runScriptOnWebView(String script)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            wv.evaluateJavascript(

                    script,null);
            System.out.println(script);
            System.out.println("eval");


        } else {
            //webView.loadUrl("window.obj.fillDetails()");
            wv.loadUrl("javascript:"+script);
            System.out.println(script);
            System.out.println("loaded");
        }

    }
    class Getter

    {


        @android.webkit.JavascriptInterface
        public void setIds(String[] s) {
            System.out.println("inside setid");
            if(isFirst==false)
            {
                return;
            }
            isFirst=false;

            System.out.println("Got ids" + s.toString());

            for (String sin : s) {
                System.out.println("that"+sin+"this");
                if(sin!=null&&!sin.isEmpty())
                {
                    idNames.add(sin);
                }

            }
            int i =0;
            System.out.println(idNames+"yeye");
            arr = new String[idNames.size()];
            for(String id: idNames)
            {
                arr[i] = id;
                i++;

            }
            System.out.println("outside");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    RelativeLayout pb = (RelativeLayout) findViewById(R.id.lp);
//                    pb.setVisibility(View.GONE);
                    if(arr.length==0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setMessage("There are no fields in the form which can be saved.Do you still want to save this website?");
                        builder.setPositiveButton("YES", null);
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(c,MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        if(!((Activity)c).isFinishing())
                            builder.show();

                    }
                    else {
                        cad = new CustomArrayAdapter(c, arr);
                        System.out.println("setting adapter");
                        lv.setAdapter(cad);
                        Toast.makeText(c, "Leave those fields empty about which you don't have any idea", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        @android.webkit.JavascriptInterface
        public void multipleFormsSelector(final int len)
            {
                hasMultipleForms=true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("Multiple forms detected");
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);

                        builder.setMessage("Multiple forms detected. Kindly enter the form number you would like to fill.Choice is between 1 - "+len+" and if it is not the form you wanted to fill then press back button once");
                        // Set up the input
                        final EditText input = new EditText(c);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String txt = input.getText().toString();

                                int val = Integer.parseInt(txt);
                                val = val - 1;
                                String changedScript = "javascript:"
                                        +"ips=form["+val+"].getElementsByTagName('input');"
                                        +"arr=[];"
                                        +"for(i=0;i<ips.length;i++) {if(ips[i].type!='hidden'&&ips[i].type!='submit'" +
                                        //"&&ips[i].type!='button'&&ips[i].type!='radio'&&ips[i].type!='checkbox'" +
                                        ") arr[i]=ips[i].id;}"
                                        +"window.obj.setIds(arr);";

                                System.out.println("running new cript "+changedScript);
                                runScriptOnWebView(changedScript);



                            }
                        });

                    if(!((Activity)c).isFinishing())
                        builder.show();

                    }
                });
                System.out.println("outside assigning");
                //return Integer.parseInt(userChoice)-1;
            }




    }


    }

