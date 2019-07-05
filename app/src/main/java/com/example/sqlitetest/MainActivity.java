package com.example.sqlitetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CommonSqlHelper sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyPermission();
        sql = new CommonSqlHelper(MainActivity.this, "TJ_ZCXT", null, 2);
        sql.getWritableDatabase();
        Button button = findViewById(R.id.create_sql);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //调用相机权限
    String[] allPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean needApply = false;
            for (int i = 0; i < allPermissions.length; i++) {
                int chechpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                        allPermissions[i]);
                if (chechpermission != PackageManager.PERMISSION_GRANTED) {
                    needApply = true;
                }
            }
            if (needApply) {
                ActivityCompat.requestPermissions(this, allPermissions, 1);
            }
        }
    }

}
