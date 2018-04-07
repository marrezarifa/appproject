package com.example.marrezarifa.comamonitoring;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class menukeluarga extends AppCompatActivity {

    private String host = "http://comamonitoring.xyz/coma/monitoring.php";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menukeluarga);

        this.mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHandler.postDelayed(m_Runnable,10000);
    }

    public void biopasien(View view) {
        Intent intent = new Intent(menukeluarga.this, biodatapasien.class);
        startActivity(intent);
    }
    public void fisiopasien(View view) {
        mHandler.removeCallbacks(m_Runnable);
        Intent intent = new Intent(menukeluarga.this, datafisiologifam.class);
        startActivity(intent);
        finish();
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            try{
                Toast.makeText(menukeluarga.this,"reload.",Toast.LENGTH_SHORT).show();
                Log.e("k","reload");
                final String id_pasien = famlogin.id_pasien;
                monitoring(id_pasien);
                menukeluarga.this.mHandler.postDelayed(m_Runnable,10000);
            }catch (Exception ex){

            }

        }

    };

    private void monitoring(final String id_pasien){

        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    data.getString("brpm");
                    data.getString("stat_jantung");
                    data.getString("btpm");
                    data.getString("stat_napas");
                    data.getString("stat_mata");

                    if (data.getString("stat_jantung").equalsIgnoreCase("tidak normal"))
                        create_notif("Coma Monitoring", "Detak jantung pasien tdk normal");
                    else if (data.getString("stat_napas").equalsIgnoreCase("tidak normal"))
                        create_notif("Coma Monitoring", "Napas pasien tdk normal");
                    else if(data.getString("stat_mata").equalsIgnoreCase("Terbuka"))
                        create_notif("Coma Monitoring", "Mata pasien terbuka");
                    else if (data.getString("stat_jantung").equalsIgnoreCase("tidak normal") &&
                            data.getString("stat_napas").equalsIgnoreCase("tidak normal"))
                        create_notif("Coma Monitoring", "Terjadi anomali data fisiologi");
                    else if (data.getString("stat_jantung").equalsIgnoreCase("tidak normal") &&
                            data.getString("stat_mata").equalsIgnoreCase("Terbuka"))
                        create_notif("Coma Monitoring", "Terjadi anomali data fisiologi");
                    else if (data.getString("stat_napas").equalsIgnoreCase("tidak normal") &&
                            data.getString("stat_mata").equalsIgnoreCase("Terbuka"))
                        create_notif("Coma Monitoring", "Terjadi anomali data fisiologi");
                    else if (data.getString("stat_jantung").equalsIgnoreCase("tidak normal") &&
                            data.getString("stat_mata").equalsIgnoreCase("Terbuka") &&
                            data.getString("stat_napas").equalsIgnoreCase("tidak normal"))
                        create_notif("Coma Monitoring", "Terjadi anomali data fisiologi");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                    Log.e("RequestLogin",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();

                Log.e("RequestLogin",error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_pasien", id_pasien);
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    public void create_notif(String title, String content){

        Intent intent = new Intent(this, datafisiologifam.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);


        // Build notification
        // Actions are just fake
        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle(title).setContentIntent(pIntent)
                .setContentText(content)
                .setSmallIcon(R.drawable.bsm)
                .setAutoCancel(false)
                .setSound(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.notif))
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        //noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_LIGHTS;
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
        startActivity(intent);

    }

}
