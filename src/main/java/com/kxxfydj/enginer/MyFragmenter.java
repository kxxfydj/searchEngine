package com.kxxfydj.enginer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.search.highlight.Fragmenter;

/**
 * Created by kxxfydj on 2018/5/9.
 */
public class MyFragmenter implements Fragmenter {
    private int currentFragments;

    private String splitStr;

    private int currentRows;

    private OffsetAttribute offsetAtt;

    public MyFragmenter(String splitStr){
        this.splitStr = splitStr;
    }

    @Override
    public void start(String s, TokenStream tokenStream) {
        this.offsetAtt = (OffsetAttribute)tokenStream.addAttribute(OffsetAttribute.class);
        this.currentFragments = 1;
    }

    @Override
    public boolean isNewFragment() {
        return false;
    }
}
