package com.kxxfydj.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * create by kaiming_xu on 2017/9/2
 */
public enum CardTypeEnum {

    //信用卡
    CREDITCARD("CREDITCARD",1),
    //储蓄卡
    DEBITCARD("DEBITCARD",0);

    private String text;

    private int value;

    CardTypeEnum(String text,int value){
        this.text = text;
        this.value = value;
    }

    private static Map<String,Integer> cardTypeMap = new HashMap<>();

    static {
        CardTypeEnum[] cardTypeEnums = CardTypeEnum.values();

        for(CardTypeEnum cardTypeEnum : cardTypeEnums){
            cardTypeMap.put(cardTypeEnum.getText(),cardTypeEnum.getValue());
        }
    }

    public static Integer getValue(String text){
        if(StringUtils.isNotBlank(text)){
            String txt = text.trim();

            Iterator<Map.Entry<String,Integer>> mapItor = cardTypeMap.entrySet().iterator();
            while(mapItor.hasNext()){
                Map.Entry<String,Integer> entry = mapItor.next();
                if(entry.getKey().equals(txt)){
                    return  entry.getValue();
                }
            }
        }
        return null;
    }


    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

}
