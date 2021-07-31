package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.view.LayoutType;

/** Document(RealmObject)表示のレイアウトを定義する列挙型 */
enum LayoutDefine implements LayoutType {
    
    TITLE_LIST( R.layout.entry_title_list, IS_ENTRY_PAGE     ),
    EDITOR    ( R.layout.edit_view,        IS_NOT_ENTRY_PAGE );
    
    @Override
    public int getResourceRef(){
        return _resourceFileRef;
    }
    
    @Override
    public boolean isEntryPage(){
        return _isEntryPage;
    }
    
    private final int            _resourceFileRef;
    private final boolean        _isEntryPage;
    
    LayoutDefine(int resourceFileRef, boolean isEntryPage){
        _resourceFileRef = resourceFileRef;
        _isEntryPage     = isEntryPage;
    }
    
}
