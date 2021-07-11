package com.honestastrology.realmexample;

import android.widget.ListView;

import androidx.test.espresso.NoMatchingViewException;

import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.Iterator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.honestastrology.realmexample.TestHelper.createDocumentIterator;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TitleListPageTest {
    
    private TitleListPage _titleListPage;
    private MainActivity  _activity;
    
    @Before
    public void setUp(){
        _activity = Robolectric.setupActivity(NullDBActivity.class);
        Viewer<Document> documentViewer = new DocumentViewer( _activity );
        _titleListPage = new TitleListPage( _activity, documentViewer );
    }
    
    //TitleListPageはNullを渡された場合でも画面遷移を行う
    @Test
    public void showDocumentListNullArg(){
        //画面を変更して、対象の表示されていないことを確認
        _activity.changeContentView( LayoutDefine.EDITOR );
        try {
            onView( withId( R.id.main_frame_layout ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch ( NoMatchingViewException expected) {
        }
        //テスト対象メソッド
        _titleListPage.showDocumentList( null );
        //表示されていなかったViewがisDisplayedになっていることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void showDocumentListTest(){
        //画面を変更して、対象のレイアウトパーツが表示されていないことを確認
        _activity.changeContentView( LayoutDefine.EDITOR );
        try {
            onView( withId( R.id.document_title_list ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch ( NoMatchingViewException expected) {
        }
        Iterator<Document> documentIterator = createDocumentIterator();
        //テスト対象メソッド
        _titleListPage.showDocumentList( documentIterator );
        //Iteratorを渡された時に正常に表示されていることを確認
        onView( withId( R.id.document_title_list ))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void onItemClickTest(){
        //インデックスとIDが同じになるように生成したListViewを取得する
        Iterator<Document> documentIterator = createDocumentIterator();
        _titleListPage.showDocumentList( documentIterator );
        ListView titleList = ( ListView )_activity.getParts( PartsDefine.TITLE_LIST );
        
        //テスト対象
        //index == 3をクリックする
        int targetIndex = 3;
        titleList.getOnItemClickListener()
                .onItemClick(
                        titleList, 
                        titleList.getChildAt(targetIndex),
                        targetIndex, 
                        titleList.getItemIdAtPosition(targetIndex));
        
        onView( withId( R.id.body_text ))
                .check( matches( isDisplayed() ));
        //インデックスとIDを同じ数値にそろえたListViewを使っている
        //クリックされたインデックスと、表示されたEditPageのDocumentのIDが同じであることを確認
        assertEquals( targetIndex, getEditorDocumentId() );
    }
    
    @Test
    public void onItemLongClickTest(){
        //インデックスとIDが同じになるように生成したListViewを取得する
        Iterator<Document> documentIterator = createDocumentIterator();
        _titleListPage.showDocumentList( documentIterator );
        ListView titleList = ( ListView )_activity.getParts( PartsDefine.TITLE_LIST );
        
        //テスト対象
        //index == 2をロングクリックする
        int targetIndex = 2;
        titleList.getOnItemLongClickListener()
                .onItemLongClick(
                        titleList,
                        titleList.getChildAt( targetIndex ),
                        targetIndex,
                        titleList.getItemIdAtPosition(targetIndex));
        
        //インデックスとIDを同じ数値にそろえたListViewを使っている
        //ロングクリックされたインデックスと、DeleteDialogに渡されたDocumentのIDが同じであることを確認
        assertEquals( targetIndex, getDeleteTargetId() );
    }
    
    //ヘルパー関数
    //EditPageで表示されているDocumentのIDをリフレクションで返す
    //EditPageが表示されていない場合このメソッドは実行できない
    private int getEditorDocumentId(){
        try {
            Class titleListClass  = _titleListPage.getClass();
            Field field_viewer  = titleListClass.getDeclaredField("_viewer");
            field_viewer.setAccessible( true );
            Viewer<Document> viewer = (Viewer<Document>)field_viewer.get(_titleListPage);
            
            Class viewerClass = viewer.getClass();
            Field field_editPage = viewerClass.getDeclaredField("_editPage");
            field_editPage.setAccessible( true );
            EditPage editPage = (EditPage)field_editPage.get(viewer);
            
            Class editPageClass = editPage.getClass();
            Field field_document = editPageClass.getDeclaredField("_document");
            field_document.setAccessible( true );
            Document document = (Document)field_document.get(editPage);
            return document.getId();
        } catch (Exception e){
            
        }
        //リフレクション失敗ならIDとして存在しない数値を返す
        return -1;
    }
    
    //ヘルパー関数
    //DeleteDialogにアクセスするリフレクション
    //ダイアログ表示時に渡されたDocumentのIDを返す
    private int getDeleteTargetId(){
        try {
            Class titleListClass  = _titleListPage.getClass();
            Field field_deleteDialog  = titleListClass.getDeclaredField("_deleteDialog");
            field_deleteDialog.setAccessible( true );
            DeleteDialog deleteDialog = (DeleteDialog)field_deleteDialog.get(_titleListPage);
        
            Class deleteDialogClass = deleteDialog.getClass();
            Field field_deleteTarget = deleteDialogClass.getDeclaredField("_deleteTarget");
            field_deleteTarget.setAccessible( true );
            Document targetDocument = (Document)field_deleteTarget.get(deleteDialog);
        
            return targetDocument.getId();
        } catch (Exception e){
        
        }
        //リフレクション失敗ならIDとして存在しない数値を返す
        return -1;
    }
}
