package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.view.DisplayTextChanger;
import com.honestastrology.realmexample.ui.view.Viewer;

import java.util.Iterator;

/**
 * 
 * */
class DocumentViewer implements Viewer<Document>{
    
    private final TitleListPage      _titleListPage;
    private final EditPage           _editPage;
    
    private final DisplayTextChanger _displayTextChanger;
    
    DocumentViewer(MainActivity mainActivity){
        //RealmObjectを保持するListを初期化
        _titleListPage = new TitleListPage(mainActivity, this);
        _editPage      = new EditPage(mainActivity, this);
        //タイトル文字列等、画面表示を変更する場合のコールバック
        _displayTextChanger = mainActivity;
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
    public void displayConnectString(ConnectType connectType){
        _displayTextChanger.setConnectType( connectType.getDisplayName() );
        _displayTextChanger.setSwitcherText( connectType.getTargetName() );
    }
}
