package com.jyq.qs.activity;

import android.content.Context;
import android.os.Handler;

/**
 * 描述：为了防止内存泄漏，定义外部类，防止内部类对外部类的引用
 */
public class BaseHandler extends Handler
{
    Context context;

    public BaseHandler(Context context) {
        this.context = context;
    }
};