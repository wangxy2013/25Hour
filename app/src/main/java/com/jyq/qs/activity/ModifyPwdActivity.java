package com.jyq.qs.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.qs.R;
import com.jyq.qs.http.DataRequest;
import com.jyq.qs.http.HttpRequest;
import com.jyq.qs.http.IRequestListener;
import com.jyq.qs.json.ResultHandler;
import com.jyq.qs.listener.MyItemClickListener;
import com.jyq.qs.utils.ConfigManager;
import com.jyq.qs.utils.ConstantUtil;
import com.jyq.qs.utils.DialogUtils;
import com.jyq.qs.utils.ToastUtil;
import com.jyq.qs.utils.Urls;
import com.jyq.qs.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ModifyPwdActivity extends BaseActivity implements IRequestListener
{


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_new_pwd1)
    EditText etNewPwd1;
    @BindView(R.id.tv_modify)
    TextView tvModify;


    private static final String USER_MODIFY_PWD = "user_modify_pwd";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(ModifyPwdActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    //                    ToastUtil.show(ModifyPwdActivity.this, "密码修改成功,请重新登录");

                    DialogUtils.showPromptDialog(ModifyPwdActivity.this, "密码修改成功,请重新登录", new MyItemClickListener()

                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });


                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(ModifyPwdActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_modify_pwd);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(ModifyPwdActivity.this, false);

    }

    @Override
    protected void initEvent()
    {
        tvModify.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("修改密码");

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tvModify)
        {
            String oldPwd = etOldPwd.getText().toString();
            String newPwd = etNewPwd.getText().toString();
            String newPwd1 = etNewPwd1.getText().toString();


            if (TextUtils.isEmpty(oldPwd))
            {
                ToastUtil.show(this, "请输入原密码");
                return;
            }


            if (TextUtils.isEmpty(newPwd))
            {
                ToastUtil.show(this, "请输入新密码");
                return;
            }
            if (oldPwd.equals(newPwd))
            {
                ToastUtil.show(this, "新密码与旧密码不能相同");
                return;
            }

            if (newPwd.length() < 6)
            {
                ToastUtil.show(this, "请输入6-18位新密码");
                return;
            }


            if (!newPwd1.equals(newPwd))
            {
                ToastUtil.show(this, "两次密码输入不一致");
                return;
            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("id", ConfigManager.instance().getUserID());
            valuePairs.put("newPassword", newPwd);
            valuePairs.put("oldPassword", oldPwd);
            Gson gson = new Gson();
            Map<String, String> postMap = new HashMap<>();
            postMap.put("json", gson.toJson(valuePairs));
            DataRequest.instance().request(ModifyPwdActivity.this, Urls.getModifyPwdUrl(), this, HttpRequest.POST, USER_MODIFY_PWD, postMap, new
                    ResultHandler());


        }
        else if (v == ivBack)
        {
            if(ConstantUtil.DEFAULT_PWD.equals(ConfigManager.instance().getUserPwd()))
            {
                ToastUtil.show(ModifyPwdActivity.this,"请修改密码");
            }
            else
            {
                finish();
            }
        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (USER_MODIFY_PWD.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                hideProgressDialog();
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(ConstantUtil.DEFAULT_PWD.equals(ConfigManager.instance().getUserPwd()))
            {
                ToastUtil.show(ModifyPwdActivity.this,"请修改密码");
                return false;
            }
            else
            {
                return super.onKeyDown(keyCode, event);
            }
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }
}
