package com.honestastrology.realmexample;

import androidx.test.core.app.ActivityScenario;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import mockit.Expectations;
import mockit.Mocked;

public class EditPageTest {
    
    private EditPage       _editPage;
    private LayoutSwitcher _layoutSwitcher;
    @Mocked
    private MainActivity _mainActivity;
    @Mocked
    private Viewer<Document> _viewer;
    
    @Before
    public void setup() throws Exception {
//        ActivityScenario<MainActivity> scenario
//                = ActivityScenario.launch(MainActivity.class);
//        try{
//            scenario.onActivity( mainActivity -> {
//                
//            });
//        } catch (IllegalStateException e){
//            e.printStackTrace();
//        }
        _editPage = new EditPage(_mainActivity, _viewer);
    }
    
    @Test
    public void nullTitleTextViewFail(){
        assertThrows(
                NullPointerException.class,
                () -> _editPage.showContent() );        
    }
    
}
