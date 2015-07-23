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

/**
 * DrawerAdapter class
 */
public class MyDrawerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    /**
     * DrawerAdapter constructor
     * @param context context
     * @param navItems menu entries
     */
    public MyDrawerAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    /**
     * gives the number of elements in the menu
     * @return number of menu entries
     */
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    /**
     * gives the entry of the given position
     * @param position position of the entry
     * @return the item of the menu at position position
     */
    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    /**
     * gives the item id
     * @param position position of the item
     * @return 0
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * get the view
     * @param position position of the item we want the view of
     * @param convertView
     * @param parent
     * @return the view
     */
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
