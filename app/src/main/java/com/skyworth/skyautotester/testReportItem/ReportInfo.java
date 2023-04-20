package com.skyworth.skyautotester.testReportItem;

public class ReportInfo {
    private String fileName;
    private String testStatus;
    private String testItemName;

    private boolean isChecked;
    public ReportInfo(String fileName) {
        this.fileName = fileName;
    }
    public ReportInfo(String fileName,String testStatus,String testItemName) {
        this.fileName = fileName;
        this.testStatus = testStatus;
        this.testItemName = testItemName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestItemName() {
        return testItemName;
    }

    public void setTestItemName(String testItemName) {
        this.testItemName = testItemName;
    }
}
