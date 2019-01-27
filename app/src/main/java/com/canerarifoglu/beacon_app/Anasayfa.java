package com.canerarifoglu.beacon_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Anasayfa extends AppCompatActivity {

    TextView tv,tv1,tv2,tv3;
    Spinner sp,sp1,sp2;
    ImageView iv;

    String response=MainActivity.res,foto="";
    //String uuid = "19D7FCF8-C802-4D2B-B1CD-5454A8672671";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        tv=(TextView)findViewById(R.id.textview);
        tv1=(TextView)findViewById(R.id.textview1);
        tv2=(TextView)findViewById(R.id.textview2);
        tv3=(TextView)findViewById(R.id.textview3);

        sp=(Spinner) findViewById(R.id.spinner);   
        sp1=(Spinner) findViewById(R.id.spinner1);
        sp2=(Spinner) findViewById(R.id.spinner2);

        iv=(ImageView)findViewById(R.id.imageview1);

        String subeadi="";
        JSONArray menu, urun;
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.getString("beacon_uuid").equals("null")) {
                subeadi = jsonObject.getString("sube_adi");
                foto=jsonObject.getString("sube_foto");
                menu = jsonObject.getJSONArray("menu");
                for (int i = 0; i < menu.length(); i++) {
                    JSONObject c = menu.getJSONObject(i);

                    String kategoriadi = c.getString("kategori_adi").toString();
                    if (c.getString("kategori_adi").equals("Yemekler")) {
                        tv1.setText(c.getString("kategori_adi"));

                        urun = c.getJSONArray("menu");
                        ArrayAdapter<String> adapter;
                        List<String> list = new ArrayList<String>();
                        for (int k = 0; k < urun.length(); k++) {
                            JSONObject b = urun.getJSONObject(k);
                            String adi = b.getString("adi");
                            String fiyat = b.getString("birim_fiyat");

                            list.add(adi + " -- " + fiyat + " TL");
                        }

                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp.setAdapter(adapter);
                    } else if (c.getString("kategori_adi").equals("Ara Sıcaklar")) {
                        tv2.setText(c.getString("kategori_adi"));
                        urun = c.getJSONArray("menu");
                        ArrayAdapter<String> adapter;
                        List<String> list = new ArrayList<String>();

                        for (int k = 0; k < urun.length(); k++) {
                            JSONObject b = urun.getJSONObject(k);
                            String adi = b.getString("adi");
                            String fiyat = b.getString("birim_fiyat");

                            list.add(adi + " -- " + fiyat + " TL");
                        }

                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp1.setAdapter(adapter);
                    } else if (kategoriadi.equals("İçecekler")) {
                        tv3.setText(c.getString("kategori_adi"));
                        urun = c.getJSONArray("menu");
                        ArrayAdapter<String> adapter;
                        List<String> list = new ArrayList<String>();
                        for (int k = 0; k < urun.length(); k++) {
                            JSONObject b = urun.getJSONObject(k);
                            String adi = b.getString("adi");
                            String fiyat = b.getString("birim_fiyat");

                            list.add(adi + " -- " + fiyat + " TL");
                        }

                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter);
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(),"Beacona Ulaşılamadı",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv.setText(subeadi);
        //Bitmap bitmap = DownloadImage(foto);
        //iv.setImageBitmap(bitmap);

    }
}
