/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Log工具，类似android.util.Log。
 * tag自动产生，格式: customTagPrefix:className[methodName,lineNumber],
 * customTagPrefix为空时只输出：className[methodName,lineNumber]。
 * <p/>
 * Author: wyouflf
 * Date: 13-7-24
 * Time: 下午12:23
 */

/**
 *
 * 添加Log level 控制与对象支持。
 *
 */
public class LogUtils {

    public static String customTagPrefix = "";

    private LogUtils() {
    }

    // default level
    public static int LEVEL = LogUtils.INFO;

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s()[%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static <T> void d(T msg) {
        if (LEVEL < DEBUG ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
        String content = msg == null? "null": msg.toString();
        Log.d(tag, content);
    }

    public static <T> void d(T msg, Throwable tr) {
        if (LEVEL < DEBUG ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.d(tag, content, tr);
    }

    public static <T> void e(T msg) {
        if (LEVEL < ERROR ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.e(tag, content);
    }

    public static <T> void e(T msg, Throwable tr) {
        if (LEVEL < ERROR ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.e(tag, content, tr);
    }

    public static <T> void i(T msg) {
        if (LEVEL < INFO ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.i(tag, content);
    }

    public static <T> void i(T msg, Throwable tr) {
        if (LEVEL < INFO ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.i(tag, content, tr);
    }

    public static <T> void v(T msg) {
        if (LEVEL < VERBOSE ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.v(tag, content);
    }

    public static <T> void v(T msg, Throwable tr) {
        if (LEVEL < VERBOSE ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.v(tag, content, tr);
    }

    public static <T> void w(T msg) {
        if (LEVEL < WARN ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.w(tag, content);
    }

    public static <T> void w(T msg, Throwable tr) {
        if (LEVEL < WARN ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (LEVEL < WARN) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
        Log.w(tag, tr);
    }


    public static <T> void wtf(T msg) {
        if (LEVEL < ASSERT ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.wtf(tag, content);
    }

    public static <T> void wtf(T msg, Throwable tr) {
        if (LEVEL < ASSERT ) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
       String content = msg == null? "null": msg.toString();
        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        if (LEVEL < ASSERT) return;
        StackTraceElement caller = OtherUtils.getCallerMethodName();
        String tag = generateTag(caller);
        Log.wtf(tag, tr);
    }

}
