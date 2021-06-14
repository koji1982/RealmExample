package com.honestastrology.realmexample;

import android.view.View;
import android.widget.EditText;
import androidx.test.espresso.NoMatchingViewException;

import com.honestastrology.realmexample.database.DBOperator;
import com.honestastrology.realmexample.ui.view.LayoutType;
import com.honestastrology.realmexample.ui.view.Viewer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static androidx.test.espresso.Espresso.*;

import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    
    private MainActivity activity;
    
    //単体テスト用にRealmを止めたMainActivityサブクラスを用意する
    public static class TestActivity extends MainActivity {
        @Override
        protected DBOperator createDBOperator(){
            return DBOperator.getNullInstance();
        }
    }

    @Before
    public void setUp(){
        activity = Robolectric.setupActivity(TestActivity.class);
    }
    
    @Test
    public void entryLayoutIsDisplayed(){
        //起動画面が表示されていることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ) );
    }
    
    @Test
    public void noEntryLayoutFailed(){
        try{
            //起動画面とは別のレイアウトなので例外をスローする
            onView( withId( R.id.edit_page_layout ))
                    .check( matches( isDisplayed() ) );
            fail();
        } catch (NoMatchingViewException expected ){
            
        }
    }
    
    //起動画面でバックボタンを押すとそのまま終了する
    @Test
    public void onBackPressedFinishedTest(){
        assertFalse( activity.isFinishing() );
        
        activity.onBackPressed();
        
        assertTrue( activity.isFinishing() );
    }
    
    //表示されている画面が違う場合にバックボタンの挙動が変わること
    //を確認するため、画面を変更してからonBackPressed()を実行する
    @Test
    public void onBackPressedTransitTest(){
        //画面を変更する
        transitEditViewPage();
        try {
            //画面が変更されているので表示されていないことを確認
            onView( withId( R.id.main_frame_layout ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch ( NoMatchingViewException expected) {
        }
        //テスト対象のメソッド
        activity.onBackPressed();
        //表示されていなかったViewがisDisplayed()になっていることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
        assertFalse( activity.isFinishing() );
    }
    
    @Test
    public void changeContentViewTest(){
        try{
            //"R.id.edit_page_layout"は
            //isDisplayed()==trueではないので例外をスロー
            onView( withId( R.id.edit_page_layout ))
                    .check( matches( isDisplayed() ) );
        } catch (NoMatchingViewException expected ){
            //テスト対象のメソッド
            activity.changeContentView( R.layout.edit_view );
        }
        //"R.id.edit_page_layout"がisDisplayed()==trueであることを確認
        onView( withId( R.id.edit_page_layout))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void getViewFromCurrentLayoutReturnsNull(){
        //currentで使用されていないlayoutファイルから
        //取り出した場合はnullが返る
        View wrongReturnView = activity.getViewFromCurrentLayout( R.id.body_text );
        assertNull( wrongReturnView );
    }
    
    @Test
    public void getViewFromCurrentLayoutTest(){
        //テスト対象メソッド実行前にレイアウトファイルを指定する
        //このメソッドを呼んでいない場合は上記テストのようにnullが返る
        activity.changeContentView( R.layout.edit_view );
        //テスト対象のメソッド
        View actual = activity.getViewFromCurrentLayout( R.id.body_text );
        
        assertNotNull( actual );
        assertThat( actual, isAssignableFrom( EditText.class ) );
    }
    
    @Test
    public void changeTitleNullable(){
        activity.changeTitle( null );
        assertNotNull( activity.getTitle() );
    }
    
    @Test
    public void changeTitleTest(){
        String insertStr = "test_string";
        //変更前に文字列を含んでいないことを確認
        assertFalse( activityTitleContains( insertStr ) );
        //テスト対象のメソッド
        activity.changeTitle( insertStr );
        //変更後に文字列を含んでいることを確認
        assertTrue( activityTitleContains( insertStr ));
    }
    
    @Test
    public void onErrorThrowsNullException(){
        assertThrows(
                NullPointerException.class,
                () -> {activity.onError(null);} );
    }
    
    //ヘルパー関数
    private boolean activityTitleContains(String str){
        return activity.getTitle()
                       .toString()
                       .contains( str );
    }
    
    //ヘルパー関数
    private void transitEditViewPage(){
        //MainActivityの画面遷移(レイアウト変更)ではなく
        //それも含んだViewerからの画面遷移がテストに必要なため
        //リフレクションで取り出して実行する
        try {
            Class clazz  = ((MainActivity)activity).getClass().getSuperclass();
            Field field  = clazz.getDeclaredField("_viewer");
            field.setAccessible( true );
            Viewer<Document> viewer = (Viewer<Document>)field.get(activity);
            viewer.transitViewPage( LayoutDefine.EDITOR );
        } catch (Exception e){
            
        }
    }
    
}
