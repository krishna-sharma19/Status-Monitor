package com.example.admin.statusmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.admin.statusmonitor.University.displayAll;

public class MainActivity extends AppCompatActivity {
    Context c;

    ArrayList<String> unis;
    String[] arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c=this;
        System.out.println(displayAll(this));
        unis = University.displayAll(this);
        arr = new String[unis.size()];
        int j =0;
        for(String uni : unis) {
            arr[j++] = uni;
        }


        CustomArrayAdapterMain cam = new CustomArrayAdapterMain(this,arr);
        ListView lv = (ListView)findViewById(R.id.lvu);
        lv.setAdapter(cam);

        }




    public void add(View v)
    {
        Intent i =  new Intent(this,UniAdder.class);
        i.putStringArrayListExtra("allNames",unis);
        this.startActivity(i);


    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}


