package com.honestastrology.realmexample;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.honestastrology.realmexample.ui.layout.Viewer;
import com.honestastrology.realmexample.ui.layout.ViewPage;

import java.util.ArrayList;

class TitleListPage implements ViewPage<Document> {
    
    private final Viewer<Document>              _viewer;
    private AdapterView<ArrayAdapter<Document>> _listView;
    
    Activity _activity;
    
    TitleListPage(Activity         mainActivity,
                  Viewer<Document> viewer       ){
        _activity = mainActivity;
        mainActivity.setContentView( R.layout.activity_main );
        
        _listView = mainActivity.findViewById( R.id.document_title_list );
        _listView.setOnItemClickListener( new ListClickListener() );
        ArrayAdapter<Document> arrayAdapter
                = new ArrayAdapter<>( mainActivity, R.layout.list_inner_text );
        _listView.setAdapter(arrayAdapter);
        //内容(RealmObject)の変更、ViewPageの遷移はViewerを通じて行う
        _viewer     = viewer;
    }
    
    @Override
    public void showContent(){
        _listView = _activity.findViewById( R.id.document_title_list );
        ArrayAdapter<Document> documentAdapter = new ArrayAdapter<>(_activity, R.layout.list_inner_text);
//        ArrayAdapter<Document> documentAdapter = _listView.getAdapter();
        documentAdapter.clear();
        documentAdapter.addAll( _viewer.getContents() );
        _listView.setAdapter( documentAdapter );
    }
    
    private class ListClickListener implements AdapterView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View           view,
                                int            position,
                                long           id ){
            Document selectedDocument = (Document)adapterView.getItemAtPosition(position);
            _viewer.setSelectedContent( selectedDocument );
            _viewer.transitViewPage( DisplayLayout.EDITOR );
        }
    }
    
}
