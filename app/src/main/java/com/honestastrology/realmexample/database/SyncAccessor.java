package com.honestastrology.realmexample.database;

import android.content.Context;
import android.util.Log;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.ConnectionListener;
import io.realm.mongodb.sync.ConnectionState;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;

class SyncAccessor implements DBAccessor {
    
    private static final String PARTITION_VAL_USER_DOC = "user_document";
    
    private Realm             _syncRealm;
    private App               _app;
    private SyncConfiguration _config;
    
    private WaitingDialog _connectDialog;
    
    private SyncSession _session;
    
    SyncAccessor(Context        context,
                 LayoutSwitcher startupCallback,
                 String         syncId){
        
        _connectDialog = new ConnectDialog( context );
        _app = new App( new AppConfiguration.Builder( syncId ).build());
        
        Credentials credentials = Credentials.anonymous();
//        Credentials credentials = Credentials.apiKey("vGBzTPrwZ3zT05qqwTaaK8vShDatOp5Wr2d4ZkngeXx5CjCJnUwkTVVToORhbzuL");
        
        User user = _app.login( credentials );
        _config = createConfig( user );
        _syncRealm = Realm.getInstance( _config );
        
        
//        LoginCallback callback  = new LoginCallback( app );
//        App.Callback<User> callback = result -> {
//            if (result.isSuccess()) {
//                Log.v("SYNC LOGIN", "Successfully authenticated anonymously.");
//                User user = _app.currentUser();
//                _config   = createConfig( user );
////                 interact with realm using your user object here
////                SyncConfiguration config = new SyncConfiguration
////                                                   .Builder(user, PARTITION_VAL_USER_DOC)
////                                                   .allowQueriesOnUiThread(true)
////                                                   .allowWritesOnUiThread(true)
////                                                   .build();
////                synchronized( _syncRealm ){
////                }
//                _syncRealm = Realm.getInstance( _config );
//                
//                
////                _connectDialog.dismissConnectingDialog();
////                boolean isConnected = _app.getSync().getSession(_config).isConnected();
////                System.out.println( "callback " + isConnected );
////                System.out.println( "3. log in " + String.valueOf( _app.currentUser().isLoggedIn()) ) ;
////                boolean isConnected = isNull();
////                System.out.println(isConnected);
////                _session = _app.getSync().getSession(_config);
////                _session.addConnectionChangeListener(new ConnectChangeListener());
//                
//            } else {
//                Log.e("SYNC LOGIN", "Failed to log in. Error: " + result.getError());
//                System.out.println( "3. log in " + String.valueOf( _app.currentUser().isLoggedIn()) ) ;
//
//            }
//            //成功か失敗に関わらず、resultを受け取ったタイミングで起動画面を表示する
////            startupCallback.showEntryView();
//        };
//        _session = _app.getSync().getSession(_config);
//        _session.addConnectionChangeListener( new ConnectChangeListener() );
        
//        _connectDialog.showConnectingDialog();
//        _app.loginAsync( credentials, callback );
//        User user = _app.currentUser();
//        _config   = createConfig( user );
//        _syncRealm = Realm.getInstance( _config );
        
//        User user = _app.login( credentials );
//        _config = createConfig( user );
//        _syncRealm = Realm.getInstance( _config );
        
//        ConnectionState connectionState = _session.getConnectionState();
//        System.out.println( "constractor " + connectionState );
    }
    
    @Override
    public boolean isValid(){
        return (_syncRealm == null);
//        if( _config == null ) return false;
//        return true;
        
        
//        Sync sync = _app.getSync();
//        SyncSession syncSession = sync.getSession(_config);
//        boolean isConnected = syncSession.isConnected();
//        ConnectionState state = syncSession.getConnectionState();
//        return isConnected;
//        return _app.getSync().getSession(_config).isConnected();
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
        _app.currentUser().logOut();
        _syncRealm.close();
    }
    
    private SyncConfiguration createConfig(User user){
        return new SyncConfiguration
                           .Builder(user, PARTITION_VAL_USER_DOC)
                           .allowQueriesOnUiThread(true)
                           .allowWritesOnUiThread(true)
                           .build();
    }
    
//    private class LoginCallback implements App.Callback<User> {
//        
//        private App _app;
//        
//        private LoginCallback(App app){
//            _app = app;
//        }
//        
//        @Override
//        public void onResult(App.Result<User> result){
//            if (result.isSuccess()) {
//                Log.v("LOGIN", "Successfully authenticated anonymously.");
//                User user = app.currentUser();
//                // interact with realm using your user object here
//                SyncConfiguration config = new SyncConfiguration
//                                                       .Builder(user, PARTITION_VAL_USER_DOC)
//                                                   .allowQueriesOnUiThread(true)
//                                                   .allowWritesOnUiThread(true)
//                                                   .build();
//                _syncRealm = Realm.getInstance( config );
//            } else {
//                Log.e("LOGIN", "Failed to log in. Error: " + result.getError());
//            }
//        }
//    }
    
    private class ConnectChangeListener implements ConnectionListener {
        @Override
        public void onChange(ConnectionState oldState,
                             ConnectionState newState){
            System.out.println("old " + oldState.name() + ": new "+ newState.name() );
        }
        
    }
    
    private class ReadCommand<E extends RealmObject> implements Callable<Iterator<E>> {
        
        private Class<E> _clazz;
        
        private ReadCommand(Class<E> clazz){
            _clazz = clazz;
        }
        
        @Override
        public Iterator<E> call(){
            return _syncRealm.where( _clazz ).findAll().iterator();
        }
    }
}
