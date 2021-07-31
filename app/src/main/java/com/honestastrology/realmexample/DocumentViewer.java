package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.view.LabelDisplay;
import com.honestastrology.realmexample.ui.view.Viewer;

import java.util.Iterator;

/** Document(RealmObject)扱うViewerの実装クラス。
 * Documentのタイトルリスト画面と、Documentの編集画面を
 * 表示する*/
class DocumentViewer implements Viewer<Document>{
    
    private final TitleListPage _titleListPage;
    private final EditPage      _editPage;
    
    private final LabelDisplay  _labelDisplay;
    
    DocumentViewer(MainActivity mainActivity){
        //RealmObjectを保持するListを初期化
        _titleListPage = new TitleListPage(mainActivity, this);
        _editPage      = new EditPage(mainActivity, this);
        //タイトル文字列等、画面表示
        _labelDisplay = mainActivity;
    }
    
    @Override
    public void showList(Iterator<Document> documentIterator){
        _titleListPage.showDocumentList( documentIterator );
    }
    
    @Override
    public void show(Document document){
        _editPage.showDocument( document );
    }
    
    @Override
    public void updateDisplayString(ConnectType connectType){
        _labelDisplay.updateLabel( connectType.getDisplayName() );
        _labelDisplay.updateButton( connectType.getTargetName() );
    }
}
