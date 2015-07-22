package com.hs_osnabrueck.swe_app.myapplication.common;

/**
 *
 */
public class NavItem {
    String mTitle;
    int mIcon;

    /**
     *
     * @param title
     * @param icon
     */
    public NavItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    /**
     *
     * @return
     */
    public int getmIcon() {
        return mIcon;
    }

    /**
     *
     * @param mIcon
     */
    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    /**
     *
     * @return
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     *
     * @param mTitle
     */
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
