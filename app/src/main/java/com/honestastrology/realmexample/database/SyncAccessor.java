package com.honestastrology.realmexample.database;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * MongoDBにログイン、同期してリモートのDBアクセスを行うクラス
 * In-Memoryで生成した場合は,リモートのDBが使えないため
 * 端末のメモリを使用するAsyncAccessorクラスと同じ設定の
 * Realmインスタンスを使用する
 * */

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
            if( result.isSuccess() && app.currentUser() != null ){
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
    
    //In-MemoryのDBとして使う場合のコンストラクタ
    //リモートのDBはIn-Memoryとしては使用できないため
    //端末のメモリを使ったAsyncRealmを
    //Realmインスタンスに代入して使用する
    SyncAccessor(Persistence persistence){
        if( persistence != Persistence.TEMPORARY ){
            throw new IllegalArgumentException();
        }
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name( IN_MEMORY_FILE_NAME )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _syncRealm = Realm.getInstance( config );
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
            _user.logOutAsync( callback -> {} );
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
    
    private void setConfig(User user){
        SyncConfiguration config = new SyncConfiguration
                                           .Builder( user, PARTITION_VAL_USER_DOC )
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
        Realm.setDefaultConfiguration( config );
    }
    
    private void setupRealm(User user){
        setConfig( user );
        try {
            _syncRealm = Realm.getDefaultInstance();
        } catch (IllegalArgumentException e){
            _syncRealm = null;
            e.printStackTrace();
        }
        _user      = user;
    }
}
