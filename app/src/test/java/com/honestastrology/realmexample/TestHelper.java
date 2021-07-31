package com.honestastrology.realmexample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestHelper {
    
    public static List<Document> createDocumentList(){
        //テスト用のDocumentリストを準備して返す
        List<Document> documentList = new ArrayList<>();
        documentList.add( new Document(0) );
        documentList.add( new Document(1) );
        documentList.add( new Document(2) );
        documentList.add( new Document(3) );
        documentList.add( new Document(4) );
        return documentList;
    }
    
    public static Iterator<Document> createDocumentIterator(){
        //テスト用のDocumentリストを準備して返す
        List<Document> documentList = createDocumentList();
        return documentList.iterator();
    }
    
}
