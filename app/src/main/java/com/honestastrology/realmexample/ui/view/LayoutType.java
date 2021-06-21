package com.honestastrology.realmexample.ui.view;

/**
 * ActivityのsetContentView() を呼び出してレイアウトを変更する場合に
 * 使用するインターフェース
 * setContentView() で使用するリソースファイル参照値(int)を、
 * このインターフェースの実装クラスで対応させて指定する
 * */
public interface LayoutType {
    
    public static final boolean IS_ENTRY_PAGE     = true;
    public static final boolean IS_NOT_ENTRY_PAGE = false;
    
    public int getResourceRef();
    
    public boolean isEntryPage();
    
}
