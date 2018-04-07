package com.example.marrezarifa.comamonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class detilrs extends AppCompatActivity {

    private String id_rs = rslogin.id_rs;
    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/detilrs.php";
    private TextView detilnamars, detilidrs,detilpasrs,detilprov,detilkota,detilalamat,detilemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detilrs);

        detilnamars = (TextView) findViewById(R.id.detilnamars);
        detilidrs = (TextView) findViewById(R.id.detilidrs);
        detilpasrs = (TextView) findViewById(R.id.detilpasrs);
        detilprov = (TextView) findViewById(R.id.detilprov);
        detilkota = (TextView) findViewById(R.id.detilkota);
        detilalamat = (TextView) findViewById(R.id.detilalamat);
        detilemail = (TextView) findViewById(R.id.detilemail);
        detailrs(id_rs);
    }

    private void detailrs(final String id){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);

                    detilnamars.setText(data.getString("namars"));
                    detilidrs.setText(data.getString("id_rs"));
                    detilpasrs.setText(data.getString("passrs"));
                    detilprov.setText(data.getString("provinsi"));
                    detilkota.setText(data.getString("kota"));
                    detilalamat.setText(data.getString("alamatrs"));
                    detilemail.setText(data.getString("email"));
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
                param.put("id_rs", id);
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

    public void logoutrs(View view) {
        Intent myintent = new Intent(detilrs.this, MainActivity.class);
        startActivity(myintent);
    }
}
