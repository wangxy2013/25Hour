package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jyq.wm.R;

import butterknife.BindView;

public class WelComeActivity extends BaseActivity
{
    @BindView(R.id.tv_tips)
    TextView tvTips;

    private int time = 3;

    private static final int UPDATE_TIME = 0x01;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(WelComeActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UPDATE_TIME:
                    tvTips.setText(time + "s");


                    if (time > 0)
                    {
                        time--;
                        mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
                    }
                    else
                    {
                        startActivity(new Intent(WelComeActivity.this, LoginActivity.class));
                        finish();
                    }
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void initEvent()
    {
        mHandler.sendEmptyMessage(UPDATE_TIME);
    }

    @Override
    protected void initViewData()
    {

    }


}
