package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BuildingsFragment extends Fragment {

    private View rootView;
    private MainActivity main;

    public BuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_veranstaltungen, container, false);

        setHasOptionsMenu(true);
        main.setPos(3);

        ArrayAdapter listAdapter = new ArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1);
        for(int i = 0; i < main.getPoiliste().size(); i++){
            listAdapter.add(main.getPoiliste().get(i).getName());
        }
        ListView gebaeudeListView = (ListView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.veranstaltungsscreen_veranstaltungsliste);
        gebaeudeListView.setAdapter(listAdapter);
        gebaeudeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude", main.getPoiliste().get(position).getGps_longitude());
                bundle.putDouble("latitude", main.getPoiliste().get(position).getGps_latitude());

                Fragment fragment = new KarteFragment();
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, fragment, "1");
                main.restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Kartenscreen));
                fragmentTransaction.commit();
            }
        });


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.menu.buildings, menu);
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}
