package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.view.LayoutType;

enum LayoutDefine implements LayoutType<Document> {
    
    TITLE_LIST(R.layout.entry_title_list),
    EDITOR(R.layout.edit_view);
    
    @Override
    public int getResource(){
        return _resourceId;
    }
    
    private final int _resourceId;
    
    LayoutDefine(int resourceId){
        _resourceId = resourceId;
    }
}
