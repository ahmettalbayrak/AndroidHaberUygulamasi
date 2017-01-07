package com.example.erp_sabah.multirss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SecimActivity extends AppCompatActivity
{

    CheckBox cbDH, cbMilliyet;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secim);

        cbDH = (CheckBox) findViewById(R.id.cbDH);
        cbMilliyet = (CheckBox) findViewById(R.id.cbMilliyet);

        cbDH.setChecked(MainActivity.enableDH);
        cbMilliyet.setChecked(MainActivity.enableMilliyet);

        cbDH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                MainActivity.enableDH = b;
            }
        });

        cbMilliyet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                MainActivity.enableMilliyet = b;
            }
        });
    }
}
