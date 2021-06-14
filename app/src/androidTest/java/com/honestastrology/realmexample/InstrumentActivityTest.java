package com.honestastrology.realmexample;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InstrumentActivityTest {
    ActivityScenario<MainActivity> _scenario;
    
    
    @Before
    public void setUp(){
        _scenario
                = ActivityScenario.launch(MainActivity.class);
        _scenario.moveToState(Lifecycle.State.INITIALIZED);
//        try {
//            scenario.onActivity( activity -> {
//                _mainActivity = activity;
//            });
//        } catch (IllegalStateException e){
//            
//        }
    }
    
    @Test
    public void nullToast(){
        try {
            _scenario.onActivity((activity)->{
                onView( ViewMatchers.withId(R.id.document_title_list));
            });
        } catch (IllegalStateException e) {
            
        } catch (ExceptionInInitializerError e){
            e.getStackTrace();
        }
        
    }
}
