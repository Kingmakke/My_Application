package com.hs_osnabrueck.swe_app.myapplication.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<String> {

    private final int TYPE_VERANSTALTUNG = 0;
    private final int TYPE_DATE = 1;

    private int elevation = 5;
    int position = 0;

    private LayoutInflater inflater;
    private ArrayList<DoubleString> mData = new ArrayList<>();
    private ArrayList<Float> translation = new ArrayList<>();
    final List<Integer> datePos = new ArrayList<>();

    public MyArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDate(String item, float translation){
        this.translation.add(translation);
        Log.e("debug", String.valueOf(translation));
        mData.add(new DoubleString(item, ""));
        datePos.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    public void addVeranstaltung(String item1, String item2, float translation){
        this.translation.add(translation);
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
        if(datePos.contains(position)){
            return TYPE_DATE;
        }else{
            return TYPE_VERANSTALTUNG;
        }
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        Log.e("debug3", String.valueOf(pos));
        int type = getItemViewType(pos);
        TextView tv1, tv2;
        if (convertView == null) {
            if(type == TYPE_VERANSTALTUNG) {
                convertView = inflater.inflate(R.layout.veranstaltungsliste_veranstaltung, null);
            }else{
                convertView = inflater.inflate(R.layout.veranstaltungsliste_date, null);

            }
        }

        if(type == TYPE_VERANSTALTUNG) {
            tv1 = (TextView) convertView.findViewById(R.id.veranstaltungsliste_veranstaltung1);
            tv2 = (TextView) convertView.findViewById(R.id.veranstaltungsliste_veranstaltung2);
            tv1.setText(mData.get(pos).getFirst());
            tv2.setText(mData.get(pos).getSecond());

        }else{
            tv1 = (TextView)convertView.findViewById(R.id.veranstaltungsliste_date);
            tv1.setText(mData.get(pos).getFirst());
            position++;
        }
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //convertView.setTranslationZ(translation);
            convertView.setElevation(translation.get(position));
            Log.e("debug2", String.valueOf(translation.get(position)));
        }

        return convertView;
    }

    private static class DoubleString{

        public String first, second;

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