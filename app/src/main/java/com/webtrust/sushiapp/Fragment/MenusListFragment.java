package com.webtrust.sushiapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webtrust.sushiapp.R;

/**
 * список элементов данного меню
 */
public class MenusListFragment extends Fragment {


    public MenusListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menus_list, container, false);
    }

}
