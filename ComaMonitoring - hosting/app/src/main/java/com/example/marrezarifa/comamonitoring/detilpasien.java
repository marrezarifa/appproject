package com.example.marrezarifa.comamonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class detilpasien extends AppCompatActivity {

    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/detilPasien.php";
    private TextView namapasien, detilidalat, detilnama, detilumur, detiltgl, detilidpas, detilpass;
    public static String id_alat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detilpasien);
        namapasien = (TextView)findViewById(R.id.namapasien);
        detilidalat = (TextView)findViewById(R.id.detilidalat);
        detilnama = (TextView)findViewById(R.id.detilnama);
        detilumur = (TextView)findViewById(R.id.detilumur);
        detiltgl = (TextView)findViewById(R.id.detiltgl);
        detilidpas = (TextView)findViewById(R.id.detilidpas);
        detilpass = (TextView)findViewById(R.id.detilpass);
        final String id_pasien = listpasien.id_pasien_g;

        //final String id_alat = detilidalat.getText().toString();
        detail_pasien(id_pasien);
    }

    public void startstop (View view) {
        Intent intent = new Intent(detilpasien.this, monitoring.class);
        startActivity(intent);
    }

    public void lihatdata (View view) {
        Intent intent = new Intent(detilpasien.this, datafisiologi.class);
        startActivity(intent);
    }

    private void detail_pasien(final String id_pasien){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    namapasien.setText(data.getString("namapas"));
                    detilidalat.setText(data.getString("id_alat"));
                    detilnama.setText(data.getString("namapas"));
                    detilumur.setText(data.getString("umur"));
                    detiltgl.setText(data.getString("tglmasuk"));
                    detilidpas.setText(data.getString("id_pasien"));
                    detilpass.setText(data.getString("passpas"));
                    id_alat = data.getString("id_alat");
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
