package com.yollpoll.hotfix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HotFix.fixDexFile(this,"");
    }

    int a = 0;

    public void test(View view) {
        if (a == 0) {
            Toast.makeText(this, "a=0,出现了bug", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "a=" + a + ",修复了bug", Toast.LENGTH_SHORT).show();
        }
    }
}