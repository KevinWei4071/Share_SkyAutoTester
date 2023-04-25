package com.skyworth.skyautotester.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 图片交互工具
 */
public class PicIOUtils {
    private final static String TAG = "SkyAutoTest";

    private static String Pic_File_Path = DataIOUtils.getPERFIXPATH() + "TVtest/";

    //要展示的图片张数
    private static int showPicNum = 5;

    public static int getShowPicNum() {
        return showPicNum;
    }

    public static String F_L() {
        return CommonUtils.getInstance().getFUNCTION_LINE();
    }

    /**
     * 获取图片
     * 限定 showPicNum 张，多出的取后5张
     *
     * @param picType
     * @return
     */
    public static List<Bitmap> getPic(String picType) {
        try {
            List<String> picList = showAllPicFromTvTest(true);
            if (picList.size() == 0 || null == picList) {
                Log.i(TAG, F_L() + "error - 暂无[" + picType + "]类型图片，稍后再试");
                return null;
            }

            //存储属于picType的图片
            ArrayList<String> resultPicList = new ArrayList<>();
            for (String orgin : picList) {
                if (orgin.contains(picType)) {
                    Log.i(TAG, F_L() + "文件" + orgin + "将被存到resultPicList");
                    resultPicList.add(orgin);
                }
            }
            ArrayList<Bitmap> resList = new ArrayList<>();
            StringBuffer fullPath = null;
            for (String str : resultPicList) {
                fullPath = new StringBuffer(Pic_File_Path);
                fullPath.append(str);
                Bitmap resBitMap = BitmapFactory.decodeFile(fullPath.toString());
                resList.add(resBitMap);
            }
            return resList;
        } catch (Exception e) {
            Log.i(TAG, F_L() + "- error ==> " + Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * 展示所有的图片,暂定5张后续优化
     *
     * @param isLimit：是否限制张数
     * @return
     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> showAllPicFromTvTest(boolean isLimit) {
        try {
            File file = new File(Pic_File_Path);
            Boolean existFile = DataIOUtils.isExistFile(file);
            if (!existFile) {
                Log.i(TAG, F_L() + " error - " + Pic_File_Path + " folder is not exist!! ");
                return null;
            }
            ArrayList<String> fileList = new ArrayList<>();
            File[] files = file.listFiles();
            HashMap<String, Date> compareMap = new HashMap<>();
            for (int i = 0; i < files.length; i++) {
                Log.i(TAG, F_L() + Pic_File_Path + " 下有这个文件" + files[i].getName());
                fileList.add(files[i].getName());

               /* if (isLimit) {
                    BasicFileAttributes attrs = Files.readAttributes(Paths.get(Pic_File_Path + files[i].getName()), BasicFileAttributes.class);
                    long millis = attrs.creationTime().toMillis();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    }
                    Date date = new Date();
                    date.setTime(millis);//当前文件的创建时间
                    compareMap.put(files[i].getName(), date);
                }*/


            }
            //限制图片数量，在一堆图片中取后5张
           /*  if (isLimit) {
                //如果本身就是小于等于5张，就不做处理
                if (fileList.size() <= showPicNum) {
                    return fileList;
                }
            }*/

            return fileList;
        } catch (Exception e) {
            Log.i(TAG, F_L() + "error");
            Log.i(TAG, F_L() + Log.getStackTraceString(e));
            return null;
        }
    }


}
