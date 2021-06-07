package com.honestastrology.realmexample.database;

public enum RealmConnectType implements ConnectType {
    SYNC("Sync File", "to Async"){
        @Override
        public void switches(DBOperator operator){
            operator.toAsync();
        }
    },
    ASYNC("Async File", "to Sync"){
        @Override
        public void switches(DBOperator operator){
            operator.toSync();
        }
    };
    
    @Override
    public String getDisplayName(){
        return _displayName;
    }
    
    @Override
    public String getTargetName(){
        return _targetName;
    }
    
    private final String _displayName;
    private final String _targetName;
    
    RealmConnectType(String name, String displayString){
        _displayName = name;
        _targetName = displayString;
    }
}
