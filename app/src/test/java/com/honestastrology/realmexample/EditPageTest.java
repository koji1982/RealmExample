package com.honestastrology.realmexample;

import android.widget.TextView;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;
import mockit.Mocked;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

//このアノテーションはRealm公式で書かれていたテストコードを参照しました
//https://github.com/realm/realm-java/tree/master/examples/unitTestExample
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(sdk = 29)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*","androidx.*", "android.*","jdk.internal.reflect.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class,
        RealmResults.class, RealmCore.class, RealmLog.class})
public class EditPageTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    
    private Realm mockRealm;
    private RealmResults<Document> people;
    
    private EditPage       _editPage;
    private MainActivity _mainActivity;
    
//    @Rule
//    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);
    
    
    @Before
    public void setup()  {
        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);
        Realm.init(RuntimeEnvironment.application);

//        Realm.init( ApplicationProvider.getApplicationContext() );
        RealmConfiguration testConfig = new RealmConfiguration.Builder()
                                            .inMemory()
                                            .name("test-realm")
                                            .build();
        Realm testRealm = Realm.getInstance(testConfig);
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//        scenario.onActivity(activity -> { 
//            _mainActivity = activity; });
//        Realm.init( ApplicationProvider.getApplicationContext());
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                                            .inMemory()
//                                            .name("test-realm")
//                                            .build();
//        Realm inMemoryRealm = Realm.getInstance( config );
        _mainActivity = Robolectric.setupActivity( InMemoryDBActivity.class);
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
