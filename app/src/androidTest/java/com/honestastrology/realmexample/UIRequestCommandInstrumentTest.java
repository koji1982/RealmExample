package com.honestastrology.realmexample;

import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.database.RealmConnectType;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static com.honestastrology.realmexample.UIRequestCommand.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UIRequestCommandInstrumentTest {
    
    private DBOperator       _dbOperator;
    private Viewer<Document> _viewer;
    private boolean          _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<InMemoryActivity> _scenarioRule
            = new ActivityScenarioRule<>(InMemoryActivity.class);
    
    @Test
    public void executeCreateThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            assertThrows(
                    NullPointerException.class,
                    () -> CREATE.execute(null, _dbOperator) );
            assertThrows(
                    NullPointerException.class,
                    () -> CREATE.execute(_viewer, null) );
            assertThrows(
                    NullPointerException.class,
                    () -> CREATE.execute( null, null ) );
        });
    }
    
    @Test
    public void executeCreateNullDBNotCreate(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Iterator<Document> preIterator = _dbOperator.readAll( Document.class );
            int expectedSize = 0;
            while( preIterator.hasNext() ){
                preIterator.next();
                expectedSize++;
            }
            assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
            
            //テスト対象
            CREATE.execute(_viewer, DBOperator.getNullInstance() );
            
            //Iteratorのサイズと表示されているレイアウトが変わっていないことを確認
            Iterator<Document> postIterator = _dbOperator.readAll( Document.class );
            int actualSize = 0;
            while( postIterator.hasNext() ){
                postIterator.next();
                actualSize++;
            }
            assertEquals( expectedSize, actualSize );
            assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
        });
    }
    
    @Test
    public void executeCreateDocument(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            int preMaxId = getNewId( _dbOperator );
            Iterator<Document> preIterator = _dbOperator.readAll( Document.class );
            int preSize = 0;
            while( preIterator.hasNext() ){
                preIterator.next();
                preSize++;
            }
            assertNull( activity.findViewById( R.id.edit_page_layout ) );
            assertNull( activity.getParts( PartsDefine.TITLE_TEXT ) );
            assertNull( activity.getParts( PartsDefine.BODY_TEXT ) );
            
            //テスト対象
            CREATE.execute( _viewer, _dbOperator );
            
            //maxIdが一つ増えていること、
            //DB内のDocument数が一つ増えていること、
            //Editorに移動しレイアウトパーツが表示されていること、
            //Documentが空のテキストであることを確認
            int postMaxId = getNewId( _dbOperator );
            Iterator<Document> postIterator = _dbOperator.readAll( Document.class );
            int postSize = 0;
            while( postIterator.hasNext() ){
                postIterator.next();
                postSize++;
            }
            TextView titleText = activity.getParts( PartsDefine.TITLE_TEXT );
            TextView bodyText  = activity.getParts( PartsDefine.BODY_TEXT );
            assertEquals( (preMaxId + 1), postMaxId );
            assertEquals( (preSize + 1), postSize );
            assertTrue( activity.findViewById( R.id.edit_page_layout ).isEnabled() );
            assertTrue( titleText.isEnabled() );
            assertTrue( bodyText.isEnabled() );
            assertEquals( "", titleText.getText().toString() );
            assertEquals( "", bodyText.getText().toString() );
        });
    }
    
    @Test
    public void executeReadThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            assertThrows(
                    NullPointerException.class,
                    () -> READ.execute( _viewer, null ) );
            assertThrows(
                    NullPointerException.class,
                    () -> READ.execute(null, _dbOperator) );
            assertThrows(
                    NullPointerException.class,
                    () -> READ.execute( null, null ) );
        });
    }
    
    @Test
    public void executeReadNullOperatorTransitScreen(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            activity.changeContentView( LayoutDefine.EDITOR );
            assertNull( activity.findViewById( R.id.main_frame_layout ) );
            
            //テスト対象
            READ.execute( _viewer, DBOperator.getNullInstance() );
            
            //NullDBOperatorが渡された場合でも画面遷移は行う
            assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
        });
    }
    
    @Test
    public void executeRead(){
        _scenarioRule.getScenario().onActivity( activity -> {
            CountDownLatch latch = new CountDownLatch( 1 );
            DBOperator.SyncConnectedCallback callback = () -> {
                setupMultipleDocuments( _dbOperator );
                Iterator<Document> preDBIterator = _dbOperator.readAll( Document.class );
    
                //テスト対象
                READ.execute( _viewer, _dbOperator );
    
                //正しいレイアウトが表示されていること、
                //ConnectTypeのタイトル表示・ボタン表示が正しいこと
                //DB内のDocumentのIDと表示されているDocumentのIDが一致していることを確認
                assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
                assertTrue( activity.getTitle().toString().contains(
                        _dbOperator.getCurrentConnect().getDisplayName() ));
                Button syncAsyncButton = activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON);
                assertEquals(
                        _dbOperator.getCurrentConnect().getTargetName(),
                        syncAsyncButton.getText().toString() );
                ListView listView = activity.getParts( PartsDefine.TITLE_LIST );
                ListAdapter adapter  = listView.getAdapter();
                for( int i=0;i<adapter.getCount();i++){
                    assertEquals(
                            preDBIterator.next().getId(),
                            ((Document)adapter.getItem( i )).getId() );
                }
                latch.countDown();
            };
            setupSync( activity,callback );
//            FutureTask<String> futureTask = new FutureTask<>(callback, "isDone");
//            ExecutorService    executor   = Executors.newFixedThreadPool(2);
//            executor.submit( futureTask );
            try{
                latch.await();
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    
    @Test
    public void executeSwitchConnectThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            assertThrows(
                    NullPointerException.class,
                    () -> SWITCH_CONNECT.execute( _viewer, null ) );
            assertThrows(
                    NullPointerException.class,
                    () -> SWITCH_CONNECT.execute(null, _dbOperator) );
            assertThrows(
                    NullPointerException.class,
                    () -> SWITCH_CONNECT.execute( null, null ) );
        });
    }
    
    @Test
    public void executeSwitchConnectNullOperator(){
        _scenarioRule.getScenario().onActivity( activity ->{
            setupField( activity );
            ConnectType expectType = RealmConnectType.NULL;
            
            //テスト対象
            SWITCH_CONNECT.execute( _viewer, DBOperator.getNullInstance() );
            
            
            assertTrue( activity.getTitle().toString().contains( 
                                                        expectType.getDisplayName() ));
            Button syncAsyncButton = activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
            assertEquals( expectType.getTargetName(), syncAsyncButton.getText().toString() );
        });
    }
    
    @Test
    public void executeSwitchConnectToSync(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupSync( activity , () -> {
                assertTrue( activity.getTitle().toString().contains(
                        RealmConnectType.ASYNC.getDisplayName() ));
                Button preButton = activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
                assertEquals( RealmConnectType.ASYNC.getTargetName(), preButton.getText().toString());
    
                //テスト対象
                SWITCH_CONNECT.execute( _viewer, _dbOperator );
    
                assertTrue( activity.getTitle().toString().contains(
                        RealmConnectType.SYNC.getDisplayName() ));
                Button postButton = activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
                assertEquals( RealmConnectType.SYNC.getTargetName(), postButton.getText().toString());
            });
        });
    }
    
    
    
    //ヘルパー関数
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator = swapInMemoryOperator( activity );
        _viewer     = new DocumentViewer( activity );
        _isInitialized = true;
    }
    
    private void setupSync(MainActivity activity, DBOperator.SyncConnectedCallback callback){
        if( !_isInitialized ){
            _dbOperator = swapInMemorySyncOperator( activity, callback );
            _viewer     = new DocumentViewer( activity );
            _isInitialized = true;
        } else {
            callback.run();
        }
    }
    
    //ヘルパー関数
    public static int getNewId(DBOperator dbOperator){
        try {
            Method method = UIRequestCommand.class
                                   .getDeclaredMethod("getNewId", DBOperator.class);
            method.setAccessible( true );
            return (int)method.invoke(null, dbOperator );
        } catch (Exception e){
        }
        return 0;
    }
    
}
