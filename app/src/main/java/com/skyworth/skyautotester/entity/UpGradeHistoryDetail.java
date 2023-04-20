package com.skyworth.skyautotester.entity;

import java.util.Map;

/**
 * 系统更新对象 - 更新包使用列表详情
 */
public class UpGradeHistoryDetail{

    //Eg：[Time]从 xxx升级包 到 xxx升级包 ：success
    //key1 : value1
    private Map<String , String > detail;

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }
}
