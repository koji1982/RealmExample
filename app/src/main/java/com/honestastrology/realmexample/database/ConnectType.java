package com.honestastrology.realmexample.database;

public interface ConnectType {
    
    public String getName();
    
    public String getTargetName();
    
    public void switches(DBOperator dbOperator);
    
}
