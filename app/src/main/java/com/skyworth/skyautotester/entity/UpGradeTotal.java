package com.skyworth.skyautotester.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 系统更新对象
 */
public class UpGradeTotal implements Serializable {

    //更新包列表名
    private Map<String , List<String>> UpGradeList;
    //更新包使用列表
    private Map<String , List<UpGradeHistoryDetail>> UpGradeHistoryInfo;

    public Map<String, List<String>> getUpGradeList() {
        return UpGradeList;
    }

    public void setUpGradeList(Map<String, List<String>> upGradeList) {
        UpGradeList = upGradeList;
    }

    public Map<String, List<UpGradeHistoryDetail>> getUpGradeHistoryInfo() {
        return UpGradeHistoryInfo;
    }

    public void setUpGradeHistoryInfo(Map<String, List<UpGradeHistoryDetail>> upGradeHistoryInfo) {
        UpGradeHistoryInfo = upGradeHistoryInfo;
    }
}


