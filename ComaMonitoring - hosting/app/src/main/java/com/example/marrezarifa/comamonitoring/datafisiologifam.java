package com.example.marrezarifa.comamonitoring;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class datafisiologifam extends AppCompatActivity {


    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/monitoring.php";
    private TextView idbrpm, ketidbrpm, idbtpm, ketidbtpm, ketidmata;
    private String id_pasien = famlogin.id_pasien;
 //   private SwipeRefreshLayout swlayout;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datafisiologi);

        this.mHandler = new Handler();
        //m_Runnable.run();

        idbrpm = (TextView)findViewById(R.id.idbrpm);
        ketidbrpm = (TextView)findViewById(R.id.ketidbrpm);
        idbtpm = (TextView)findViewById(R.id.idbtpm);
        ketidbtpm = (TextView)findViewById(R.id.ketidbtpm);
        ketidmata = (TextView)findViewById(R.id.ketidmata);

//        swlayout = (SwipeRefreshLayout)findViewById(R.id.swlayout);

        // Mengeset properti warna yang berputar pada SwipeRefreshLayout
//        swlayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

 //       swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
  //          @Override
   //         public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
     //           new Handler().postDelayed(new Runnable() {
       //             @Override public void run() {

                        // Berhenti berputar/refreshing
         //               swlayout.setRefreshing(false);
                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
           //             final String id_pasien = famlogin.id_pasien;
             //           monitoring(id_pasien);
  //                  }
      //          }, 1000);
    //        }
    //    });

        final String id_pasien = famlogin.id_pasien;
        monitoring(id_pasien);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //m_Runnable.run();
        mHandler.postDelayed(m_Runnable,10000);
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(datafisiologifam.this,"reload!",Toast.LENGTH_SHORT).show();
            final String id_pasien = famlogin.id_pasien;
            monitoring(id_pasien);
        }

    };

    private void monitoring(final String id_pasien){
        //showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    idbrpm.setText(data.getString("brpm"));
                    ketidbrpm.setText(data.getString("stat_jantung"));
                    idbtpm.setText(data.getString("btpm"));
                    ketidbtpm.setText(data.getString("stat_napas"));
                    ketidmata.setText(data.getString("stat_mata"));

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
                //showDialog(false, "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                //showDialog(false, "");
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

    private void showDialog(boolean show,String message){
        if(pdDialogl == null){
            pdDialogl = new ProgressDialog(this);
        }
        pdDialogl.setMessage(message);
        pdDialogl.setCancelable(false);
        if(show) pdDialogl.show();
        else pdDialogl.dismiss();
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(m_Runnable);
        super.onBackPressed();
        this.finish();
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
