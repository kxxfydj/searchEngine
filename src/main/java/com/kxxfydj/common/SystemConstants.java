package com.kxxfydj.common;

/**
 * Created by ChenWei on 2016/12/2.
 */
public class SystemConstants {

    public static final String GLOBAL_DEBUG_SWITCH = "global.debug.switch";

    private SystemConstants() {
    }

    /**
     * 判断当前是否测试环境
     * @return true or false
     */
    public static boolean isDebugEnv(){
        String debugProperty = System.getProperty(GLOBAL_DEBUG_SWITCH);
        return debugProperty != null && "true".equalsIgnoreCase(debugProperty);
    }
}
