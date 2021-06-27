package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

class SyncAccessor implements DBAccessor {
    
    private static final String PARTITION_VAL_USER_DOC = "user_document";
    private static final String TEST_API_KEY           = "vGBzTPrwZ3zT05qqwTaaK8vShDatOp5Wr2d4ZkngeXx5CjCJnUwkTVVToORhbzuL";
    private static final String LOGIN_ERROR_MESSAGE    = "Sync DB login failed.";
    
    private Realm           _syncRealm;
    private User            _user;
    
    SyncAccessor(String syncId, DBErrorCallback errorCallback){
        
        App app = new App( new AppConfiguration.Builder( syncId ).build());
        Credentials credentials = Credentials.apiKey( TEST_API_KEY );
        
        App.Callback<User> callback = result -> {
            if( result.isSuccess() ){
                _user = app.currentUser();
                SyncConfiguration config = createConfig( _user );
                _syncRealm = Realm.getInstance( config );
            } else {
                _user      = null;
                _syncRealm = null;
                errorCallback.onError( LOGIN_ERROR_MESSAGE );
            }
        };
        app.loginAsync( credentials, callback );
    }
    
    @Override
    public boolean isNull(){
        return (_syncRealm == null);
    }
    
    @Override
    public ConnectType getConnectType(){
        return RealmConnectType.SYNC;
    }
    
    @Override
    public <E extends RealmObject>
    Number getMaxPrimaryNumber(Class<E> clazz, String primaryKeyField){
        return _syncRealm.where( clazz ).max( primaryKeyField );
    }
    
    @Override
    public void create(RealmObject realmObject){
        _syncRealm.executeTransaction(
                realmTransaction -> _syncRealm.insert( realmObject ));
    }
    
    @Override
    public <E extends RealmObject> E getRealmObject(Class<E> clazz,
                                                    String   fieldName,
                                                    int      id){
        return _syncRealm.where( clazz ).equalTo( fieldName, id ).findFirst();
    }
    
    @Override
    public <E extends RealmObject> Iterator<E> readAll(Class<E> clazz){
        return _syncRealm.where( clazz ).findAll().iterator();
    }
    
    @Override
    public void update(RealmObject realmObject){
        _syncRealm.executeTransaction(
                realmTransaction -> _syncRealm.copyToRealmOrUpdate( realmObject ));
    }
    
    @Override
    public void delete(RealmObject realmObject){
        _syncRealm.executeTransaction(
                realmTransaction -> realmObject.deleteFromRealm() );
    }
    
    @Override
    public void close(){
        if( _user != null ){
            _user.logOutAsync(result -> {});
        }
        if( _syncRealm != null ){
            _syncRealm.close();
        }
    }
    
    private SyncConfiguration createConfig(User user){
        return new SyncConfiguration
                           .Builder( user, PARTITION_VAL_USER_DOC )
                           .allowQueriesOnUiThread(true)
                           .allowWritesOnUiThread(true)
                           .build();
    }
    
}
