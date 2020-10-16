package com.yollpoll.hotfix;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by spq on 2020/10/16
 */
public class HotFix {

    /**
     * 修复指定的类
     *
     * @param context        上下文环境
     * @param fixDexFilePath 修复文件路径
     */
    public static void fixDexFile(Context context, String fixDexFilePath) {
        if (null != fixDexFilePath && new File(fixDexFilePath).exists()) {
            try {
                injectDexToClassLoader(context, fixDexFilePath);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入修复文件到classloader
     *
     * @param context        上下文环境
     * @param fixDexFilePath 文件地址
     */
    private static void injectDexToClassLoader(Context context, String fixDexFilePath) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        //两次反射获取到原始dexElements
        Object basePathList = getPathList(pathClassLoader);
        Object dexElements = getDexElements(basePathList);

        //读取 文件中fixElements
        String baseDexAbsolutePath = context.getDir("dex", 0).getAbsolutePath();
        DexClassLoader fixDexClassLoader = new DexClassLoader(fixDexFilePath, baseDexAbsolutePath,
                fixDexFilePath, context.getClassLoader());
        Object fixPathList = getPathList(fixDexClassLoader);
        Object fixElements = getDexElements(fixPathList);

        //合并两个dexElements
        Object newElements = combineArray(dexElements, fixElements);
        //一定要重新获取，不要用basePathList，会报错
        Object basePathList2 = getPathList(pathClassLoader);
        ReflectUtils.setField(basePathList2, basePathList2.getClass(), "dexElements", newElements);
    }

    /**
     * 通过反射获取pathList
     *
     * @param obj pathClassLoader
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return ReflectUtils.getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }


    /**
     * 进一步获取到dexElements
     *
     * @param obj PathList
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException {
        return ReflectUtils.getField(obj, obj.getClass(), "dexElements");
    }

    /**
     * 合并两个dex数组
     *
     * @param dexElements 原始dexElements
     * @param fixElements 需要合并的数组
     * @return 合并后数据
     */
    private static Object combineArray(Object dexElements, Object fixElements) {
        //获取数组的class对象
        Class componentType = fixElements.getClass().getComponentType();
        int length = Array.getLength(fixElements);
        //合并后数组的长度
        int length2 = Array.getLength(dexElements) + length;
        //反射获取新的数组
        Object newArray = Array.newInstance(componentType, length2);
        for (int i = 0; i < length2; i++) {
            if (i < length) {
                Array.set(newArray, i, Array.get(fixElements, i));
            } else {
                Array.set(newArray, i, Array.get(dexElements, i - length));
            }
        }
        return newArray;
    }
}
