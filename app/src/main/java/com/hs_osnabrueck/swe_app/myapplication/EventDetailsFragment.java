package com.hs_osnabrueck.swe_app.myapplication;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 */
public class EventDetailsFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private TextView title, date, location, description;
    private Bundle bundle;
    private View divider;

    public EventDetailsFragment() {}

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_event_details, container, false);

        setHasOptionsMenu(true);

        bundle = getArguments();

        main.setPos(8);
        main.setPos_old(bundle.getInt("pos"));

        title = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.event_details_title);
        title.setText(bundle.getString("title"));

        divider = (View)rootView.findViewById(R.id.event_details_divider);
        if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[0])){
            divider.setBackground(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal)));
        }else if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[1])){
            divider.setBackground(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_uni)));
        }else{
            divider.setBackground(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_int)));
        }

        date = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.event_details_date);
        date.setText(bundle.getString("date"));

        location = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.event_details_location);
        location.setText(bundle.getString("location"));

        description = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.event_details_description);
        description.setText(Html.fromHtml(bundle.getString("description")));

        return rootView;
    }

    /**
     *
     * @param activity
     */
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    /**
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.menu.event, menu);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.hs_osnabrueck.swe_app.myapplication.R.id.menu_event_navigate:
                /*
                double latitude = 0;
                double longitude = 0;
                String label = "";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
                */
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
