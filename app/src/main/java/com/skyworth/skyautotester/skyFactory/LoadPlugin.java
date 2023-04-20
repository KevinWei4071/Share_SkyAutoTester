package com.skyworth.skyautotester.skyFactory;

import android.content.Context;

import com.tianci.plugins.factory.BaseFactoryManager;

import java.io.File;

import dalvik.system.DexClassLoader;

public class LoadPlugin
{
    private final static String pluginFilePath = "/system/plugins/AtomicFactoryPlugin.jar";
    private final static String pluginClazz = "com.skyworth.factory.FactoryManager";
    private static BaseFactoryManager mFactoryManager = null;
    /**
     * 动态加载jar包依赖
     * @param context
     * @return
     */
    public static BaseFactoryManager LoadFactoryManager(Context context)
    {
        if (new File(pluginFilePath).exists())
        {
            DexClassLoader loader = new DexClassLoader(pluginFilePath,
                    context.getDir("dex", 0).getAbsolutePath(), null, context.getClassLoader());
            try
            {
                mFactoryManager = (BaseFactoryManager) loader.loadClass(pluginClazz).newInstance();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return mFactoryManager;
    }
}
