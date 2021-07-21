package com.honestastrology.realmexample.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.core.os.HandlerCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestScratch {
    
//    private DBAccessor _syncAccessor;
    private Realm _realm;
    
    @Test
    public void asyncTrial(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Realm.init( context );
        RealmConfiguration config = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name( "inMemory_test" )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _realm = Realm.getInstance( config );
        
        _realm.executeTransaction(transaction -> {
            System.out.println("test");
            assertEquals( 4, 2+2 );
        });
    }
    
    @Test
    public void trial(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Realm.init( context );
        
        RealmConfiguration asyncConfig = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name( "inMemory_test" )
                                            .allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
        _realm = Realm.getInstance( asyncConfig );
        
        Realm.setDefaultConfiguration( asyncConfig );
        App app = new App( new AppConfiguration.Builder( "realmexample-puwav" ).build());
        Credentials credentials = Credentials.anonymous();
        
//        App.Callback<User> callback = result -> {
//            SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser(), "user_document")
//                                               .inMemory()
//                                               .allowQueriesOnUiThread(true)
//                                               .allowWritesOnUiThread(true)
//                                               .build();
//            Realm instance = Realm.getInstance( config );
//            System.out.println( instance.getPath() );
//            assertEquals( 4, 2+2);
//        };
        
        User user = app.login( credentials );
//        Realm.removeDefaultConfiguration();
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
//                                                 .inMemory()
//                                                 .allowQueriesOnUiThread( true )
//                                                 .allowWritesOnUiThread( true )
//                                                 .build();
//        Realm.setDefaultConfiguration( realmConfig );
        SyncConfiguration config = new SyncConfiguration.Builder(user, "user_document")
                                           .inMemory()
                                           .allowQueriesOnUiThread(true)
                                           .allowWritesOnUiThread(true)
                                           .build();
        
//        Thread thread = new Thread(){
//            private Handler _handler;
//            @Override
//            public void run(){
//                Looper.prepare();
//                
//                _handler = new Handler(Looper.myLooper()){
//                    @Override
//                    public void handleMessage(Message message){
//                        Realm.getInstanceAsync( config, new Realm.Callback(){
//                            @Override
//                            public void onSuccess(Realm realm) {
//                                _realm = realm;
//                                assertEquals( 4, 2+2 );
//                            }
//                            @Override
//                            public void onError(Throwable exception){
//                                exception.printStackTrace();
//                            }
//                        });
//                    }
//                };
//                
//                Looper.loop();
//            }
//        };
//        thread.run();
//        CountDownLatch latch = new CountDownLatch( 1 );
//        FutureTask<String> futureTask = new FutureTask<>( () -> {
//            Realm.getInstanceAsync( config, new Realm.Callback(){
//                @Override
//                public void onSuccess(Realm realm) {
//                    _realm = realm;
//                    assertEquals( 4, 2+2 );
//                    latch.countDown();
//                }
//                @Override
//                public void onError(Throwable exception){
//                    exception.printStackTrace();
//                    latch.countDown();
//                }
//            });
//        }, "");
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.submit( futureTask );
//        try {
//            latch.await();
//        } catch (Exception e){
//            
//        }
        CountDownLatch latch = new CountDownLatch( 1 );
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        handler.post( () -> {
            Realm.getInstanceAsync( config, new Realm.Callback(){
                @Override
                public void onSuccess(Realm realm) {
                    _realm = realm;
                    assertEquals( 4, 2+2 );
                    latch.countDown();
                }
                @Override
                public void onError(Throwable exception){
                    exception.printStackTrace();
                    latch.countDown();
                }
            });
        } );
        try {
            latch.await();
        } catch (Exception e){
            e.printStackTrace();
        }
    
        assertEquals( 2, 1+1 );
//        Realm.getInstanceAsync( config, new Realm.Callback(){
//            @Override
//            public void onSuccess(Realm realm) {
//                _realm = realm;
//                assertEquals( 4, 2+2 );
//            }
//            @Override
//            public void onError(Throwable exception){
//                exception.printStackTrace();
//            }
//        });
        
    }
}
