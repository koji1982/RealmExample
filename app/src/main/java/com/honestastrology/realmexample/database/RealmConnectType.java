package com.honestastrology.realmexample.database;

public enum RealmConnectType implements ConnectType {
    SYNC("to Async"){
        @Override
        public void switches(DBOperator operator){
            operator.toAsync();
        }
    },
    ASYNC("to Sync"){
        @Override
        public void switches(DBOperator operator){
            operator.toSync();
        }
    };
    
    public String getDisplayString(){
        return _displayString;
    }
    
    private final String _displayString;
    
    RealmConnectType(String displayString){
        this._displayString = displayString;
    }
}
