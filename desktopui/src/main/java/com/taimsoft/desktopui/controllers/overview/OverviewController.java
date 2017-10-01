package com.taimsoft.desktopui.controllers.overview;


/**
 * Created by Tjin on 9/5/2017.
 */
public interface OverviewController<T> {
    enum SummaryLabelMode{
        Quoted,
        Unpaid,
        Paid;
    }

    void loadData();

    void initSearchField();
}
