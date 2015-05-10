package com.hs_osnabrueck.swe_app.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<String> {

    private static final int TYPE_VERANSTALTUNG = 0;
    private static final int TYPE_DATE = 1;

    private View v;
    private Boolean date;
    private LayoutInflater inflater;
    private ArrayList<DoubleString> mData = new ArrayList<DoubleString>();
    final List<Integer> datePos = new ArrayList<Integer>();

    public MyArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDate(String item){
        mData.add(new DoubleString(item, ""));
        datePos.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    public void addVeranstaltung(String item1, String item2){
        mData.add(new DoubleString(item1, item2));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position).getFirst() + mData.get(position).getSecond();
    }

    @Override
    public int getItemViewType(int position) {
        return datePos.contains(position) ? TYPE_DATE : TYPE_VERANSTALTUNG;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        int type = getItemViewType(pos);
        TextView tv1, tv2;
        if (convertView == null) {
            switch (type) {
                case TYPE_VERANSTALTUNG:
                    convertView = inflater.inflate(R.layout.veranstaltungsliste_veranstaltung, null);
                    tv1 = (TextView)convertView.findViewById(R.id.veranstaltungsliste_veranstaltung1);
                    tv2 = (TextView)convertView.findViewById(R.id.veranstaltungsliste_veranstaltung2);
                    tv1.setText(mData.get(pos).getFirst());
                    tv2.setText(mData.get(pos).getSecond());
                    break;
                case TYPE_DATE:
                    convertView = inflater.inflate(R.layout.veranstaltungsliste_date, null);
                    tv1 = (TextView)convertView.findViewById(R.id.veranstaltungsliste_date);
                    tv1.setText(mData.get(pos).getFirst());
                    break;
            }
        }

        return convertView;
    }

    private class DoubleString{

        String first, second;

        public DoubleString(String first, String second){
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }

}