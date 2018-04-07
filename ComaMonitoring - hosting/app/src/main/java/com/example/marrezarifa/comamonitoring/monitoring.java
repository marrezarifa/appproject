package com.example.marrezarifa.comamonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class monitoring extends AppCompatActivity {

    private Button btn_start, btn_stop;
    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/startAlat.php";
    private String host1 = "http://comamonitoring.xyz/coma/stopAlat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id_alat = detilpasien.id_alat;
                final String id_pasien = listpasien.id_pasien_g;
                startmonitoring(id_pasien, id_alat);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id_alat = detilpasien.id_alat;
                final String id_pasien = listpasien.id_pasien_g;
                stopmonitoring(id_pasien,id_alat);
            }
        });

    }

    private void startmonitoring (final String id_pasien, final String id_alat){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    if (data.getString("error").equals("false")){
                        Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG). show();
                        Intent i = new Intent(monitoring.this, detilpasien.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Start monitoring: FAILED.\nPlease try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                    Log.e("RequestLogin",e.getMessage());
                }
                showDialog(false, "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                showDialog(false, "");
                Log.e("RequestLogin",error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_alat", id_alat);
                param.put("id_pasien", id_pasien);
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    private void stopmonitoring (final String id_pasien,final String id_alat){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    if (data.getString("error").equals("false")){
                        Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_LONG). show();
                        Intent i = new Intent(monitoring.this, detilpasien.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Stop monitoring: FAILED.\nPlease try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                    Log.e("RequestLogin",e.getMessage());
                }
                showDialog(false, "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Kesalahan dalam koneksi\nPastikan perangkat telah terkoneksi dengan benar!", Toast.LENGTH_SHORT).show();
                showDialog(false, "");
                Log.e("RequestLogin",error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_alat", id_alat);
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
}
