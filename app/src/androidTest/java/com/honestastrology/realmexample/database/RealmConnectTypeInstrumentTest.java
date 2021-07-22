package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.InstrumentTestHelper;
import com.honestastrology.realmexample.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.honestastrology.realmexample.InstrumentTestHelper.swapInMemoryOperator;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class RealmConnectTypeInstrumentTest {
    
    private DBOperator _dbOperator;
//    private boolean    _isInitialized = false;
    
//    @Rule
//    public ActivityScenarioRule<MainActivity> _scenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Before
    public void setup(){
        Context context = InstrumentationRegistry
                                  .getInstrumentation()
                                  .getTargetContext();
        _dbOperator = DBOperator.getInMemoryInstance( 
                            context, Persistence.TEMPORARY );
        
    }
    
    @After
    public void close(){
        _dbOperator.closeAll();
    }
    
    @Test
    public void switchesAsyncToSync(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, () -> {
//            });
            assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
            //テスト対象
            RealmConnectType.ASYNC.switches( _dbOperator );
    
            assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchSyncToAsync(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, () -> {
//            });
//        });
        _dbOperator.toSync();
        assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.SYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchesAsyncSyncGoBack(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, () -> {
//            });
//        });
        assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.ASYNC.switches( _dbOperator );
    
        assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.SYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchesSyncNoSwitch(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, () -> {
//            });
//        });
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.SYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchesAsyncNoSwitch(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, ()->{
//            });
//        });
        _dbOperator.toSync();
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.ASYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void nullTypeNoSwitches(){
//        _scenarioRule.getScenario().onActivity( activity -> {
//            startSync( activity, () -> {
//            });
//        });
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.NULL.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
        _dbOperator.toSync();
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.NULL.switches( _dbOperator );
    
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    }
    
//    //ヘルパー関数
//    private void startSync(MainActivity activity, DBOperator.SyncConnectedCallback callback){
//        if( !_isInitialized ){
//            _dbOperator = swapInMemoryOperator( activity );
//            _dbOperator.toSync();
//            _isInitialized = true;
//        } else {
//            _dbOperator.toSync();
//            callback.run();
//        }
//    }
}
