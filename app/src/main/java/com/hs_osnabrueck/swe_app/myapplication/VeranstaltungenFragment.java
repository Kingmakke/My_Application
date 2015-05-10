package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class VeranstaltungenFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private LayoutInflater inflater;
    private ViewGroup container;

    public VeranstaltungenFragment() {}

    public void initVeranstaltung(){
        rootView =  inflater.inflate(R.layout.fragment_veranstaltungen, container, false);

        MyArrayAdapter listAdapter = new MyArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1);
        final List<Integer> veranstaltungsitems = new ArrayList<>();
        final List<Integer> dateitems = new ArrayList<>();
        dateitems.add(0);

        for(int i = 0; i < main.getVeranstaltungsliste().size(); i++){
            if(!listAdapter.isEmpty()){
                if(listAdapter.getItem(dateitems.size() - 1).compareTo(main.getVeranstaltungsliste().elementAt(i).getDate()) != 0){
                    listAdapter.addDate(main.getVeranstaltungsliste().elementAt(i).getDate());
                }
            }else{
                listAdapter.addDate(main.getVeranstaltungsliste().elementAt(i).getDate());
                dateitems.add(listAdapter.getCount()-1);
            }
            listAdapter.addVeranstaltung(main.getVeranstaltungsliste().elementAt(i).getName(),
                    main.getVeranstaltungsliste().elementAt(i).getDescription());
            veranstaltungsitems.add(listAdapter.getCount()-1);
        }
        ListView veranstaltungsListView = (ListView)rootView.findViewById(R.id.veranstaltungsscreen_veranstaltungsliste);
        veranstaltungsListView.setAdapter(listAdapter);
        veranstaltungsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {

                if (veranstaltungsitems.contains(pos)) {
                    //TODO
                    //zu Veranstaltungsdeteils wechseln?
                }
            }

        });
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.inflater = inflater;
        this.container = container;

        initVeranstaltung();
        return rootView;
    }
}
