package com.example.admin.statusmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class UniAdder extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_adder);
    }


    public void submitData(View v)
    {

        String link = ((EditText)findViewById(R.id.link)).getText().toString();
        String uniName = ((EditText)findViewById(R.id.uniName)).getText().toString();
        //DataResource dr = new DataResource(this);
        ArrayList<String> unis = (ArrayList<String>)(this.getIntent()).getSerializableExtra("allNames");
        if(uniName==null||uniName.isEmpty()||unis.contains(uniName.toUpperCase()))
        {
            new AlertDialog.Builder(this)
            .setMessage("Duplicate Name cannot be added")
            .setNeutralButton("Okay",null)
            .show();

        }
        else {
            Intent i = new Intent(this, com.example.admin.statusmonitor.Credentials.class);
            i.putExtra("link", link);
            i.putExtra("uniName", uniName);
            this.startActivity(i);
        }
    }

}

