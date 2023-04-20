package com.skyworth.skyautotester.utils;

import android.util.Log;


import com.skyworth.skyautotester.entity.UpGradeHistoryDetail;
import com.skyworth.skyautotester.entity.UpGradeTotal;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于从data下进行数据交互的工具
 */
public class DataIOUtils {

    private final static String TAG = "SkyAutoTest";
    private final static String PERFIXPATH = "/data/SkyAutoTester/";


    public static String F_L() {
        return CommonUtils.getInstance().getFUNCTION_LINE();
    }

    /**
     * 检查/data/下是否有SkyAutoTester文件夹
     *
     * @return
     */
    public static Boolean IsExistSkyAutoTester() {
        File file = new File("/data/SkyAutoTester");
        if (file.exists()) {
            if (file.isDirectory()) {
                Log.i(TAG, F_L() + "SkyAutoTester folder has already exist!!");
                return true;
            }
        } else {
            Log.i(TAG, F_L() + "SkyAutoTester folder has not  exist!!");
            return false;
        }
        return false;
    }

    /**
     * 在/data/下创建SkyAutoTester文件夹
     */
    public static void createSkyAutoTester() throws IOException {
        Boolean isExist = IsExistSkyAutoTester();
        if (!isExist) {
            File file = new File("/data/SkyAutoTester");
            boolean mkdirs = false;
            mkdirs = file.mkdirs();
            if (mkdirs) {
                try {
                    Runtime.getRuntime().exec("chmod -R 777 /data/SkyAutoTester");
                } catch (Exception e) {

                }
                Log.i(TAG, F_L() + "SkyAutoTester folder created successfully");
            } else {
                Log.i(TAG, F_L() + "SkyAutoTester folder not created ");
            }

        }
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @return
     */
    public static Boolean isExistFile(File file) {
        if (file.exists()) {
            Log.i(TAG, F_L() + " file => " + file.getName() + " has exist");
            return true;
        } else {
            Log.i(TAG, F_L() + " error - file => " + file.getName() + " has not exist");
            return false;
        }
    }


    /**
     * 【/data/SkyAutoTester】
     * 读取传入文件名的数据
     *
     * @return
     */
    public static String ReadDataByIO(String FileName) {
        FileInputStream fin = null;
        BufferedReader buffReader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            String filePath = "";
            filePath = PERFIXPATH + FileName;
            File file = new File(filePath);//完整文件路径
            Boolean existFolder = IsExistSkyAutoTester();
            Boolean existFile = isExistFile(file);
            if (!existFolder || !existFile) {
                if (!existFolder) {
                    try {
                        createSkyAutoTester();
                    } catch (IOException e) {
                        Log.i(TAG, F_L() + "error - An exception occurs when creating a folder");
                        e.printStackTrace();
                        return null;
                    }
                }
                return null;
            }
            fin = new FileInputStream(file);
            buffReader = new BufferedReader(new InputStreamReader(fin));
            String strTmp = "";
            while ((strTmp = buffReader.readLine()) != null) {
                Log.i(TAG, F_L() + "Text's data each line => " + strTmp);
                sbf.append(strTmp);
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, F_L() + "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(TAG, F_L() + "IOException");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.i(TAG, F_L() + "Exception");
            e.printStackTrace();
            return null;
        } finally {
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException e) {
                    Log.i(TAG, F_L() + "error - An error occurred during BufferedReader closing");
                    e.printStackTrace();
                    return null;
                }
            }
        }
        if (sbf == null || sbf.length() == 0 || "".equals(sbf.toString())) {
            Log.i(TAG, F_L() + "error - ReadInfoBufferfromData is null");
            return null;
        }
        return sbf.toString();
    }

    /**
     * 拼接usb升级包列表信息、升级历史
     *
     * @param upGradeTotal
     * @return
     */
    public static UpGradeTotal SplicingUpGradeInfoFromData(UpGradeTotal upGradeTotal, String readDataByIO) {
        if (StringUtils.isEmpty(readDataByIO)) {
            return null;
        }
        Map<String, List<String>> upGradeList = new HashMap<>();
        Map<String, List<UpGradeHistoryDetail>> upGradeHistoryInfo = new HashMap<>();
        ArrayList<String> upGrade_list = new ArrayList<>();
        ArrayList<UpGradeHistoryDetail> upgrade_history_list = new ArrayList<>();
        String upgrade_history_section = "upgrade_history";
        String upgrade_list_section = "upgrade_list";
        /**************** 对字符串进行处理 START  ****************/
        Map<String, List<String>> map = CommonSplitDataUtils(readDataByIO);
        if (null == map) {
            Log.i(TAG, F_L() + "error-分割数据格式出现错误");
            return null;
        }
        try {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String sectionName = entry.getKey();
                if (upgrade_list_section.equals(sectionName)) {
                    for (String list : entry.getValue()) {
                        upGrade_list.add(list);
                    }
                    upGradeList.put(upgrade_list_section, upGrade_list);
                } else if (upgrade_history_section.equals(sectionName)) {
                    List<String> upgrade_history_temp_content = entry.getValue();
                    for (int i = 0; i < upgrade_history_temp_content.size(); i++) {
                        String[] split_arr = upgrade_history_temp_content.get(i).split("=");
                        HashMap<String, String> upgrade_detail_map = new HashMap<>();
                        upgrade_detail_map.put(split_arr[0], split_arr[1]);
                        UpGradeHistoryDetail upGradeHistoryDetail = new UpGradeHistoryDetail();
                        upGradeHistoryDetail.setDetail(upgrade_detail_map);
                        upgrade_history_list.add(upGradeHistoryDetail);
                    }
                    upGradeHistoryInfo.put(upgrade_history_section, upgrade_history_list);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error");
            e.printStackTrace();
            return null;
        }
        /**************** 对字符串进行处理 END  ****************/
        upGradeTotal.setUpGradeList(upGradeList);
        upGradeTotal.setUpGradeHistoryInfo(upGradeHistoryInfo);
        return upGradeTotal;
    }

    /**
     * 正则表达式截取字符串中多个被[]包含的内容
     *
     * @param msg
     * @return
     */
    public static ArrayList<String> extractMessageByRegular(String msg) {
        @SuppressWarnings("unchecked")
        ArrayList<String> list = new ArrayList();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }


    /**
     * 拆分数据通用工具
     * 文本格式为：
     * [xxxx]
     * xxxxxxxxxxxxxxxxxxxxxxxx;
     * xxxxxxxxxxxxxxxxxxxxxxxx;
     * [xxxx]
     * xxxxxxxxxxxxxxxxxxxxxxxx;
     * xxxxxxxxxxxxxxxxxxxxxxxx;
     * ........
     */
    public static Map<String, List<String>> CommonSplitDataUtils(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        HashMap<String, List<String>> resultMap = new HashMap<>();
        try {
            ArrayList<String> section_list = extractMessageByRegular(data);
            int sectionSize = section_list.size();
            if (sectionSize > 0) {
                if (sectionSize == 1) {//只有一个section的情况
                    String oneSelfSection = section_list.get(0);
                    ArrayList<String> contentList = new ArrayList<>();
                    String oneSelfSection_Content = StringUtils.substringAfter(data, "[" + oneSelfSection + "]");
                    String[] oneSelfSection_Content_Arr = oneSelfSection_Content.split(";");
                    for (int i = 0; i < oneSelfSection_Content_Arr.length; i++) {
                        contentList.add(oneSelfSection_Content_Arr[i]);
                    }
                    resultMap.put(oneSelfSection, contentList);
                    return resultMap;
                }
                if (sectionSize > 1) {
                    ArrayList<String> doubleSectionList = null;
                    for (int i = 0; i < section_list.size() - 1; i++) {
                        String section_tmp_content = StringUtils.substringBetween(data, "[" + section_list.get(i) + "]", "[" + section_list.get(i + 1) + "]");
                        String[] secton_arr = section_tmp_content.split(";");
                        String sectionName = section_list.get(i);
                        doubleSectionList = new ArrayList<>();
                        for (int j = 0; j < secton_arr.length; j++) {
                            doubleSectionList.add(secton_arr[j]);
                        }
                        resultMap.put(sectionName, doubleSectionList);
                    }
                    String lastSection = section_list.get(sectionSize - 1);
                    String lastSection_tmp_content = StringUtils.substringAfter(data, "[" + lastSection + "]");
                    String[] lastSection_arr = lastSection_tmp_content.split(";");
                    ArrayList<String> lastSection_content = new ArrayList<>();
                    for (int i = 0; i < lastSection_arr.length; i++) {
                        lastSection_content.add(lastSection_arr[i]);
                    }
                    resultMap.put(lastSection, lastSection_content);
                    return resultMap;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error - 拆分数据通用工具异常，分析文本格式是否符合约定");
            e.printStackTrace();
            return null;
        }
        return null;
    }


    /**
     * 展示升级相关信息
     *
     * @return
     */
    public static UpGradeTotal ShowUpgradeInfo() {
        String readFileName = "update.list";
        UpGradeTotal upGradeTotal = new UpGradeTotal();
        try {
            String readDataByIO = ReadDataByIO(readFileName);
            SplicingUpGradeInfoFromData(upGradeTotal, readDataByIO);
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error");
            e.printStackTrace();
            return null;
        }
        return upGradeTotal;
    }

    /**
     * 通用格式文本数据返回
     * 需输入文件名
     *
     * @return
     */
    public static Map<String, List<String>> GenericFormatDataResp(String fileName) {
        Map<String, List<String>> map = null;
        try {
            String readDataByIO = ReadDataByIO(fileName);
            map = CommonSplitDataUtils(readDataByIO);
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error");
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 展示/data/SkyAutoTester/下所有的文件
     *
     * @return
     */
    public static List<String> showAllFileFromSkyAutoTester() {
        try {
            File file = new File(PERFIXPATH);
            Boolean existFile = isExistFile(file);
            if (!existFile) {
                Log.i(TAG, F_L() + " error - " + PERFIXPATH + " folder is not exist!! ");
                return null;
            }
            ArrayList<String> fileList = new ArrayList<>();
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                fileList.add(files[i].getName());
            }
            return fileList;
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查/data/SkyAutoTester/下已存在的测试报告的结果
     *
     * @param FileName : 传入要获取结果的文件名，如 xxx.txt
     * @param FlagName : 传入表明失败的标准是什么？SUCCESS还是FAILED代表失败？
     * @return : 全部测试项pass返回 1 ，否则返回 0
     */
    public Map<String, Integer> getTestStatus(String FileName, String FlagName) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        Map<String, List<String>> map = GenericFormatDataResp(FileName);
        if (null == map) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (FlagName.equals(entry.getKey())) {
                resultMap.put(FileName, Integer.valueOf(0));
                return resultMap;
            }
        }
        resultMap.put(FileName, Integer.valueOf(1));
        return resultMap;
    }
}
