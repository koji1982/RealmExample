package com.honestastrology.realmexample;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import mockit.Mocked;
import mockit.Tested;

public class TitleListPageTest {
    
    @Test
    public void showContentTest() throws Exception {
//        ActivityScenario<MainActivity> scenario
//                = ActivityScenario.launch(MainActivity.class);
//        try{
//            scenario.onActivity( activity -> {
//                TitleListPage titleListPage = new TitleListPage(activity, )
//            });
//        } catch (IllegalStateException e){
//            e.printStackTrace();
//        }
        TitleListPage titleListPage = new TitleListPage(null, null);
        try {
            titleListPage.showContent();
        } catch ( NullPointerException e ){
            fail();
        }
    }
    
    private class showContentTestAction implements ActivityScenario.ActivityAction<MainActivity> {
        @Override
        public void perform(MainActivity activity) {
            TitleListPage titleListPage = new TitleListPage(activity, new DocumentViewer(activity) );
            
        }
    }
    
}
