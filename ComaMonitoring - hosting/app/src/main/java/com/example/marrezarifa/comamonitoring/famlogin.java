package com.example.marrezarifa.comamonitoring;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class famlogin extends AppCompatActivity {

    private EditText txidpas, txpasspas;
    private Button btn_loginfam;
    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/famLogin.php";
    public static String id_pasien = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famlogin);

        txidpas = (EditText)findViewById(R.id.txidpas);
        txpasspas = (EditText)findViewById(R.id.txpasspas);
        btn_loginfam = (Button) findViewById(R.id.btn_loginfam);

        btn_loginfam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = txidpas.getText().toString();
                final String pwd = txpasspas.getText().toString();
                loginfam(id,pwd);
            }
        });

    }

    private void loginfam(final String id, final String pwd){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONObject data = new JSONObject(s);

                    if (data.getString("error").equals("false")){
                        id_pasien = id;
                     //   Intent i = new Intent(famlogin.this, menukeluarga.class);
                     //   startActivity(i);

                        //getting the user from the response
                        JSONObject userJson = data.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("id_pasien"),
                                userJson.getString("passpas")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), menukeluarga.class));

                    }else{
                        Toast.makeText(getApplicationContext(), "Id Pasien atau Password salah!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Some errors occured", Toast.LENGTH_SHORT).show();
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
                param.put("passpas", pwd);
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
