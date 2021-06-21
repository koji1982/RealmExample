package com.honestastrology.realmexample;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InstrumentActivityTest {
    
    private Context _context;
    
    @Before
    public void setUp(){
        _context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Realm.init( _context );
    }
    
    @Test
    public void realmInitTest(){
        assertEquals("com.honestastrology.realmexample", _context.getPackageName());
    }
    
}
