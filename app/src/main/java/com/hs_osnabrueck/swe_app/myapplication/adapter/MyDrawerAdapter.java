package com.hs_osnabrueck.swe_app.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.common.NavItem;
import com.hs_osnabrueck.swe_app.myapplication.R;

import java.util.ArrayList;

public class MyDrawerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    public MyDrawerAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.drawer_item_title);
        ImageView iconView = (ImageView) view.findViewById(R.id.drawer_item_icon);

        titleView.setText( mNavItems.get(position).getmTitle() );
        iconView.setImageResource(mNavItems.get(position).getmIcon());

        return view;
    }
}
