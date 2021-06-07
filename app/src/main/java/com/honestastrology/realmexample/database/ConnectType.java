package com.honestastrology.realmexample.database;

public interface ConnectType {
    
    public String getDisplayName();
    
    public String getTargetName();
    
    public void switches(DBOperator dbOperator);
    
}
