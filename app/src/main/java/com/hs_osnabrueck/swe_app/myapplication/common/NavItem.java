package com.hs_osnabrueck.swe_app.myapplication.common;

/**
 * this class defines how a NavItem object looks like
 */
public class NavItem {
    String mTitle;
    int mIcon;

    /**
     * NavItem constructor
     * @param title title of the navigation item
     * @param icon icon of the navigation item
     */
    public NavItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    /**
     * returns mIcon
     * @return mIcon
     */
    public int getmIcon() {
        return mIcon;
    }

    /**
     * sets mIcon
     * @param mIcon
     */
    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    /**
     * returns mTitle
     * @return mTitle
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * sets mTitle
     * @param mTitle
     */
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
