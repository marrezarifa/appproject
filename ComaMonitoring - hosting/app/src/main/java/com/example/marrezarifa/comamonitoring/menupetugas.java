package com.example.marrezarifa.comamonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class menupetugas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menupetugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
    }

    public void inpasien (View view) {
        Intent intent = new Intent(menupetugas.this, inputpasien.class);
        startActivity(intent);
    }

    public void daftarpasien (View view) {
        Intent intent = new Intent(menupetugas.this, listpasien.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i("hhh", ""+id);

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_profil_rs) {
            Intent intent = new Intent(menupetugas.this, detilrs.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
