package com.honestastrology.realmexample.ui.view;

/** 接続タイプの表示と切り替えを行う場合に呼ばれるインターフェース */
public interface LabelDisplay {
    
    public void updateLabel(String newString);
    
    public void updateButton(String newString);
    
}
