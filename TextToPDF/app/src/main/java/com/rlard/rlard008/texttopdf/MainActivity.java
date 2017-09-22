package com.rlard.rlard008.texttopdf;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.container);

//        SelfNoteFragment noteFragment = SelfNoteFragment.newInstance();
//
//        android.support.v4.app.FragmentTransaction fragTransaction= this.getSupportFragmentManager().beginTransaction();
//        fragTransaction.replace(R.id.container, noteFragment);
//        fragTransaction.commit();

        SelfNoteFragment frg=new SelfNoteFragment();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container,frg);
        ft.commit();

    }
}
