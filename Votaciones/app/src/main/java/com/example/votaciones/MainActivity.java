package com.example.votaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tblMenu;
    TabItem tabCandidato, tabVotante, tabVotar, tabCierre;
    ViewPager viewpager;
    Context ctx;
    PagerController pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectar();

        pagerAdapter = new PagerController(getSupportFragmentManager(), tblMenu.getTabCount());
        viewpager.setAdapter(pagerAdapter);
        tblMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 2) {
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 3) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tblMenu));
    }

    private void conectar() {
        tblMenu = findViewById(R.id.tblMenu);
        tabCandidato = findViewById(R.id.tabCandidato);
        tabVotante = findViewById(R.id.tabVotante);
        tabVotar = findViewById(R.id.tabVotar);
        tabCierre = findViewById(R.id.tabCierre);
        viewpager = findViewById(R.id.viewpager);
    }

}
