package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.BuildConfig;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.internal.OsRealmConfig;
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
                setupRealm( app.currentUser() );
            } else {
                _user      = null;
                _syncRealm = null;
                errorCallback.onError( LOGIN_ERROR_MESSAGE );
            }
        };
        
        //既にLogin状態なら終了
        if ( isLoggedIn( app ) ) return;
        
        app.loginAsync( credentials, callback );
    }
    
    SyncAccessor(String syncId, DBErrorCallback errorCallback, ConnectType connectType){
        if( RealmConnectType.IN_MEMORY != connectType ){
            throw new IllegalArgumentException();
        }
        App inMemoryApp = new App( new AppConfiguration.Builder( syncId ).build());
//        AppConfiguration appConfiguration = new AppConfiguration.Builder(syncId)
//                                                    .appName(BuildConfig.VERSION_NAME)
//                                                    .appVersion(Integer.toString(BuildConfig.VERSION_CODE))
//                                                    .build();
//        App inMemoryApp = new App( appConfiguration );
        Credentials credentials = Credentials.anonymous();
        App.Callback<User> callback = result -> {
            if( result.isSuccess() ){
                setupInMemoryRealm( inMemoryApp.currentUser() );
            } else {
                _user      = null;
                _syncRealm = null;
                errorCallback.onError( LOGIN_ERROR_MESSAGE );
            }
        };
    
        inMemoryApp.loginAsync( credentials, callback );
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
    
    private boolean isLoggedIn(App app){
        User user = app.currentUser();
        if ( user == null || !(user.isLoggedIn()) ) return false;
        
        setupRealm( user );
        return true;
    }
    
    private void setupRealm(User user){
        SyncConfiguration config = new SyncConfiguration
                                           .Builder( user, PARTITION_VAL_USER_DOC )
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
        _syncRealm = Realm.getInstance( config );
        _user = user;
    }
    
    private void setupInMemoryRealm(User user){
        SyncConfiguration config = new SyncConfiguration
                                           .Builder( user, "in_memory_document" )
                                           .inMemory()
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
//        String name = config.getRealmFileName();
//        OsRealmConfig.Durability syncDurability = config.getDurability();
//        OsRealmConfig.Durability durability     = Realm.getDefaultConfiguration().getDurability();
        _syncRealm = Realm.getInstance( config );
        _user = user;
    }
}
