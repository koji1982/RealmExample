package com.honestastrology.realmexample;

import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentTest {
    
    private DBOperator _dbOperator;
    private boolean    _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void requestNullThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            assertThrows(NullPointerException.class, () -> {
                activity.request( null );
            });
        });
    }
    
    @Test
    public void requestCREATECommand(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            assertNull( activity.findViewById( R.id.edit_page_layout ));
            assertNull( activity.getParts( PartsDefine.TITLE_TEXT ));
            assertNull( activity.getParts( PartsDefine.BODY_TEXT ));
            
            //テスト対象
            activity.request( UIRequestCommand.CREATE );
            
            //画面遷移、レイアウトパーツ、空文字列が表示されていることを確認
            TextView titleView = activity.getParts( PartsDefine.TITLE_TEXT );
            TextView bodyView  = activity.getParts( PartsDefine.BODY_TEXT );
            assertTrue( activity.findViewById( R.id.edit_page_layout ).isEnabled() );
            assertTrue( titleView.isEnabled() );
            assertTrue( bodyView.isEnabled() );
            assertEquals( "", titleView.getText().toString() );
            assertEquals( "", bodyView.getText().toString());
        });
    }
    
    @Test
    public void requestREADCommand(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //EditPageを開く
            activity.request( UIRequestCommand.CREATE );
            
            assertNull( activity.findViewById( R.id.main_frame_layout ));
            assertNull( activity.getParts( PartsDefine.TITLE_LIST ));
            assertNull( activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON ));
            assertNull( activity.getParts( PartsDefine.NEW_NOTE_BUTTON ));
            
            //テスト対象
            activity.request( UIRequestCommand.READ );
    
            assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
            assertTrue( activity.getParts( PartsDefine.TITLE_LIST ).isEnabled() );
            assertTrue( activity.getParts( PartsDefine.SYNC_ASYNC_BUTTON ).isEnabled() );
            assertTrue( activity.getParts( PartsDefine.NEW_NOTE_BUTTON ).isEnabled() );
        });
    }
    
    @Test
    public void sendNullNullThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            assertThrows(NullPointerException.class, () -> {
                activity.send( null, null );
            });
        });
    }
    
    @Test
    public void sendNullWithDocumentThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            assertThrows(NullPointerException.class, () -> {
                activity.send( null, new Document() );
            });
        });
    }
    
    @Test
    public void sendUpdateWithNullThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            assertThrows(IllegalArgumentException.class, () -> {
                activity.send( DBSendCommand.UPDATE, null );
            });
        });
    }
    
    @Test
    public void sendDeleteWithNullThrowsNull(){
        _scenarioRule.getScenario().onActivity( activity -> {
            assertThrows(NullPointerException.class, () -> {
                activity.send( DBSendCommand.DELETE, null );
            });
        });
    }
    
    @Test
    public void sendUpdateDocument(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            //準備として、unmanagedのRealmObjectを用意して文字列を更新する
            Document document          = InstrumentTestHelper.createTestDocument( _dbOperator );
            Document unmanagedDocument = document.getRealm().copyFromRealm( document );
            int primaryId = unmanagedDocument.getId();
            String updatedTitle = "updated title";
            String updatedBody  = "updated body";
            unmanagedDocument.updateTitle( updatedTitle );
            unmanagedDocument.updateText( updatedBody );
            //unmanagedなのでDocumentの更新でデータベースの情報が更新されていないことを確認
            Document preCheck = _dbOperator.getRealmObject(
                            Document.class, Document.PRIMARY_KEY, primaryId );
            assertEquals( "", preCheck.getTitle() );
            assertEquals( "", preCheck.getText() );
            
            //テスト対象
            activity.send( DBSendCommand.UPDATE, unmanagedDocument );
            
            //データベース内の情報が更新されていることを確認
            Document postCheck = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, primaryId );
            assertEquals( updatedTitle, postCheck.getTitle() );
            assertEquals( updatedBody,  postCheck.getText() );
        });
    }
    
    @Test
    public void sendDeleteDocument(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document   = createTestDocument( _dbOperator );
            int      id         = document.getId();
            //データベースにデータがあることを確認
            Document preCheck   = _dbOperator.getRealmObject(
                                    Document.class, Document.PRIMARY_KEY, id );
            assertEquals( id, preCheck.getId() );
            
            //テスト対象
            activity.send( DBSendCommand.DELETE, document );
            
            //データベースのデータが無い(nullになっている)ことを確認
            Document postCheck = _dbOperator.getRealmObject(
                                    Document.class, Document.PRIMARY_KEY, id);
            assertNull( postCheck );
        });
    }
    
    @Test
    public void sendDeleteNullNotDelete(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document document   = createTestDocument( _dbOperator );
            //テストメソッド実行前のデータを取得,更新が反映されないようにするためIDのみを取り出しておく
            Iterator<Document> expectIterator = _dbOperator.readAll( Document.class );
            List<Integer>      expectIdList   = new ArrayList<>();
            while( expectIterator.hasNext() ){
                expectIdList.add( expectIterator.next().getId() );
            }
            
            //テスト対象
            try {
                activity.send( DBSendCommand.DELETE, null );
                fail();
            } catch (Exception expected){
            }
            
            //全てのデータのIDを照合して、何も消去されていないことを確認
            Iterator<Document> actualIterator   = _dbOperator.readAll( Document.class );
            for (Integer integer : expectIdList) {
                assertEquals(integer.intValue(),
                        actualIterator.next().getId());
            }
        });
    }
    
    //ヘルパー関数
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _dbOperator    = swapInMemoryOperator( activity );
        _isInitialized = true;
    }
}
