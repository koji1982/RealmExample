package com.honestastrology.realmexample;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.honestastrology.realmexample.ui.control.CommandControl;
import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;

class EditPage {
    
    private final Viewer<Document> _viewer;
    private final LayoutSwitcher   _layoutSwitcher;
    private final CommandControl<Document> _commandControl;
    
    private final BackConfirmDialog _backConfirmDialog;
    
    private Document _document;
    
    EditPage(@NonNull MainActivity     mainActivity,
             @NonNull Viewer<Document> viewer  ){
        _layoutSwitcher    = mainActivity;
        _viewer            = viewer;
        _commandControl    = mainActivity;
        _backConfirmDialog = new BackConfirmDialog( mainActivity );
    }
    
    void showDocument(Document document){
        if( document == null )return;
        
        _layoutSwitcher.changeContentView( LayoutDefine.EDITOR );
        TextView titleText  = _layoutSwitcher.getParts( PartsDefine.TITLE_TEXT );
        TextView bodyText   = _layoutSwitcher.getParts( PartsDefine.BODY_TEXT  );
        Button   saveButton = _layoutSwitcher.getParts( PartsDefine.UPDATE_BUTTON );
        Button   backButton = _layoutSwitcher.getParts( PartsDefine.BACK_BUTTON );
        titleText.setText( document.getTitle() );
        bodyText.setText( document.getText() );
        saveButton.setOnClickListener( new SaveButtonClickListener() );
        backButton.setOnClickListener( new BackButtonClickListener() );
        _document = document;
    }
    
    //その時点でのユーザー入力文字列が、保存されている文字列から変更されたかを返す
    private boolean isChanged(){
        TextView titleText
                = _layoutSwitcher.getParts( PartsDefine.TITLE_TEXT );
        String currentTitle = titleText.getText().toString();
        String savedTitle   = _document.getTitle();
        if( !( currentTitle.equals( savedTitle )) ){
            return true;
        }
        TextView bodyText
                = _layoutSwitcher.getParts( PartsDefine.BODY_TEXT );
        String currentBodyText = bodyText.getText().toString();
        String savedBodyText   = _document.getText();
        if( !( currentBodyText.equals( savedBodyText )) ){
            return true;
        }
        return false;
    }
    
    private class SaveButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TextView titleText
                    = _layoutSwitcher.getParts( PartsDefine.TITLE_TEXT );
            TextView bodyText
                    = _layoutSwitcher.getParts( PartsDefine.BODY_TEXT );
            _document.updateTitle( titleText.getText().toString() );
            _document.updateText( bodyText.getText().toString() );
            
            if( !(_document.isManaged()) ){
                _commandControl.send( DocumentSendCommand.UPDATE, _document);
            }
        }
    }
    
    private class BackButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            if( isChanged() ){
                _backConfirmDialog.showConfirm();
                return;
            }
            _commandControl.request( RequestCommand.READ );
        }
    }
    
}
