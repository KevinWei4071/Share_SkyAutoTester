package com.skyworth.skyautotester.utils;


import static com.skyworth.skyautotester.utils.DataIOUtils.ShowUpgradeInfo;
import static com.skyworth.skyautotester.utils.DataIOUtils.showAllFileFromSkyAutoTester;

import android.util.Log;

import com.skyworth.skyautotester.entity.UpGradeHistoryDetail;
import com.skyworth.skyautotester.entity.UpGradeTotal;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于excel交互操作
 */
public class ExcelUtils {
    private final static String TAG = "SkyAutoTest";
    private final static String TestReportFileName = "/data/SkyAutoTester/SkyAutoTester.xlsx";
    private final static String FILE_UPDATELIST = "update.list";
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;
    private static File file;
    //表头的列数
    private static int TITLE_COLUMN_NUM = 3 - 1;
    //目前excel内容输出到哪一行了
    private int CURRENT_EXCEL_CONTENT_ROW_NUM = 0;
    /*** 用于辨认/data/SkyAutoTester/下哪些是测试文件，避免程序去跑读错误文件。注意：后续如果添加测试项需要输出报告，需要在此数组中添加文件名 ***/
    private String[] TestReportFileArr = {"MediaReport.txt", "sourceTestResult.txt", FILE_UPDATELIST};

    public static String F_L() {
        return CommonUtils.getInstance().getFUNCTION_LINE();
    }


    /**
     * 生成命名为SkyTester.xlsx文件并且存到/data/SkyAutoTester/
     *
     * @return ：true/false。
     */
    public Boolean createExcel() {
        Log.i(TAG, F_L());
        List<String> allFileFromSkyAutoTester_list = showAllFileFromSkyAutoTester();
        if (null == allFileFromSkyAutoTester_list || allFileFromSkyAutoTester_list.size() == 0) {
            Log.i(TAG, F_L() + "目前SkyAutoTester下暂无测试报告，请稍后再试！");
            return false;
        }
        try {
            //即便文件夹下不为空，也要识别是不是属于测试报告
            Log.i(TAG, F_L());
            isTestReport(allFileFromSkyAutoTester_list);
            Log.i(TAG, F_L()+"111");
            ArrayList<String> partFileNameList = new ArrayList<>();
            setSheet("Test_Result");//创建sheet页
            Log.i(TAG, F_L()+"222");
            for (String fileNameList : allFileFromSkyAutoTester_list) {
                Log.i(TAG, F_L() + " - 有这些文件：" + fileNameList);
                if (FILE_UPDATELIST.equals(fileNameList)) {
                    UpGradeTotal total = ShowUpgradeInfo();
                    if (null == total) {
                        Log.i(TAG, F_L());
                        return false;
                    }
                    Log.i(TAG, F_L()+"333");
                    Map<String, List<UpGradeHistoryDetail>> history = total.getUpGradeHistoryInfo();//update.list里面的升级历史
                    Log.i(TAG, F_L()+"43");
                    if (null == history) {
                        Log.i(TAG, F_L() + "得到 " + FILE_UPDATELIST + " 里面的history内容为空");
                        return false;
                    }
                    String update_history = "";
                    Log.i(TAG, F_L());
                    List<UpGradeHistoryDetail> historyDetails = new ArrayList<>();
                    for (Map.Entry<String, List<UpGradeHistoryDetail>> entry : history.entrySet()) {
                        update_history = entry.getKey();
                        historyDetails = entry.getValue();
                    }
                    Log.i(TAG, F_L());
                    //update.list 文件格式特殊，故另作处理，后续可优化
                    BuildUpdateHistoryContent2Excel(update_history, historyDetails);
                    Log.i(TAG, F_L());
                }
            }
            /**** 统一文件格式的excel输出 START ***/
            for (String str : TestReportFileArr) {
                if (allFileFromSkyAutoTester_list.contains(str)) {
                    //由于update.list不属于通用格式，不在此处进行写入
                    if (str.equals(FILE_UPDATELIST)) {
                        break;
                    }
                    Log.i(TAG, F_L() + "当前遍历到文件【 " + str + " 】属于测试结果文档,将写入到excel");
                    partFileNameList.add(str);
                }
            }
            CommonBuildContent2Excel(partFileNameList);
            /**** 统一文件格式的excel输出 END ***/
            //写入文件
            writeToFile(TestReportFileName);
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error - 创建excel过程中出现错误");
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
            return false;
        }
        return true;
    }


    /**
     * 即便文件夹下不为空，但要识别是不是属于测试报告。如果不是测试报告就没有必要再往下写excel了
     *
     * @param allFileFromSkyAutoTester_list
     * @return
     */
    private boolean isTestReport(List<String> allFileFromSkyAutoTester_list) {
        for (String str : TestReportFileArr) {
            if (allFileFromSkyAutoTester_list.contains(str)) {
                return true;
            }
        }
        Log.i(TAG, F_L() + "目前SkyAutoTester下暂无测试报告，请稍后再试！");
        Log.i(TAG, F_L() + "或检查测试报告文件名是否符合TestReportFileArr规定");
        return false;
    }


