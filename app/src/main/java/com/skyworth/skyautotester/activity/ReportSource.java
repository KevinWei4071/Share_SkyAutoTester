package com.skyworth.skyautotester.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.skyworth.skyautotester.Defines;
import com.skyworth.skyautotester.R;
import com.skyworth.skyautotester.utils.CommonUtils;
import com.skyworth.skyautotester.utils.PicIOUtils;

import java.util.List;

public class ReportSource extends AppCompatActivity {
    private final String TAG = "SkyAutoTest";

    public static String F_L() {
        return CommonUtils.getInstance().getFUNCTION_LINE();
    }

    int showPicNum = PicIOUtils.getShowPicNum();

    ImageView im1, im2, im3, im4, im5, im6, im7, im8, im9, im10, im11, im12, im13, im14, im15, im16, im17, im18, im19, im20, im21, im22, im23, im24, im25, im26, im27, im28, im29, im30 = null;
    /*TextView Av_tv, Hdmi1_tv, Hdmi2_tv, Hdmi3_tv, Hdmi4_tv, Spdif_av = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_source);
        im1 = (ImageView) findViewById(R.id.imageView1);
        im2 = (ImageView) findViewById(R.id.imageView2);
        im3 = (ImageView) findViewById(R.id.imageView3);
        im4 = (ImageView) findViewById(R.id.imageView4);
        im5 = (ImageView) findViewById(R.id.imageView5);
        im6 = (ImageView) findViewById(R.id.imageView6);
        im7 = (ImageView) findViewById(R.id.imageView7);
        im8 = (ImageView) findViewById(R.id.imageView8);
        im9 = (ImageView) findViewById(R.id.imageView9);
        im10 = (ImageView) findViewById(R.id.imageView10);
        im11 = (ImageView) findViewById(R.id.imageView11);
        im12 = (ImageView) findViewById(R.id.imageView12);
        im13 = (ImageView) findViewById(R.id.imageView13);
        im14 = (ImageView) findViewById(R.id.imageView14);
        im15 = (ImageView) findViewById(R.id.imageView15);
        im16 = (ImageView) findViewById(R.id.imageView16);
        im17 = (ImageView) findViewById(R.id.imageView17);
        im18 = (ImageView) findViewById(R.id.imageView18);
        im19 = (ImageView) findViewById(R.id.imageView19);
        im20 = (ImageView) findViewById(R.id.imageView20);
        im21 = (ImageView) findViewById(R.id.imageView21);
        im22 = (ImageView) findViewById(R.id.imageView22);
        im23 = (ImageView) findViewById(R.id.imageView23);
        im24 = (ImageView) findViewById(R.id.imageView24);
        im25 = (ImageView) findViewById(R.id.imageView25);
        im26 = (ImageView) findViewById(R.id.imageView26);
        im27 = (ImageView) findViewById(R.id.imageView27);
        im28 = (ImageView) findViewById(R.id.imageView28);
        im29 = (ImageView) findViewById(R.id.imageView29);
        im30 = (ImageView) findViewById(R.id.imageView30);
        ImageView[] ivArr = {im1, im2, im3, im4, im5,
                im6, im7, im8, im9, im10,
                im11, im12, im13, im14, im15,
                im16, im17, im18, im19, im20,
                im21, im22, im23, im24, im25,
                im26, im27, im28, im29, im30};
        /*Av_tv = (TextView) findViewById(R.id.AV_VIEW);
        Hdmi1_tv = (TextView) findViewById(R.id.HMDI1_VIEW);
        Hdmi2_tv = (TextView) findViewById(R.id.HMDI2_VIEW);
        Hdmi3_tv = (TextView) findViewById(R.id.HMDI3_VIEW);
        Hdmi4_tv = (TextView) findViewById(R.id.HMDI4_VIEW);
        Spdif_av = (TextView) findViewById(R.id.SPDIF_VIEW);*/
        try {
            int arrFlag = 0;
            //AV
            List<Bitmap> AV_pic = PicIOUtils.getPic(Defines.PIC_TYPE_AV);
            if (null != AV_pic && AV_pic.size() != 0) {
                for (int i = arrFlag; i < AV_pic.size(); i++) {
                    ivArr[i].setImageBitmap(AV_pic.get(i));
                    arrFlag = i;
                }
            } else {
                Log.i(TAG, F_L() + "AV_pic is null");
            }
            arrFlag = LineFeed(arrFlag);
            Log.i(TAG, F_L() + "换行，将于下标[" + arrFlag + "]输出数据");
            //hdmi1
            List<Bitmap> HDMI1_pic = PicIOUtils.getPic(Defines.PIC_TYPE_HDMI1);
            if (null != HDMI1_pic && HDMI1_pic.size() != 0) {
                for (int i = 0; i < HDMI1_pic.size(); i++) {
                    ivArr[arrFlag].setImageBitmap(HDMI1_pic.get(i));
                    arrFlag++;
                }
            } else {
                Log.i(TAG, F_L() + "HDMI1_pic is null");
            }

            //����
            arrFlag = LineFeed(arrFlag);
            Log.i(TAG, F_L() + "换行，将于下标[" + arrFlag + "]输出数据");
            //hdmi2
            List<Bitmap> HDMI2_pic = PicIOUtils.getPic(Defines.PIC_TYPE_HDMI2);
            if (null != HDMI2_pic && HDMI2_pic.size() != 0) {
                for (int i = 0; i < HDMI2_pic.size(); i++) {
                    ivArr[arrFlag].setImageBitmap(HDMI2_pic.get(i));
                    arrFlag++;
                }
            } else {
                Log.i(TAG, F_L() + "HDMI2_pic is null");
            }

            //����
            arrFlag = LineFeed(arrFlag);
            Log.i(TAG, F_L() + "换行，将于下标[" + arrFlag + "]输出数据");
            //hdmi3
            List<Bitmap> HDMI3_pic = PicIOUtils.getPic(Defines.PIC_TYPE_HDMI3);
            if (null != HDMI3_pic && HDMI3_pic.size() != 0) {
                for (int i = 0; i < HDMI3_pic.size(); i++) {
                    ivArr[arrFlag].setImageBitmap(HDMI3_pic.get(i));
                    arrFlag++;
                }
            } else {
                Log.i(TAG, F_L() + "HDMI3_pic is null");
            }

            //����
            arrFlag = LineFeed(arrFlag);
            Log.i(TAG, F_L() + "换行，将于下标[" + arrFlag + "]输出数据");
            //hdmi4
            List<Bitmap> HDMI4_pic = PicIOUtils.getPic(Defines.PIC_TYPE_HDMI4);
            if (null != HDMI4_pic && HDMI4_pic.size() != 0) {
                for (int i = 0; i < HDMI4_pic.size(); i++) {
                    ivArr[arrFlag].setImageBitmap(HDMI4_pic.get(i));
                    arrFlag++;
                }
            } else {
                Log.i(TAG, F_L() + "HDMI4_pic is null");
            }

            //����
            arrFlag = LineFeed(arrFlag);
            Log.i(TAG, F_L() + "换行，将于下标[" + arrFlag + "]输出数据");
            //spdif
            List<Bitmap> vod_pic = PicIOUtils.getPic(Defines.PIC_TYPE_VOD);
            if (null != vod_pic && vod_pic.size() != 0) {
                for (int i = 0; i < vod_pic.size(); i++) {
                    ivArr[arrFlag].setImageBitmap(vod_pic.get(i));
                    arrFlag++;
                }
            } else {
                Log.i(TAG, F_L() + "vod_pic is null");
            }
        } catch (Exception e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * ����
     *
     * @param currentLine
     * @return
     */
    public int LineFeed(int currentLine) {
        if (currentLine == 0) {
            return 0;
        }
        int res = 0;
        if (currentLine <= showPicNum) {
            res = showPicNum;
        } else {
            res = ((currentLine / showPicNum) + 1) * showPicNum;
        }
        return res;
    }

}
