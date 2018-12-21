package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.bean.UserInfo;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.UserInfoHandler;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class UserDetailActivity extends BaseActivity implements IRequestListener
{


    private static final String GET_USER_INFO = "get_user_info";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_sameTimeSize)
    TextView tvSameTimeSize;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(UserDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    UserInfo userInfo = mUserInfoHandler.getUserInfo();

                    if (null != userInfo)
                    {
                        tvName.setText(userInfo.getName());
                        tvPhone.setText(userInfo.getPhone());
                        tvSameTimeSize.setText(userInfo.getSameTimeSize());
                        tvStatus.setText("1".equals(userInfo.getStatus()) ? "已启用" : "暂停使用");
                        tvAddress.setText(StringUtils.stringIsEmpty(userInfo.getAddress()) ? "暂无地址" : userInfo.getAddress());
                    }

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(UserDetailActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_user_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(UserDetailActivity.this, false);

    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("个人信息");


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!NetWorkUtil.isConn(this))
        {
            NetWorkUtil.showNoNetWorkDlg(this);
            return;
        }
        showProgressDialog();
        Map<String, String> postMap = new HashMap<>();
        DataRequest.instance().request(UserDetailActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_INFO, postMap, new UserInfoHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_USER_INFO.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if(v == ivBack)
        {
            finish();
        }
    }
}
