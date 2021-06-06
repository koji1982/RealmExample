package com.honestastrology.realmexample.database;

public enum RealmConnectType implements ConnectType {
    SYNC("Sync", "to Async"){
        @Override
        public void switches(DBOperator operator){
            operator.toAsync();
        }
    },
    ASYNC("Async", "to Sync"){
        @Override
        public void switches(DBOperator operator){
            operator.toSync();
        }
    };
    
    @Override
    public String getName(){
        return _name;
    }
    
    @Override
    public String getTargetName(){
        return _targetName;
    }
    
    private final String _name;
    private final String _targetName;
    
    RealmConnectType(String name, String displayString){
        _name       = name;
        _targetName = displayString;
    }
}
