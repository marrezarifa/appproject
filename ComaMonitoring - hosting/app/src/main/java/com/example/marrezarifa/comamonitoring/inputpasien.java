package com.example.marrezarifa.comamonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class inputpasien extends AppCompatActivity implements View.OnClickListener {


    private EditText txidalat, txnamapas, txumurpas, txtglmasuk, txidpas, txpasspasien, txidrs;
    private Button btnsubmit;
    private ProgressDialog progressDialog;
    private String host = "http://comamonitoring.xyz/coma/registerPasien.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputpasien);

        txidalat = (EditText) findViewById(R.id.txidalat);
        txnamapas = (EditText) findViewById(R.id.txnamapas);
        txumurpas = (EditText) findViewById(R.id.txumurpas);
        txtglmasuk = (EditText) findViewById(R.id.txtglmasuk);
        txidpas = (EditText) findViewById(R.id.txidpas);
        txpasspasien = (EditText) findViewById(R.id.txpasspasien);
        txidrs = (EditText) findViewById(R.id.txidrs);

        btnsubmit = (Button) findViewById(R.id.btnsubmit);

        progressDialog = new ProgressDialog(this);

        btnsubmit.setOnClickListener(this);
    }

    private void registerPasien(){
        final String id_alat = txidalat.getText().toString().trim();
        final String namapas = txnamapas.getText().toString().trim();
        final String umur = txumurpas.getText().toString().trim();
        final String tglmasuk = txtglmasuk.getText().toString().trim();
        final String id_pasien = txidpas.getText().toString().trim();
        final String passpas = txpasspasien.getText().toString().trim();
        final String id_rs = txidrs.getText().toString().trim();

        progressDialog.setMessage("Submitting patient...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG). show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_alat", id_alat);
                params.put("namapas", namapas);
                params.put("umur", umur);
                params.put("tglmasuk", tglmasuk);
                params.put("id_pasien", id_pasien);
                params.put("passpas", passpas);
                params.put("id_rs", id_rs);
                return params;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if(view == btnsubmit)
            registerPasien();
    }

}