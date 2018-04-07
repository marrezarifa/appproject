package com.example.marrezarifa.comamonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class listpasien extends AppCompatActivity {
    ListView listView1;
    private ProgressDialog pdDialogl;
    private String host = "http://comamonitoring.xyz/coma/listPasien.php";
    ArrayList<String> id_pasien = new ArrayList<String>();
    public static String id_pasien_g = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpasien);

        listView1 = (ListView) findViewById(R.id.listView1);
        get_list_pasien();
    }

    private void get_list_pasien(){
        showDialog(true,"Loading...");
        StringRequest req = new StringRequest(Request.Method.POST, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("RequestLogin",s);
                try {
                    JSONArray data = new JSONArray(s);
                    ArrayList data_json = new ArrayList();

                    for(int i =0; i<data.length(); i++){
                        JSONObject tmp_data = new JSONObject(data.getString(i));
                        data_json.add(tmp_data.getString("id_pasien") + "\n" + tmp_data.getString("namapas"));
                        id_pasien.add(tmp_data.getString("id_pasien"));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, data_json);
                    listView1.setAdapter(adapter);
                    listView1.setOnItemClickListener(lvClick);
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
                param.put("action", "list");
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    private AdapterView.OnItemClickListener lvClick = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            id_pasien_g = id_pasien.get(position);

            Intent i = new Intent(listpasien.this, detilpasien.class);
            startActivity(i);
            //Toast.makeText(getApplicationContext(),"Posisi: "+ id_pasien.get(position), Toast.LENGTH_SHORT).show();
        }
    };

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
