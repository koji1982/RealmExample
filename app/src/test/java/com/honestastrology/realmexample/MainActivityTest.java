package com.honestastrology.realmexample;

import android.view.View;
import android.widget.EditText;
import androidx.test.espresso.NoMatchingViewException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.espresso.Espresso.*;

import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    
    private MainActivity _activity;
    
    @Before
    public void setUp(){
        _activity = Robolectric.setupActivity(NullDBActivity.class);
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
        assertFalse( _activity.isFinishing() );
        
        _activity.onBackPressed();
        
        assertTrue( _activity.isFinishing() );
    }
    
    //表示されている画面が違う場合にバックボタンの挙動が変わること
    //を確認するため、画面を変更してからonBackPressed()を実行する
    @Test
    public void onBackPressedTransitTest(){
        //画面を変更する
        _activity.changeContentView( LayoutDefine.EDITOR );
        try {
            //画面が変更されているので表示されていないことを確認
            onView( withId( R.id.main_frame_layout ))
                    .check( matches( isDisplayed() ));
            fail();
        } catch ( NoMatchingViewException expected) {
        }
        //テスト対象のメソッド
        _activity.onBackPressed();
        //表示されていなかったViewがisDisplayed()になっていることを確認
        onView( withId( R.id.main_frame_layout ))
                .check( matches( isDisplayed() ));
        assertFalse( _activity.isFinishing() );
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
            _activity.changeContentView( LayoutDefine.EDITOR );
        }
        //"R.id.edit_page_layout"がisDisplayed()==trueであることを確認
        onView( withId( R.id.edit_page_layout))
                .check( matches( isDisplayed() ));
    }
    
    @Test
    public void getViewFromCurrentLayoutReturnsNull(){
        //currentで使用されていないlayoutファイルから
        //取り出した場合はnullが返る
        View wrongReturnView = _activity.getParts( PartsDefine.BODY_TEXT );
        assertNull( wrongReturnView );
    }
    
    @Test
    public void getViewFromCurrentLayoutTest(){
        //テスト対象メソッド実行前にレイアウトファイルを指定する
        //このメソッドを呼んでいない場合は上記テストのようにnullが返る
        _activity.changeContentView( LayoutDefine.EDITOR );
        //テスト対象のメソッド
        View actual = _activity.getParts( PartsDefine.BODY_TEXT );
        
        assertNotNull( actual );
        assertThat( actual, isAssignableFrom( EditText.class ) );
    }
    
    @Test
    public void changeTitleNullable(){
        _activity.updateLabel( null );
        assertNotNull( _activity.getTitle() );
    }
    
    @Test
    public void changeTitleTest(){
        String insertStr = "test_string";
        //変更前に文字列を含んでいないことを確認
        assertFalse( activityTitleContains( insertStr ) );
        //テスト対象のメソッド
        _activity.updateLabel( insertStr );
        //変更後に文字列を含んでいることを確認
        assertTrue( activityTitleContains( insertStr ));
    }
    
    @Test
    public void onErrorThrowsNullException(){
        assertThrows(
                NullPointerException.class,
                () -> {
                    _activity.onError(null);} );
    }
    
    private boolean activityTitleContains(String str){
        return _activity.getTitle()
                       .toString()
                       .contains( str );
    }
}
