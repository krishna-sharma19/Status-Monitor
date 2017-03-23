package com.example.admin.statusmonitor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by admin on 09/03/17.
 */

/**
 * Created by admin on 20/01/17.
 */

public class CustomArrayAdapter extends ArrayAdapter {
    private Context c;
    private String[] name;
    private HashMap<String,String> hmvalues=new HashMap<>();



    public CustomArrayAdapter(Context context,String[] fieldNames)
    {
        super(context, R.layout.layout,fieldNames);
        c = context;
        name = fieldNames;
        System.out.println("names received"+name);


    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout,parent,false);
        }
        final TextView tv = ((TextView)convertView.findViewById(R.id.tv));
        tv.setText(name[pos]);
        System.out.println("Setting text"+name[pos]);
        final EditText ed = ((EditText)convertView.findViewById(R.id.ed));

        TextWatcher tw = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hmvalues.put(tv.getText().toString(),ed.getText().toString());
                System.out.println("-------------hm updated-----------"+hmvalues);

            }
        };
        ed.addTextChangedListener(tw);
        return convertView;

    }
    public HashMap<String,String>getHashMapValues()
    {
        return hmvalues;

    }

}


