package com.honestastrology.realmexample;

import android.app.Activity;

import com.honestastrology.realmexample.ui.layout.LayoutType;
import com.honestastrology.realmexample.ui.layout.Viewer;
import com.honestastrology.realmexample.ui.layout.ViewPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class DocumentViewer implements Viewer<Document> {
    
    private final Activity _activity;
    private final Map<LayoutType<Document>, ViewPage<Document>> _layoutMap;
    
    private List<Document>  _documentList;
    private Document        _currentSelectedDocument;
    
    DocumentViewer(Activity mainActivity){
        //レイアウト切り替えをコントロールするため
        //このクラスでActivity参照を保持する
        _activity  = mainActivity;
        //ViewPageを生成する
        ViewPage<Document> titleListPage = new TitleListPage( mainActivity, this );
        ViewPage<Document> editPage      = new EditPage( mainActivity, this );
        //ViewPageはMap内で保持し、使用時はDisplayTypeを指定して取り出す
        _layoutMap = new HashMap<>();
        _layoutMap.put( DisplayLayout.TITLE_LIST, titleListPage );
        _layoutMap.put( DisplayLayout.EDITOR,     editPage      );
        //RealmObjectを保持するListを初期化
        _documentList = new ArrayList<>();
    }
    
    @Override
    public void transitViewPage(LayoutType<Document> nextLayoutType){
        if( !_layoutMap.containsKey( nextLayoutType ) ){
            System.out.println( "page transit ERROR. Type-Page Map is NOT contains key.");
            return;
        }
    
        _activity.setContentView( nextLayoutType.getResource() );
        ViewPage<Document> nextPage = _layoutMap.get( nextLayoutType );
        nextPage.showContent();
    }
    
    @Override
    public void setContents(Iterator<Document> iterator){
        if( iterator == null )return;
        _documentList.clear();
        while( iterator.hasNext() ){
            _documentList.add( iterator.next() );
        }
    }
    
    @Override
    public List<Document> getContents(){
        return _documentList;
    }
    
    @Override
    public void setSelectedContent(Document selectedContent){
        _currentSelectedDocument = selectedContent;
    }
    
    @Override
    public Document getSelectedContent(){
        return _currentSelectedDocument;
    }
    
}
