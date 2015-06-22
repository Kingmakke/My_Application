package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hs_osnabrueck.swe_app.myapplication.adapter.MyArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private LayoutInflater inflater;
    private ViewGroup container;

    public EventFragment() {}

    public void initEvent(){
        rootView =  inflater.inflate(R.layout.fragment_veranstaltungen, container, false);

        MyArrayAdapter listAdapter = new MyArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1);
        final List<Integer> eventitems = new ArrayList<>();
        final List<Integer> dateitems = new ArrayList<>();

        for(int i = 0; i < main.getEventliste().size(); i++){
            if(!listAdapter.isEmpty()){
                if(listAdapter.getItem(dateitems.get(dateitems.size() - 1)).compareTo(main.getEventliste().elementAt(i).getDate()) != 0){
                    listAdapter.addDate(main.getEventliste().elementAt(i).getDate());
                }
            }else{
                listAdapter.addDate(main.getEventliste().elementAt(i).getDate());
                dateitems.add(0);
            }
            listAdapter.addVeranstaltung(main.getEventliste().elementAt(i).getName(),
                    main.getEventliste().elementAt(i).getDescription());
            eventitems.add(listAdapter.getCount() - 1);
        }
        ListView veranstaltungsListView = (ListView)rootView.findViewById(R.id.veranstaltungsscreen_veranstaltungsliste);
        veranstaltungsListView.setAdapter(listAdapter);
        veranstaltungsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                Log.e("pos", String.valueOf(pos));
                if (eventitems.contains(pos)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", main.getEventliste().elementAt(eventitems.indexOf(pos)).getTitle());
                    bundle.putString("date", main.getEventliste().elementAt(eventitems.indexOf(pos)).getDate());
                    bundle.putString("location",main.getEventliste().elementAt(eventitems.indexOf(pos)).getDescription());
                    bundle.putString("description", main.getEventliste().elementAt(eventitems.indexOf(pos)).getContent());
                    bundle.putInt("pos", 2);
                    Fragment fragment = new EventDetailsFragment();
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    main.restoreActionBar(getString(R.string.title_section10));
                    fragmentTransaction.commit();

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

        main.setPos(2);

        initEvent();
        return rootView;
    }
}
