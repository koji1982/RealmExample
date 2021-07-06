package com.honestastrology.realmexample;

import org.junit.Test;

import static org.junit.Assert.*;

public class LayoutDefineTest {
    
    @Test
    public void titleListIsEntryPage(){
        assertTrue( LayoutDefine.TITLE_LIST.isEntryPage() );
    }
    
    @Test
    public void editorIsNotEntryPage(){
        assertFalse( LayoutDefine.EDITOR.isEntryPage() );
    }
    
}
