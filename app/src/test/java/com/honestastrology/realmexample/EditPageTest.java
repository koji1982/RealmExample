package com.honestastrology.realmexample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EditPageTest {
    
    private EditPage       _editPage;
    private MainActivity _mainActivity;
    
    @Before
    public void setup()  {
        _mainActivity = Robolectric.setupActivity( NullDBActivity.class );
        Viewer<Document> viewer = new DocumentViewer( _mainActivity );
        _editPage = new EditPage(_mainActivity, viewer);
    }
    
    //EditPageを表示するメソッドにNullを渡した場合、画面遷移自体を行わない
    @Test
    public void showDocumentNullArg(){
        try {
            onView( withId( R.id.body_text ))
                    .check( matches( isDisplayed() ) );
            fail();
        } catch (Exception expected){
        }
        //テスト対象メソッド
        _editPage.showDocument( null );
        
        try {
            onView( withId( R.id.body_text ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch (Exception expected){
        }
    }
    
    @Test
    public void showDocumentTest(){
        try {
            onView( withId( R.id.body_text ))
                    .check( matches( isDisplayed() ) );
            fail();
        } catch (Exception expected){
        }
        //テスト対象メソッド
        _editPage.showDocument( new Document() );
        
        onView( withId( R.id.body_text ))
                .check( matches( isDisplayed() ));
    }
    
}
