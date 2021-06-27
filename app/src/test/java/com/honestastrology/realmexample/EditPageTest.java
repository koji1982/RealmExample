package com.honestastrology.realmexample;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EditPageTest {
    
    private EditPage       _editPage;
    private MainActivity _mainActivity;
    
//    @Rule
//    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);
    
    @Before
    public void setup()  {
//        Realm.init( ApplicationProvider.getApplicationContext() );
        Realm.init(InstrumentationRegistry.getInstrumentation().getTargetContext());
//        RealmConfiguration testConfig = new RealmConfiguration.Builder()
//                                            .inMemory()
//                                            .name("test-realm")
//                                            .allowWritesOnUiThread(true)
//                                            .allowQueriesOnUiThread(true)
//                                            .build();
//        Realm testRealm = Realm.getInstance(testConfig);
        
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//        scenario.onActivity(activity -> { 
//            _mainActivity = activity; });
//        _mainActivity = Robolectric.setupActivity( InMemoryDBActivity.class);
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
    
    @Test
    public void showDocumentTitleTest(){
        String displayTestText = "display test text";
        Document document = new Document();
        document.updateTitle( displayTestText );
        //テスト対象メソッド
        _editPage.showDocument( document );
    
        TextView actualTitle = _mainActivity.getParts( PartsDefine.TITLE_LIST );
        assertEquals( displayTestText, actualTitle.getText().toString() );
    }
}
