package com.jyq.wm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jyq.wm.R;
import com.jyq.wm.activity.MainActivity;
import com.jyq.wm.activity.ModifyPwdActivity;
import com.jyq.wm.activity.UserDetailActivity;
import com.jyq.wm.utils.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment implements View.OnClickListener
{

    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.rl_voice)
    RelativeLayout rlVoice;
    private View rootView = null;
    private Unbinder unbinder;

    private boolean voiceOpend = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_setting, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        rlUser.setOnClickListener(this);
        rlPwd.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        if (ConfigManager.instance().getVoiceIsOpend())
        {
            voiceOpend = true;
            ivSwitch.setImageResource(R.drawable.ic_switch_on);
        }
        else

        {
            voiceOpend = false;
            ivSwitch.setImageResource(R.drawable.ic_switch_off);
        }
    }


    @Override
    public void onClick(View v)
    {
        if (v == rlUser)
        {
            startActivity(new Intent(getActivity(), UserDetailActivity.class));

        }
        else if (v == rlPwd)
        {
            startActivityForResult(new Intent(getActivity(), ModifyPwdActivity.class), 9001);
        }
        else if (v == ivSwitch)
        {
            if (voiceOpend)
            {
                voiceOpend = false;
                ivSwitch.setImageResource(R.drawable.ic_switch_off);
            }
            else
            {
                voiceOpend = true;
                ivSwitch.setImageResource(R.drawable.ic_switch_on);
            }
            ConfigManager.instance().setVoiceIsOpend(voiceOpend);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 9001)
            {
                ((MainActivity) getActivity()).finish();
            }
        }

    }
}
