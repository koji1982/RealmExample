package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.content.DialogInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DeleteDialogTest {
    
    private DeleteDialog _deleteDialog;
    
    @Before
    public void setup(){
        MainActivity activity = Robolectric.setupActivity(NullDBActivity.class);
        _deleteDialog = new DeleteDialog( activity );
    }
    
    @Test
    public void showConfirmArgNull(){
        //テスト対象
        _deleteDialog.showConfirm( null );
        
        assertFalse( getDecisionDialog().isShowing() );
    }
    
    @Test
    public void showConfirmDialogEnable(){
        Document document = new Document( 0 );
        int      id       = document.getId();
        
        //テスト対象
        _deleteDialog.showConfirm( document );
        
        assertTrue( getDecisionDialog().isShowing() );
        assertEquals( id, getDeleteTarget().getId() );
    }
    
    @Test
    public void onClickCancelButton(){
        Document document = new Document( 0 );
        AlertDialog decisionDialog = getDecisionDialog();
        decisionDialog.setOnDismissListener( dialogInterface -> {
            //Dialogの表示、クリック処理にそれぞれラグがあるため
            //それらが完了した後のonDismiss()内でAssertionを行う
            assertFalse( decisionDialog.isShowing() );
        });
        _deleteDialog.showConfirm( document );
        
        //テスト対象
        decisionDialog.getButton( DialogInterface.BUTTON_NEGATIVE )
                      .callOnClick();
    }
    
    //ヘルパー関数
    private AlertDialog getDecisionDialog(){
        try{
            Field field_dialog = _deleteDialog.getClass()
                                         .getDeclaredField("_decisionDialog");
            field_dialog.setAccessible( true );
            return (AlertDialog)field_dialog.get(_deleteDialog);
        } catch (Exception e){
        }
        
        return null;
    }
    
    private Document getDeleteTarget(){
        try {
            Field field_deleteTarget = _deleteDialog.getClass()
                                               .getDeclaredField("_deleteTarget");
            field_deleteTarget.setAccessible( true );
            return (Document)field_deleteTarget.get(_deleteDialog);
        } catch (Exception e){
        }
        return null;
    }
}
