package com.honestastrology.realmexample.database;

/** DBの接続タイプを外部から参照する場合のインターフェース */
public interface ConnectType {
    
    public String getDisplayName();
    
    public String getTargetName();
    
    public void switches(DBOperator dbOperator);
    
}
