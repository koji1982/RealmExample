package com.honestastrology.realmexample;

import android.app.AlertDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class BackConfirmDialogTest {
    
    private BackConfirmDialog _backConfirmDialog;
    
    @Before
    public void setup(){
        MainActivity activity = Robolectric.setupActivity(NullDBActivity.class);
        _backConfirmDialog = new BackConfirmDialog( activity );
    }
    
    @Test
    public void showConfirmIsShowing(){
        assertFalse( getConfirmDialog().isShowing() );
        
        //テスト対象
        _backConfirmDialog.showConfirm();
        
        assertTrue( getConfirmDialog().isShowing() );
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
