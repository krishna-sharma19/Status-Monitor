package com.example.admin.statusmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static com.example.admin.statusmonitor.University.displayAll;

public class MainActivity extends AppCompatActivity {
    Context c;
    ArrayList<String> unis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c=this;
        System.out.println(displayAll(this));
        unis = University.displayAll(this);
        LinearLayout rl = (LinearLayout)findViewById(R.id.ll);
        for (final String uni : unis)
        {
            Button clickUni = new Button(c);
            rl.addView(clickUni);
            clickUni.setText(uni);
            clickUni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c, WebFill.class);
                    University uniDet = new University(c).displayDetail(uni);
                    i.putExtra("flag","directed");
                    //i.putExtra("hmNameValues",uniDet.getHmfieldNamesValues());

                    i.putExtra("hmIdValues",uniDet.getHmIdValues());
                    System.out.println("hm received is"+uniDet.getHmIdValues());
                    i.putStringArrayListExtra("idNames",uniDet.getNames());
                    //i.putStringArrayListExtra("")
                    i.putExtra("link",uniDet.getUniLink());
                    startActivity(i);

                }
            });
        }

    }

    public void add(View v)
    {
        Intent i =  new Intent(this,UniAdder.class);
        i.putStringArrayListExtra("allNames",unis);
        this.startActivity(i);


    }
}


