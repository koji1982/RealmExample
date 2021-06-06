package com.honestastrology.realmexample;

import android.widget.TextView;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.ui.view.ViewPage;

class EditPage implements ViewPage<Document> {
    
    private final Viewer<Document> _viewer;
    private final LayoutSwitcher   _layoutSwitcher;
    
    EditPage(MainActivity mainActivity, Viewer<Document> viewer){
        _layoutSwitcher = mainActivity;
        _viewer         = viewer;
    }
    
    @Override
    public void showContent(){
        _layoutSwitcher.changeContentView( R.layout.edit_view );
        TextView titleText
                = _layoutSwitcher.getViewFromCurrentLayout( R.id.title_text );
        TextView bodyText 
                = _layoutSwitcher.getViewFromCurrentLayout( R.id.body_text );
        Document selectedDocument = _viewer.getSelectedContent();
        titleText.setText( selectedDocument.getTitle() );
        bodyText.setText( selectedDocument.getText() );
    }
    
    @Override
    public void updateContent(){
        TextView titleText
                = _layoutSwitcher.getViewFromCurrentLayout( R.id.title_text );
        TextView bodyText
                = _layoutSwitcher.getViewFromCurrentLayout( R.id.body_text );
        Document selectedDocument = _viewer.getSelectedContent();
        selectedDocument.updateTitle( titleText.getText().toString() );
        selectedDocument.updateText( bodyText.getText().toString() );
    }
    
}
