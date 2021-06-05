package com.honestastrology.realmexample;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.ui.view.ViewPage;

class TitleListPage implements ViewPage<Document> {
    
    private final Viewer<Document> _viewer;
    private final LayoutSwitcher   _layoutSwitcher;
//    private AdapterView<ArrayAdapter<Document>> _listView;
    
    private ArrayAdapter<Document> _innerListAdapter;
    
    
    TitleListPage(MainActivity         mainActivity,
                  Viewer<Document> viewer       ){
        _layoutSwitcher = mainActivity;
//        mainActivity.setContentView( R.layout.activity_main );
//        
//        _listView = mainActivity.findViewById( R.id.document_title_list );
//        _listView.setOnItemClickListener( new ListClickListener() );
//        _listView.setAdapter(arrayAdapter);
        //ArrayAdapterはレイアウトファイルから取得できないため、
        //ここで生成したものを保持しておく
        _innerListAdapter = new ArrayAdapter<>( mainActivity, R.layout.list_inner_text );
        //内容(RealmObject)の変更、ViewPageの遷移はViewerを通じて行う
        _viewer     = viewer;
//        _viewer.registerViewPage(LayoutDefine.TITLE_LIST, this);
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
    }
    
    /**
     * TitleListPageではDocumentの変更・更新を行わない
     * */
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
    
}
