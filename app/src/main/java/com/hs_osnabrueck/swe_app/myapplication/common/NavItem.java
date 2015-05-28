package com.hs_osnabrueck.swe_app.myapplication.common;

public class NavItem {
    String mTitle;
    int mIcon;

    public NavItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
