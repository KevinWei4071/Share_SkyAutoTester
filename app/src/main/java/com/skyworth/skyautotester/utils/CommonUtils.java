package com.skyworth.skyautotester.utils;

/**
 * 通用工具类
 */
public class CommonUtils {

    //功能类似于c语言的 __function__ 、 __line__ ：可能有bug
    private String FUNCTION_LINE = "[" + new Exception().getStackTrace()[3].getMethodName() + " - " + new Exception().getStackTrace()[3].getLineNumber() + " ] ";

    public static CommonUtils getInstance() {
        return new CommonUtils();
    }

    public String getFUNCTION_LINE() {
        return FUNCTION_LINE;
    }

    public void setFUNCTION_LINE(String FUNCTION_LINE) {
        this.FUNCTION_LINE = FUNCTION_LINE;
    }
}
