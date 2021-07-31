package com.honestastrology.realmexample.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RealmConnectTypeInstrumentTest {
    
    private DBOperator _dbOperator;
    
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
            assertEquals(RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
            //テスト対象
            RealmConnectType.ASYNC.switches( _dbOperator );
    
            assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchSyncToAsync(){
        _dbOperator.toSync();
        assertEquals(RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.SYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchesAsyncSyncGoBack(){
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
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.SYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.ASYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void switchesAsyncNoSwitch(){
        _dbOperator.toSync();
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    
        //テスト対象
        RealmConnectType.ASYNC.switches( _dbOperator );
    
        assertEquals( RealmConnectType.SYNC, _dbOperator.getCurrentConnect() );
    }
    
    @Test
    public void nullTypeNoSwitches(){
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
}
