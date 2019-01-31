package com.jyq.qs.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.jyq.qs.utils.DialogUtils;
import com.jyq.qs.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 描述：一句话简单描述
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener
{

    Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initData();
        initViews(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        ;//注册黄油刀
        initEvent();
        initViewData();
    }


    /**
     * 从intent中获取数据
     */
    protected abstract void initData();

    /**
     * 存放从xml中获取ui,例如 findViewById
     */
    protected abstract void initViews(Bundle savedInstanceState);


    /**
     * 初始化页面UI事件,例如 setOnClickListener
     */
    protected abstract void initEvent();

    /**
     * 存放刷新页面的代码和初始化数据
     */
    protected abstract void initViewData();

    private String mPageName = getClass().getSimpleName();

    @Override
    protected void onPause()
    {
        super.onPause();
        //SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        //        MobclickAgent.onPageEnd(mPageName);
       // MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        //        MobclickAgent.onPageStart(mPageName);
        //MobclickAgent.onResume(this);
    }

    @Override
    public void onClick(View v)
    {
        if (isFastClick())
        {
            return;
        }

    }

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick()
    {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME)
        {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
    private RequestPermissionCallBack mRequestPermissionCallBack;
    private final int mRequestCode = 1024;
    /**
     * 权限请求结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        StringBuilder  permissionName = new StringBuilder();
        for (String s: permissions) {
            permissionName = permissionName.append(s + "\r\n");
        }
        switch (requestCode) {
            case mRequestCode: {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false;
                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                        // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            new AlertDialog.Builder(this)
                                    .setMessage("【用户选择了不在提示按钮，或者系统默认不在提示（如MIUI）。" +
                                            "引导用户到应用设置页去手动授权,注意提示用户具体需要哪些权限】\r\n" +
                                            "获取相关权限失败:\r\n" +
                                            permissionName +
                                            "将导致部分功能无法正常使用，需要到设置页面手动授权")
                                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mRequestPermissionCallBack.denied();
                                        }
                                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mRequestPermissionCallBack.denied();
                                }
                            }).show();

                        } else {
                            //用户拒绝权限请求，但未选中“不再提示”选项
                            mRequestPermissionCallBack.denied();
                        }
                        break;
                    }
                }
                if (hasAllGranted) {
                    mRequestPermissionCallBack.granted();
                }
            }
        }
    }

    /**
     * 发起权限请求
     *
     * @param context
     * @param permissions
     * @param callback
     */
    public void requestPermissions(final Context context, final String[] permissions,
                                   RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for(String s : permissions){
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(context)
                            .setMessage("【用户曾经拒绝过你的请求，所以这次发起请求时解释一下】\r\n" +
                                    "您好，需要如下权限：\r\n" +
                                    permissionNames+
                                    " 请允许，否则将影响部分功能的正常使用。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                }

                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    /**
     * 权限请求结果回调接口
     */
    interface RequestPermissionCallBack {
        /**
         * 同意授权
         */
        public void granted();

        /**
         * 取消授权
         */
        public void denied();
    }


    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }


    private Dialog mProgressDialog = null;

    /**
     * @return void
     * @throws [异常类型] [异常说明]
     * @see
     */
    public void showProgressDialog()
    {

        if (isFinishing())
        {
            return;
        }
        LogUtil.e("TAG", "showProgressDialog");
        if (mProgressDialog == null)
        {
            mProgressDialog = DialogUtils.createLoadingDialog(this, "加载中...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog()
    {
        if (isFinishing())
        {
            return;
        }
        LogUtil.e("TAG", "hideProgressDialog");
        if (null != mProgressDialog)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
