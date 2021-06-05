package com.honestastrology.realmexample.ui.view;

import android.view.View;

public interface LayoutSwitcher {
    
    void showEntryView();
    
    void changeContentView(int layoutFile);
    
    <T extends View> T getViewFromCurrentLayout(int resourceId);
    
}
