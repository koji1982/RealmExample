package com.honestastrology.realmexample;

import android.app.AlertDialog;
import android.widget.Button;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.honestastrology.realmexample.database.DBOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.honestastrology.realmexample.InstrumentTestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DeleteDialogInstrumentTest {
    
    private DeleteDialog _deleteDialog;
    private DBOperator   _dbOperator;
    private boolean      _isInitialized = false;
    
    @Rule
    public ActivityScenarioRule<MainActivity> _scenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void onClickDeleteButton(){
        _scenarioRule.getScenario().onActivity( activity -> {
            setupField( activity );
            Document deleteTarget = createTestDocument( _dbOperator );
            int      targetId     = deleteTarget.getId();
            //削除対象のDocumentがDB内にあることを確認
            Document targetFromDB = _dbOperator.getRealmObject(
                    Document.class, Document.PRIMARY_KEY, targetId );
            assertEquals( deleteTarget.getId(), targetFromDB.getId() );
            AlertDialog decisionDialog = getDecisionDialog();
            decisionDialog.setOnDismissListener( dialogInterface -> {
                //Dialogの表示及び(テスト対象の)クリックにはラグがあるため
                //onClick終了後のonDismiss()内でAssertionを行う
                
                //DeleteTargetが削除されていることを確認
                Document targetAfterDelete = _dbOperator.getRealmObject(
                        Document.class, Document.PRIMARY_KEY, targetId );
                assertNull( targetAfterDelete );
            });
            _deleteDialog.showConfirm( deleteTarget );
            //テスト対象
            Button deleteButton = decisionDialog.getButton( AlertDialog.BUTTON_POSITIVE );
            deleteButton.callOnClick();
        });
    }
    
    private void setupField(MainActivity activity){
        if( _isInitialized ) return;
        _deleteDialog  = new DeleteDialog( activity );
        _dbOperator    = swapInMemoryOperator( activity );
        _isInitialized = true;
    }
    
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
}
