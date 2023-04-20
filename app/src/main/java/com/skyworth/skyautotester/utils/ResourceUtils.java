package com.skyworth.skyautotester.utils;

import android.content.Context;

public class ResourceUtils
{
    public static String getText(Context context, String name)
    {
        String result = null;
        try
        {
            result = context.getResources().getText(
                            context.getResources().getIdentifier(name, "string", context.getPackageName()))
                    .toString();
            if (result == null)
            {
                result = name;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getText(Context context, int resId)
    {
        String result = "" + resId;
        try
        {
            result = context.getResources().getText(resId).toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getDrawableIdentify(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static int getTextIdentify(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

}

