package com.honestastrology.realmexample.database;

/** RealmAccessorの接続タイプを表す列挙型 */
public enum RealmConnectType implements ConnectType {
    SYNC( "Sync DB",   "to Async" ){
        @Override
        public void switches(DBOperator operator){
            operator.toAsync();
        }
    },
    ASYNC( "Async DB", "to Sync" ){
        @Override
        public void switches(DBOperator operator){
            operator.toSync();
        }
    },
    NULL( "Null DB", "Null"){
        @Override
        public void switches(DBOperator operator){
        }
    };
    
    @Override
    public String getDisplayName(){
        return _displayName;
    }
    
    @Override
    public String getTargetName(){
        return _switchTarget;
    }
    
    private final String _displayName;
    private final String _switchTarget;
    
    RealmConnectType(String name, String displayString){
        _displayName  = name;
        _switchTarget = displayString;
    }
    
}
