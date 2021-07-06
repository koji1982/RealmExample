package com.honestastrology.realmexample;

import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.database.RealmConnectType;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.honestastrology.realmexample.TestHelper.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DocumentViewerTest {
    
    private MainActivity     _mainActivity;
    private Viewer<Document> _viewer;
    
    @Before
    public void setUp(){
        _mainActivity = Robolectric.setupActivity( NullDBActivity.class );
        _viewer       = new DocumentViewer( _mainActivity );
    }
    
    @Test
    public void showListDocumentIterator(){
        List<Document>     documentList  = createDocumentList();
        Iterator<Document> argIterator   = documentList.iterator();
        Iterator<Document> checkIterator = documentList.iterator();
        
        //テスト対象
        _viewer.showList( argIterator );
        
        //引数として渡す前のIDと表示されてるアイテムのIDが同じであることを確認
        ListView    listView = _mainActivity.getParts( PartsDefine.TITLE_LIST );
        ListAdapter adapter  = listView.getAdapter();
        int         index    = 0;
        while( checkIterator.hasNext() ){
            assertEquals(
                    checkIterator.next().getId(),
                    ((Document)adapter.getItem( index )).getId() );
            index++;
        }
    }
    
    @Test
    public void showListArgNullTransitTitleList(){
        _mainActivity.changeContentView( LayoutDefine.EDITOR );
        try {
            onView( withId( R.id.main_frame_layout ))
                    .check( matches( isDisplayed() ) );
        } catch (Exception expected){
        }
        
        //テスト対象
        _viewer.showList( null );
        
        //nullが渡されても表示されることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void showListEmptyIteratorTransitTitleList(){
        _mainActivity.changeContentView( LayoutDefine.EDITOR );
        List<Document> emptyList = new ArrayList<>();
        Iterator<Document> emptyIterator = emptyList.iterator();
        try {
            onView( withId( R.id.main_frame_layout ))
                    .check( matches( isDisplayed() ) );
        } catch (Exception expected){
        }
    
        //テスト対象
        _viewer.showList( emptyIterator );
    
        //空のIteratorが渡されても表示されることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void showDocument(){
        Document document = new Document( 0 );
        try {
            onView( withId( R.id.edit_page_layout ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch(Exception expect){
        }
        
        //テスト対象
        _viewer.show( document );
    
        onView( withId( R.id.edit_page_layout ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void showArgNullNotTransit(){
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
        
        //テスト対象
        _viewer.show( null );
        
        try {
            onView( withId( R.id.edit_page_layout ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch(Exception expect){
        }
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void displayConnectStringChangeLabels(){
        ConnectType testTargetType = RealmConnectType.SYNC;
        String preTitle            = _mainActivity.getTitle().toString();
        Button preSyncAsyncButton  = _mainActivity.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
        String preButtonText       = preSyncAsyncButton.getText().toString();
        //表示がtargetと違っていることを確認
        assertFalse( preTitle.contains( testTargetType.getDisplayName() ));
        assertNotEquals( testTargetType.getTargetName(), preButtonText );
        
        //テスト対象メソッド
        _viewer.displayConnectString( testTargetType );
        
        //表示がtargetと同じに変更されていることを確認
        String actualTitle      = _mainActivity.getTitle().toString();
        Button syncAsyncButton  = _mainActivity.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
        String actualButtonText = syncAsyncButton.getText().toString();
        assertTrue( actualTitle.contains( testTargetType.getDisplayName() ));
        assertEquals( testTargetType.getTargetName(), actualButtonText );
    }
}
