package com.honestastrology.realmexample.ui.view;

/** 画面遷移時にレイアウトタイプを指定するためのインターフェース。
 *  起動ページか否かの真偽値と、リソースファイルの参照値を持たせることを
 *  前提としている  */
public interface LayoutType {
    
    public static final boolean IS_ENTRY_PAGE     = true;
    public static final boolean IS_NOT_ENTRY_PAGE = false;
    
    public int getResourceRef();
    
    public boolean isEntryPage();
    
}
