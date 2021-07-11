package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.honestastrology.realmexample.InstrumentTestHelper.swapInMemoryOperator;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BackConfirmDialogInstrumentTest {
    
    private BackConfirmDialog _backConfirmDialog;
    private DBOperator        _dbOperator;
    private boolean           _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void onDialogCancelNotTransit(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            activity.changeContentView( LayoutDefine.EDITOR );
            AlertDialog confirmDialog = getConfirmDialog();
            confirmDialog.setOnDismissListener( dialogInterface -> {
                //Dialogの表示及び(テスト対象の)クリックにはラグがあるため
                //onClick終了後のonDismiss()内でAssertionを行う
                assertFalse( confirmDialog.isShowing() );
                assertTrue( activity.findViewById( R.id.edit_page_layout ).isEnabled());
            });
            assertTrue( activity.findViewById( R.id.edit_page_layout ).isEnabled());
            _backConfirmDialog.showConfirm();
            //テスト対象
            confirmDialog.getButton(
                    DialogInterface.BUTTON_NEGATIVE ).callOnClick();
        });
    }
    
    @Test
    public void onClickBackConfirmTransit(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            activity.changeContentView( LayoutDefine.EDITOR );
            AlertDialog confirmDialog = getConfirmDialog();
            confirmDialog.setOnShowListener( dialogInterface -> {
                //テスト対象
                confirmDialog.getButton( DialogInterface.BUTTON_POSITIVE ).callOnClick();
            });
            confirmDialog.setOnDismissListener( dialogInterface -> {
                //Dialogの表示及び(テスト対象の)クリックにはラグがあるため
                //onClick終了後のonDismiss()内でAssertionを行う
                assertTrue( activity.findViewById( R.id.main_frame_layout ).isEnabled() );
            });
            _backConfirmDialog.showConfirm();
            assertTrue( activity.findViewById( R.id.edit_page_layout ).isEnabled() );
        });
    }
    
    //ヘルパー関数
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _backConfirmDialog  = new BackConfirmDialog( activity );
        _dbOperator         = swapInMemoryOperator( activity );
        _isInitialized      = true;
    }
    
    private AlertDialog getConfirmDialog(){
        try{
            Field field = _backConfirmDialog.getClass()
                                  .getDeclaredField("_confirmDialog");
            field.setAccessible( true );
            return (AlertDialog)field.get(_backConfirmDialog);
        } catch (Exception e){
        }
        return null;
    }
}
