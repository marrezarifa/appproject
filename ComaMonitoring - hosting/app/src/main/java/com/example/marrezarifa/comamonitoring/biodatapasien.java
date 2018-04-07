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

public class biodatapasien extends AppCompatActivity {

    private ProgressDialog pdDialogl;
    private String id_pasien = famlogin.id_pasien;
    private String host = "http://comamonitoring.xyz/coma/detilPasien.php";
    private TextView namapasien, detilidalat, detilumur, detiltgl, detilidpas;
    private Button btn_logoutfam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodatapasien);

        namapasien = (TextView)findViewById(R.id.namapasien);
        detilidalat = (TextView)findViewById(R.id.detilidalat);
        detilumur = (TextView)findViewById(R.id.detilumur);
        detiltgl = (TextView)findViewById(R.id.detiltgl);
        detilidpas = (TextView)findViewById(R.id.detilidpas);

        btn_logoutfam = (Button)findViewById(R.id.btn_logoutfam) ;

        findViewById(R.id.btn_logoutfam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        biodatapasien(id_pasien);
    }

    public void fisiopasien1 (View view) {
        Intent myintent = new Intent(biodatapasien.this, datafisiologifam.class);
        startActivity(myintent);
    }

    private void biodatapasien(final String id){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);
                    namapasien.setText(data.getString("namapas"));
                    detilidalat.setText(data.getString("id_alat"));
                    detilumur.setText(data.getString("umur"));
                    detiltgl.setText(data.getString("tglmasuk"));
                    detilidpas.setText(data.getString("id_pasien"));
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
                param.put("id_pasien", id);
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
