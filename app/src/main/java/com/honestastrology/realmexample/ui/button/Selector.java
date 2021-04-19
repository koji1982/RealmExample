package com.honestastrology.realmexample.ui.button;

public interface Selector {
    
    static Selector getInstance(){
        return UISelector.getInstance();
    } 
    
    void onClick(int uiId);
    
}
