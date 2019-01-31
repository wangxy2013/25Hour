package com.jyq.qs.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyq.qs.R;
import com.jyq.qs.listener.MyItemClickListener;
import com.jyq.qs.listener.MyOnClickListener;


/**
 * 描述：一句话简单描述
 */
public class DialogUtils
{


    public static void showVersionUpdateDialog(Context mContext, String content, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_version, null);
        dialog.setContentView(view);
        TextView mContent = (TextView) view.findViewById(R.id.tv_content);
        Button mSubmitBtn = (Button) view.findViewById(R.id.btn_submit);
        Button mCancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        TextView mLinkTv = (TextView) view.findViewById(R.id.tv_link);
        mLinkTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mLinkTv.getPaint().setAntiAlias(true);//抗锯齿


        mContent.setText(content);
        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
                listener.onSubmit("1");
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit("2");
            }
        });

        mLinkTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("3");
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 提示框
     *
     * @return
     */
    public static void showPromptDialog(Context mContext, String title, final MyItemClickListener listener)
    {
        if (!((Activity) mContext).isFinishing())
        {
            final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
            dialog.setCancelable(true);
            View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
            dialog.setContentView(v);
            ((TextView) v.findViewById(R.id.tv_content)).setText(title);
            v.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(v, 0);
                    dialog.dismiss();
                }
            });

            //Dialog部分
            Window mWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = APPUtils.getScreenWidth(mContext) * 3 / 4;
            mWindow.setAttributes(lp);
            dialog.setCancelable(false);
            dialog.show();
        }
    }




    /**
     * 温馨提示
     *
     * @return
     */
    public static void showToastDialog2Button(Context mContext, String str, final View.OnClickListener onClickListener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_2_button, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_title);
        mTitle.setText(str);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }


    public static Dialog createLoadingDialog(Context context, String msg)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading1, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        return loadingDialog;
    }


}
