package com.example.admin.statusmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Created by admin on 30/03/17.
 */

public class CustomArrayAdapterMain extends ArrayAdapter{
    private String[] uniNames;
    private Context c;
    private int[] imgIds;

    public CustomArrayAdapterMain(Context context, String[] uniNames)
    {
        super(context, R.layout.showuni,uniNames);
        c = context;
        this.uniNames = uniNames;
        imgIds = new int[]{R.drawable.excited1,R.drawable.excited2,R.drawable.excited3,R.drawable.excited4,R.drawable.excited5,R.drawable.excited6,R.drawable.excited7,R.drawable.excited8,R.drawable.excited9,R.drawable.excited10};

    }
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.showuni, parent, false);
        }
        Button btn = ((Button)convertView.findViewById(R.id.btn));

        btn.setText(uniNames[pos]);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, WebFill.class);
                University uniDet = new University(c).displayDetail(uniNames[pos]);
                    /*uniDet.open();
                    uniDet.deleteAll();
                    uniDet.close();*/
                i.putExtra("flag","directed");
                //i.putExtra("hmNameVR^^ues",uniDet.getHmfieldNamesValues());

                i.putExtra("hmIdValues",uniDet.getHmIdValues());
                System.out.println("hm received is"+uniDet.getHmIdValues());
                i.putStringArrayListExtra("idNames",uniDet.getNames());
                //i.putStringArrayListExtra("")
                i.putExtra("imgId",imgIds[pos%10]);
                i.putExtra("link",uniDet.getUniLink());
                c.startActivity(i);

            }
        });

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("long clicked");
                new AlertDialog.Builder(c)
                        .setMessage("Are you sure you want to delete this university?")
                        .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                University uniObj = new University(c, uniNames[pos], null, null, null);
                                uniObj.open();
                                uniObj.deleteUni(uniNames[pos]);
                                uniObj.close();
                                Intent i = ((Activity) c).getIntent();
                                ((Activity) c).finish();
                                c.startActivity(i);

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });



        View img = convertView.findViewById(R.id.img);
//        if(img!=null)
        img.setBackgroundResource(imgIds[pos%10]);
//        else
//            System.out.println("NULL hai");
                return convertView;
    }


    }
