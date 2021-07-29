package com.honestastrology.realmexample;

import com.honestastrology.realmexample.ui.view.Parts;


/** 定数(列挙型)として定義したui.view.Partsの実装クラス */
enum PartsDefine implements Parts {
    
    TITLE_LIST       ( R.id.document_title_list),
    SYNC_ASYNC_BUTTON( R.id.sync_async_button  ),
    NEW_NOTE_BUTTON  ( R.id.create_button      ),
    TITLE_TEXT       ( R.id.title_text         ),
    BODY_TEXT        ( R.id.body_text          ),
    UPDATE_BUTTON    ( R.id.update_button      ),
    BACK_BUTTON      ( R.id.back_from_edit     );
    
    @Override
    public int getResourceId(){
        return _resourceId;
    }
    
    private final int _resourceId;
    
    PartsDefine(int resourceId){
        _resourceId = resourceId;
    }
}
