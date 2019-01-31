package com.jyq.qs.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.jyq.qs.utils.DialogUtils;
import com.jyq.qs.utils.LogUtil;

/**
 * 描述：一句话简单描述
 */
public abstract class BaseFragment extends Fragment
{
    private String title;

    /**
     * 从intent中获取数据
     */
    protected abstract void initData();

    /**
     * 存放从xml中获取ui,例如 findViewById
     */
    protected abstract void initViews();


    /**
     * 初始化页面UI事件,例如 setOnClickListener
     */
    protected abstract void initEvent();

    /**
     * 存放刷新页面的代码和初始化数据
     */
    protected abstract void initViewData();

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    private Dialog mProgressDialog = null;

    /**
     * @return void
     * @throws [异常类型] [异常说明]
     * @see
     */
    public void showProgressDialog(Activity activity)
    {

        if (activity.isFinishing())
        {
            return;
        }
        LogUtil.e("TAG", "showProgressDialog");
        if (mProgressDialog == null)
        {
            mProgressDialog = DialogUtils.createLoadingDialog(activity, "加载中...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog(Activity activity)
    {
        if (activity.isFinishing())
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

}
