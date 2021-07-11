package com.honestastrology.realmexample;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.honestastrology.realmexample.ui.control.CommandControl;
import com.honestastrology.realmexample.ui.view.LayoutSwitcher;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.ui.view.Page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TitleListPage implements Page<Document> {
    
    private final Viewer<Document>         _viewer;
    private final LayoutSwitcher           _layoutSwitcher;
    private final CommandControl<Document> _commandControl;
    
    private final ArrayAdapter<Document> _innerListAdapter;
    
    private final DeleteDialog             _deleteDialog;
    
    TitleListPage(@NonNull MainActivity     mainActivity,
                  @NonNull Viewer<Document> viewer       ){
        _layoutSwitcher = mainActivity;
        _commandControl = mainActivity;
        //ArrayAdapterはxmlレイアウトファイルから取得できないため、
        //ここで生成したものを保持しておく
        _innerListAdapter = new ArrayAdapter<>( mainActivity, R.layout.list_inner_text );
        //内容(RealmObject)の変更、ViewPageの遷移はViewerを通じて行う
        _viewer       = viewer;
        _deleteDialog = new DeleteDialog( mainActivity );
    }
    
    void showDocumentList(Iterator<Document> documentIterator){
        //画面のレイアウト変更
        _layoutSwitcher.changeContentView( LayoutDefine.TITLE_LIST );
        //nullの場合はそのまま終了
        if( documentIterator == null ) return;
        
        //Adapterに渡すためにListに入れ替える
        List<Document> documentList = new ArrayList<>();
        while( documentIterator.hasNext() ){
            documentList.add( documentIterator.next() );
        }
        _innerListAdapter.clear();
        _innerListAdapter.addAll( documentList );
        
        //レイアウトxml経由でListViewインスタンスを取り出してAdapterをセットする
        AdapterView<ArrayAdapter<Document>> titleListView
                = _layoutSwitcher.getParts( PartsDefine.TITLE_LIST );
        titleListView.setAdapter( _innerListAdapter );
        
        //イベントリスナーをセットする
        titleListView.setOnItemClickListener( new ListClickListener() );
        titleListView.setOnItemLongClickListener( new LongClickListener() );
        Button newNoteButton   = _layoutSwitcher.getParts( PartsDefine.NEW_NOTE_BUTTON );
        Button syncAsyncButton = _layoutSwitcher.getParts( PartsDefine.SYNC_ASYNC_BUTTON );
        newNoteButton.setOnClickListener( new NewButtonClickListener() );
        syncAsyncButton.setOnClickListener( new SyncAsyncClickListener() );
    }
    
    private class ListClickListener implements AdapterView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View           view,
                                int            position,
                                long           id ){
            Document selectedDocument = (Document)adapterView.getItemAtPosition(position);
            _viewer.show( selectedDocument );
        }
    }
    
    private class LongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView,
                                       View           view,
                                       int            position,
                                       long           id) {
            Document selectedDocument = (Document)adapterView.getItemAtPosition(position);
            _deleteDialog.showConfirm( selectedDocument );
            return true;
        }
    }
    
    private class NewButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            _commandControl.request( UIRequestCommand.CREATE );
        }
    }
    
    private class SyncAsyncClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            _commandControl.request( UIRequestCommand.SWITCH_CONNECT );
        }
    }
}
