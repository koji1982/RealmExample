package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.layout.LayoutType;

enum DisplayLayout implements LayoutType<Document> {
    
    TITLE_LIST(R.layout.activity_main),
    EDITOR(R.layout.edit_view);
    
    @Override
    public int getResource(){
        return _resourceId;
    }
    
    private final int _resourceId;
    
    DisplayLayout(int resourceId){
        _resourceId = resourceId;
    }
}
