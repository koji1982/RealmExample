package com.honestastrology.realmexample;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.ui.view.ViewPage;

class TitleListPage implements ViewPage<Document> {
    
    private final Viewer<Document> _viewer;
    private final LayoutSwitcher   _layoutSwitcher;
    private final DeleteDialog     _deleteDialog;
    
    private final ArrayAdapter<Document> _innerListAdapter;
    
    
    TitleListPage(@NonNull MainActivity     mainActivity,
                  @NonNull Viewer<Document> viewer       ){
        _layoutSwitcher = mainActivity;
        //ArrayAdapterはxmlレイアウトファイルから取得できないため、
        //ここで生成したものを保持しておく
        _innerListAdapter = new ArrayAdapter<>( mainActivity, R.layout.list_inner_text );
        //内容(RealmObject)の変更、ViewPageの遷移はViewerを通じて行う
        _viewer       = viewer;
        _deleteDialog = new DeleteDialog( mainActivity );
    }
    
    @Override
    public void showContent(){
        
        _innerListAdapter.clear();
        _innerListAdapter.addAll( _viewer.getContents() );
        
        _layoutSwitcher.changeContentView( R.layout.entry_title_list );
        AdapterView<ArrayAdapter<Document>> titleListView
                = _layoutSwitcher.getViewFromCurrentLayout( R.id.document_title_list );
        titleListView.setAdapter( _innerListAdapter );
        titleListView.setOnItemClickListener( new ListClickListener() );
        titleListView.setOnItemLongClickListener( new LongClickListener() );
    }
    
    //TitleListPageではDocumentの変更・更新を行わない
    @Override
    public void updateContent(){
        
    }
    
    private class ListClickListener implements AdapterView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View           view,
                                int            position,
                                long           id ){
            Document selectedDocument = (Document)adapterView.getItemAtPosition(position);
            _viewer.setSelectedContent( selectedDocument );
            _viewer.transitViewPage( LayoutDefine.EDITOR );
        }
    }
    
    private class LongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView,
                                       View           view,
                                       int            position,
                                       long           id) {
            Document selectedDocument = (Document)adapterView.getItemAtPosition(position);
            _viewer.setSelectedContent( selectedDocument );
            _deleteDialog.show(view);
            return true;
        }
    }
}
