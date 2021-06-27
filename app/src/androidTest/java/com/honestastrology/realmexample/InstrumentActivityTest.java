package com.honestastrology.realmexample;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InstrumentActivityTest {
    
    private MainActivity _activity;
    private ActivityScenario<MainActivity> scenario;
    
    @Before
    public void setUp(){
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity( activity -> { 
            _activity = activity;
        });
        try {
            Field field = _activity.getClass().getDeclaredField("_dbOperator");
            field.setAccessible( true );
            field.set(_activity, DBOperator.getInMemoryInstance(_activity,_activity));
        } catch (Exception e){
        }
    }
    
    @Test
    public void realmInitTest(){
        assertEquals("com.honestastrology.realmexample", _activity.getPackageName() );
    }
    
    @Test
    public void realmUnitTest(){
//        try {
//            onView( withId( R.id.document_title_list ))
//                    .check( matches( isDisplayed() ) );
//            fail();
//        } catch (Exception expected){
//        }
//        try {
//            scenario = ActivityScenario.launch(MainActivity.class);
//            scenario.onActivity( activity -> {
//            _activity = activity;
//                try {
//                    Field field = activity.getClass().getDeclaredField("_dbOperator");
//                    field.setAccessible( true );
//                    field.set(activity, DBOperator.getInMemoryInstance(activity,activity));
//                } catch (Exception e){
//                }
                _activity.request( DocumentUICommand.CREATE );
                _activity.request( DocumentUICommand.READ );
                onView( withId( R.id.document_title_list ))
                        .check( matches( isDisplayed() ) );
//            });
//        } catch (AssertionError e){
            
//        }
//        onView( withId( R.id.document_title_list ))
//                .check( matches( isDisplayed() ) );
        
//        try {
//            Field field = _activity.getClass().getDeclaredField("_dbOperator");
//            field.setAccessible( true );
//            field.set(_activity, DBOperator.getInMemoryInstance(_activity,_activity));
//        } catch (Exception e){
//            _activity = null;
//            _activity.changeContentView(null);
//        }
//        _activity.request( DocumentUICommand.CREATE );
//        _activity.request( DocumentUICommand.READ );
//        onView( withId( R.id.document_title_list ))
//                .check( matches( isDisplayed() ) );
    }
    
}
