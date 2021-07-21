package com.honestastrology.realmexample.database;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.honestastrology.realmexample.InstrumentTestHelper.swapInMemorySyncOperator;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RealmConnectTypeInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void switchSyncToAsync(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, () -> {
                assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.SYNC.switches( _dbOperator );
                
                assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    @Test
    public void switchAsyncToSync(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, () -> {
                _dbOperator.toAsync();
                assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.ASYNC.switches( _dbOperator );
                
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    @Test
    public void switchesRepeat(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, () -> {
                assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.SYNC.switches( _dbOperator );
                
                assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.ASYNC.switches( _dbOperator );
                
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    @Test
    public void switchesSyncNoSwitch(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, () -> {
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.SYNC.switches( _dbOperator );
                
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    @Test
    public void switchesAsyncNoSwitch(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, ()->{
                _dbOperator.toAsync();
                assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.ASYNC.switches( _dbOperator );
                
                assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    @Test
    public void nullTypeNoSwitches(){
        _scenarioRule.getScenario().onActivity( activity -> {
            startSync( activity, () -> {
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.NULL.switches( _dbOperator );
                
                assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
                
                _dbOperator.toAsync();
                assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
                
                //テスト対象
                RealmConnectType.NULL.switches( _dbOperator );
                
                assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
            });
        });
    }
    
    //ヘルパー関数
    private void startSync(MainActivity activity, DBOperator.SyncConnectedCallback callback){
        if( !_isInitialized ){
            _dbOperator = swapInMemorySyncOperator( activity, callback );
            _isInitialized = true;
        } else {
            _dbOperator.toSync();
            callback.run();
        }
    }
}
