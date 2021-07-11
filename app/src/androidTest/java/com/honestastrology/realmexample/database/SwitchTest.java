package com.honestastrology.realmexample.database;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SwitchTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void switchSyncToAsync(){
        _scenarioRule.getScenario().onActivity( activity -> {
            DBOperator dbOperator = extractDBOperator( activity );
            RealmConnectType.SYNC.switches( dbOperator );
            
            assertEquals( RealmConnectType.ASYNC, dbOperator.getCurrentConnect() );
        });
    }
    
    private DBOperator extractDBOperator(MainActivity activity){
        try{
            Field field = activity.getClass().getDeclaredField("_dbOperator");
            field.setAccessible( true );
            return (DBOperator)field.get(activity);
        } catch (Exception e){}
        return null;
    }
}
