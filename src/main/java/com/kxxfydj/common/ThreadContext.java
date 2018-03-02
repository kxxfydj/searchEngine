package com.kxxfydj.common;

import java.util.HashMap;

/**
 * 用于保存线程上下文信息
 * Created by ChenWei on 2016/12/28.
 */
public class ThreadContext {
    private static final InheritableThreadLocal<HashMap<String, Object>> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    private ThreadContext() {
    }

    public static void put(String key, Object value){
        HashMap<String, Object> context = CONTEXT_HOLDER.get();
        if(context == null){
            context = new HashMap<>();
            CONTEXT_HOLDER.set(context);
        }
        context.put(key, value);
    }

    public static <T extends Object> T get(String key){
        HashMap<String, Object> context = CONTEXT_HOLDER.get();
        if(context == null){
            return null;
        }
        return (T) context.get(key);
    }

    public static boolean contains(String key){
        HashMap<String, Object> context = CONTEXT_HOLDER.get();
        if(context == null){
            return false;
        }
        return context.containsKey(key);
    }

    public static void remove(String key){
        HashMap<String, Object> context = CONTEXT_HOLDER.get();
        if(context != null){
            context.remove(key);
        }
    }

    public static void clear(){
        HashMap<String, Object> context = CONTEXT_HOLDER.get();
        if(context != null){
            context.clear();
            CONTEXT_HOLDER.set(null);
        }
    }
}
