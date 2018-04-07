package com.example.marrezarifa.comamonitoring;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class rslogin extends AppCompatActivity {

    public static String id_rs = "";
    private EditText idrs, pasrs;
    private Button btn_loginrs;
    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/rsLogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rslogin);

        idrs = (EditText)findViewById(R.id.idrs);
        pasrs = (EditText)findViewById(R.id.pasrs);
        btn_loginrs = (Button) findViewById(R.id.btn_loginrs);

        btn_loginrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idrs.getText().toString();
                final String pwd = pasrs.getText().toString();
                loginrs(id,pwd);
            }
        });
    }

    private void loginrs(final String id, final String pwd){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);

                    if (data.getString("error").equals("false")){
                        id_rs = id;

                        Intent i = new Intent(rslogin.this, menupetugas.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Id Hospital atau Password salah!", Toast.LENGTH_SHORT).show();
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
                param.put("id_rs", id);
                param.put("passrs", pwd);
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
