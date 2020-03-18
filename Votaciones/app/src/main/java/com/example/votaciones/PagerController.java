package com.example.votaciones;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int numftabs;

    public PagerController(FragmentManager fm, int behavior) {
        super(fm);
        this.numftabs = behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Candidatos();
            case 1:
                return new Votantes();
            case 2:
                return new Votar();
            case 3:
                return new Cierre();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numftabs;
    }
}
