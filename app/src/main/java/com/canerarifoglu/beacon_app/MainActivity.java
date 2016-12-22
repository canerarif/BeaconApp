package com.canerarifoglu.beacon_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    Button bt;
    TextView tv;
    ProgressBar progressBar;
    static final String API_KEY = "3w4[WX½[(b{(91])08u%P41jé6%8pq6'5ayTsR8rbi%fsé0aPgvda7(<½VLulQhZzKLiD2ZjACo!|W]AFptJ½cm<nzZWGOFLBpéizRWqHMdXSEJ1eLo$0YeW1N½icCc(";
    static final String API_URL = "http://loc.deepram.com/api/Beacon/Search";
    public static String uuid="";
    public static String res="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt=(Button) findViewById(R.id.Button);
        tv=(TextView) findViewById(R.id.textview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        final BluetoothAdapter adaptor = BluetoothAdapter.getDefaultAdapter();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adaptor!=null){
                    if(adaptor.isEnabled()){

                        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.baglan5, null);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        bt.setCompoundDrawables(null,drawable,null,null);
                        tv.setText("Masaya Bağlanmak İçin Dokunun");

                        beaconManager=new BeaconManager(getApplicationContext());
                        beaconManager.setBackgroundScanPeriod(1000,500);
                        beaconManager.setForegroundScanPeriod(1000,500);
                        try {
                            Region region1 = new Region("monitored region",null, null, null);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Region tanımlama hatası",Toast.LENGTH_SHORT).show();
                        }

                        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onEnteredRegion(Region region, List<Beacon> list) {
                                progressBar.setVisibility(View.VISIBLE);
                                uuid=list.iterator().next().getProximityUUID().toString();

                                try {
                                    new RetrieveFeedTask().execute();
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"Fonksiyon çalıştırma hatası",Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onExitedRegion(Region region) {

                            }
                        });

                        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                            @Override
                            public void onServiceReady() {
                                try{
                                    beaconManager.startMonitoring(new Region("monitored region",

                                            null, null, null));
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"Hata ile karşılaştı",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else{
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.bluetooth1, null);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        bt.setCompoundDrawables(null,drawable,null,null);
                        tv.setText("Lütfen Bluetooth Bağlantınızı Açın");
                    }
                }else {
                    Toast.makeText(getApplication(),"Bluetooth verisine ulaşılamıyor",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        RetrieveFeedTask() throws IOException {
        }

        protected void onPreExecute() {
            tv.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                String request = API_URL+"?id="+uuid;
                int requesLength = request.length();

                URL url = new URL(request);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects( false );
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
                tv.setText(response);
            }
            Log.i("INFO", response);
            // responseView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed



            res = response;

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getString("beacon_uuid").equals("null")) {

                    Intent intent = new Intent(getApplicationContext(),Anasayfa.class);
                    startActivity(intent);

                }else {
                    tv.setText("Kayıtlı Masa Bulunamadı!");
                    progressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            // lessonNameText.setText(lessonName);
            // classNameText.append("" + className);
            // termText.append("" + term);
            // instructorDataText.setText(instructor);
        }


    }
}
