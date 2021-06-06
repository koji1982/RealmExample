package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.view.LayoutType;

enum LayoutDefine implements LayoutType<Document> {
    
    TITLE_LIST( IS_ENTRY_PAGE     ),
    EDITOR    ( IS_NOT_ENTRY_PAGE );
    
    @Override
    public boolean isEntryPage(){
        return _isEntryPage;
    }
    
    @Override
    public LayoutType<Document> getPrevPageType(){
        return _prevPageType;
    }
    
    private final boolean        _isEntryPage;
    private LayoutType<Document> _prevPageType;
    
    LayoutDefine(boolean isEntryPage){
        _isEntryPage = isEntryPage;
    }
    
    static{
        TITLE_LIST._prevPageType = TITLE_LIST;
        EDITOR    ._prevPageType = TITLE_LIST;
    }
}
