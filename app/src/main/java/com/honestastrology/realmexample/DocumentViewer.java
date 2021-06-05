package com.honestastrology.realmexample;

import com.honestastrology.realmexample.database.ConnectType;
import com.honestastrology.realmexample.ui.view.LayoutType;
import com.honestastrology.realmexample.ui.view.Viewer;
import com.honestastrology.realmexample.ui.view.ViewPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * */
class DocumentViewer implements Viewer<Document>{
    
    private Map<LayoutType<Document>, ViewPage<Document>> _layoutMap;
    private List<Document>       _documentList;
    private LayoutType<Document> _currentLayoutType;
    private Document             _currentSelectedDocument;
    
    private String _connectSwitchButtonString;
    
    DocumentViewer(MainActivity mainActivity){
        //RealmObjectを保持するListを初期化
        _documentList = new ArrayList<>();
        //ViewPageを生成してMapに登録する。
        //使用時はLayoutTypeを指定して取り出す
        _layoutMap = new HashMap<>();
        _layoutMap.put(LayoutDefine.TITLE_LIST, new TitleListPage( mainActivity, this ) );
        _layoutMap.put(LayoutDefine.EDITOR,     new EditPage( mainActivity, this )      );
    }
    
    @Override
    public void registerViewPage(LayoutType<Document> layoutType,
                                 ViewPage<Document>   viewPage   ){
        _layoutMap.put( layoutType, viewPage );
    }
    
    @Override
    public void transitViewPage(LayoutType<Document> nextLayoutType){
        if( !_layoutMap.containsKey( nextLayoutType ) ){
            System.out.println( "page transit ERROR. Type-Page Map is NOT contains key.");
            return;
        }
        
        ViewPage<Document> nextPage = _layoutMap.get( nextLayoutType );
        _currentLayoutType = nextLayoutType;
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
    
    @Override
    public void onSwitchConnect(ConnectType connectType){
        _connectSwitchButtonString = connectType.getDisplayString();
    }
    
    @Override
    public void update(){
        ViewPage<Document> currentViewPage
                = _layoutMap.get(_currentLayoutType);
        currentViewPage.updateContent();
    }
    
}