    /**
     * 统一格式的文件输出到excle
     *
     * @param partFileNameList
     */
    private void CommonBuildContent2Excel(ArrayList<String> partFileNameList) {
        if (null == partFileNameList || partFileNameList.size() == 0) {
            return;
        }
        String section = "";
        List<String> contentList = null;
        try {
            for (String fileNameList : partFileNameList) {
                Log.i(TAG, F_L() + "--- 接收到的文件有：" + fileNameList);
                Map<String, List<String>> contentMap = DataIOUtils.GenericFormatDataResp(fileNameList);
                CreateHead(fileNameList);//创建表头
                for (Map.Entry<String, List<String>> entry : contentMap.entrySet()) {
                    section = entry.getKey();
                    contentList = entry.getValue();
                    for (String content_list : contentList) {
                        row = sheet.createRow(CURRENT_EXCEL_CONTENT_ROW_NUM);
                        row.createCell(0).setCellValue(section);
                        row.createCell(1).setCellValue(content_list);
                        CURRENT_EXCEL_CONTENT_ROW_NUM++;
                    }
                    //合并单元格
                    CellRangeAddress cra = MergeLineOrColumn(CURRENT_EXCEL_CONTENT_ROW_NUM - contentList.size(), CURRENT_EXCEL_CONTENT_ROW_NUM - 1, 0, 0);
                    sheet.addMergedRegion(cra);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, F_L() + " error - 格式解析出现错误");
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
        }
    }

    /***
     * 构建升级历史内容
     * @param update_history
     * @param historyDetails
     */
    private void BuildUpdateHistoryContent2Excel(String update_history, List<UpGradeHistoryDetail> historyDetails) {
        try {
            //设置表头
            CreateHead(FILE_UPDATELIST);
            //设置内容
            int size = historyDetails.size();
            for (int i = 0; i < size; i++) {
                Log.i(TAG, F_L() + " - CURRENT_EXCEL_CONTENT_ROW_NUM =" + CURRENT_EXCEL_CONTENT_ROW_NUM);
                row = sheet.createRow(CURRENT_EXCEL_CONTENT_ROW_NUM);//从CURRENT_EXCEL_CONTENT_ROW_NUM创建行
                row.createCell(0).setCellValue(update_history);
                ArrayList<String> keyList = new ArrayList<>();
                ArrayList<String> valList = new ArrayList<>();
                for (UpGradeHistoryDetail hisDetail : historyDetails) {
                    Map<String, String> detail = hisDetail.getDetail();
                    for (Map.Entry<String, String> entry : detail.entrySet()) {
                        keyList.add(entry.getKey());
                        valList.add(entry.getValue());
                    }
                }
                row.createCell(1).setCellValue(keyList.get(i));
                row.createCell(2).setCellValue(valList.get(i));
                CURRENT_EXCEL_CONTENT_ROW_NUM++;
            }
            CellRangeAddress cra = MergeLineOrColumn(CURRENT_EXCEL_CONTENT_ROW_NUM - size, CURRENT_EXCEL_CONTENT_ROW_NUM - 1, 0, 0);
            sheet.addMergedRegion(cra);
        } catch (Exception e) {
            Log.i(TAG, F_L() + " error - 格式解析出现错误");
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
        }
    }

    /**
     * 创建表头
     *
     * @param fileName :
     */
    public void CreateHead(String fileName) {
        //创建行
        row = sheet.createRow(CURRENT_EXCEL_CONTENT_ROW_NUM);
        //创建列
        cell = row.createCell(0);
        //设置列内容
        cell.setCellValue(fileName);
        //合并
        CellRangeAddress cra = MergeLineOrColumn(CURRENT_EXCEL_CONTENT_ROW_NUM, CURRENT_EXCEL_CONTENT_ROW_NUM, 0, TITLE_COLUMN_NUM);/**起始行,结束行 , 起始列, 结束列*/
        sheet.addMergedRegion(cra);
        CURRENT_EXCEL_CONTENT_ROW_NUM++;
    }

    /**
     * 合并行、列
     *
     * @return
     */
    public static CellRangeAddress MergeLineOrColumn(int qsh, int jsh, int qsl, int jsl) {
        CellRangeAddress cra = new CellRangeAddress(qsh, jsh, qsl, jsl);/**起始行,结束行 , 起始列, 结束列*/
        return cra;
    }


    /**
     * 创建sheet页
     *
     * @param sheetName
     */
    public static void setSheet(String sheetName) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }


    /**
     * 写入文件
     *
     * @param filePath
     */
    public static void writeToFile(String filePath) {
        file = new File(filePath);
        try {
            workbook.write(new FileOutputStream(file));
            Log.i(TAG, F_L() + " - 写入成功！");
            workbook.close();
        } catch (IOException e) {
            Log.i(TAG, F_L() + "error - 把数据写入到excel的时候发生了错误");
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
        }
    }

}