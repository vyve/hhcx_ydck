package com.estar.hh.survey.utils;


import android.util.Log;

import java.util.Locale;

/**
 * Log统一管理类
 */
public class LogUtils {
    public static boolean isOpen = true;

    public static void v(String mess) {
        if (isOpen) {
            Log.v(getTag(), buildMessage(mess));
        }
    }

    public static void d(String mess) {
        if (isOpen) {
            Log.d(getTag(), buildMessage(mess));
        }
    }

    public static void i(String mess) {
        if (isOpen) {
            Log.i(getTag(), buildMessage(mess));
        }
    }

    public static void w(String mess) {
        if (isOpen) {
            Log.w(getTag(), buildMessage(mess));
        }
    }

    public static void e(String mess) {
        if (isOpen) {
            Log.e(getTag(), buildMessage(mess));
        }
    }

    public static void v(String tag, String mess) {
        if (isOpen) {
            Log.v(getTag(), buildMessage(mess));
        }
    }

    public static void d(String tag, String mess) {
        if (isOpen) {

            Log.d(getTag(), buildMessage(mess));
        }
    }

    public static void i(String tag, String mess) {
        if (isOpen) {
            Log.i(getTag(), buildMessage(mess));
        }
    }

    public static void w(String tag, String mess) {
        if (isOpen) {
            Log.w(getTag(), buildMessage(mess));
        }
    }

    public static void e(String tag, String mess) {
        if (isOpen) {

            Log.e(getTag(), buildMessage(mess));
        }
    }


    /**
     * 获取TAG,为类名
     *
     * @return
     */
    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                break;
            }
        }
        return callingClass;
    }

    /**
     * 获取线程名,所在的方法名
     *
     * @param msg
     * @return
     */
    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils.class)) {
                caller = trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread()
                .getId(), caller, msg);
    }
}