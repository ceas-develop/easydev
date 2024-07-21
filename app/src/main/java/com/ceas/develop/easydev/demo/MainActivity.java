package com.ceas.develop.easydev.demo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toast(String to) {
        Toast.makeText(this, to, Toast.LENGTH_SHORT).show();
    }

}