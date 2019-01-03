package com.jyq.wm.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.wm.MyApplication;
import com.jyq.wm.R;
import com.jyq.wm.activity.BaseHandler;
import com.jyq.wm.activity.LoginActivity;
import com.jyq.wm.activity.MainActivity;
import com.jyq.wm.adapter.MyViewPagerAdapter;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.LoginHandler;
import com.jyq.wm.json.ResultHandler;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_receipt)
    TextView mReceiptTv;


    private View rootView = null;
    private Unbinder unbinder;
    private List<String> tabs = new ArrayList<>(); //标签名称

    private boolean isOnline;
    private static final String ONLINE_REQUEST = "online_request";
    private static final String OFFLINE_REQUEST = "offline_request";
    private static final int ONLINE_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int OFF_LINE_SUCCESS = 0x03;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case ONLINE_SUCCESS:
                    isOnline = true;
                    MyApplication.getInstance().setOnline(isOnline);
                    ToastUtil.show(getActivity(),"骑手上线操作成功");
                    mReceiptTv.setText("骑手下线");
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case OFF_LINE_SUCCESS:
                    isOnline = false;
                    MyApplication.getInstance().setOnline(isOnline);
                    mReceiptTv.setText("骑手上线");
                    ToastUtil.show(getActivity(),"骑手下线操作成功");
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_home, null);
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
        tabs.add("待抢");
        tabs.add("取货");
        tabs.add("配送");
        tabs.add("成功");
    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        mReceiptTv.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(OrderFragment1.newInstance(), "待抢");//添加Fragment
        viewPagerAdapter.addFragment(OrderFragment2.newInstance(), "取货");
        viewPagerAdapter.addFragment(OrderFragment3.newInstance(), "配送");
        viewPagerAdapter.addFragment(OrderFragment4.newInstance(), "成功");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addTab(mTabLayout.newTab().setText("待抢"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("取货"));
        mTabLayout.addTab(mTabLayout.newTab().setText("配送"));
        mTabLayout.addTab(mTabLayout.newTab().setText("成功"));
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题

    }

    @Override
    public void onClick(View v)
    {
        if (v == mReceiptTv)
        {


            if (isOnline)
            {
                setOnFffLine("-1");
            }
            else
            {
                setOnFffLine("1");
            }
        }
    }


    private void setOnFffLine(String operateType)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("deliverUserId ", ConfigManager.instance().getUserID());
        valuePairs.put("operateType ", operateType);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        //operateType  1-上线0-下线
        if ("1".equals(operateType))
        {
            DataRequest.instance().request(getActivity(), Urls.getOnOfflinUrl(), this, HttpRequest.POST, ONLINE_REQUEST, postMap, new ResultHandler());
        }
        else
        {
            DataRequest.instance().request(getActivity(), Urls.getOnOfflinUrl(), this, HttpRequest.POST, OFFLINE_REQUEST, postMap, new ResultHandler());
        }

    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());

        if (ONLINE_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ONLINE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (OFFLINE_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(OFF_LINE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
