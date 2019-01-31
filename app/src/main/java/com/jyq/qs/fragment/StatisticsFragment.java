package com.jyq.qs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.jyq.qs.R;
import com.jyq.qs.bean.ResponseHeaderInfo;
import com.jyq.qs.bean.StatisticsInfo;
import com.jyq.qs.http.DataRequest;
import com.jyq.qs.http.HttpRequest;
import com.jyq.qs.http.IRequestListener;
import com.jyq.qs.json.StatisticsHandler;
import com.jyq.qs.utils.ConfigManager;
import com.jyq.qs.utils.ConstantUtil;
import com.jyq.qs.utils.LogUtil;
import com.jyq.qs.utils.StringUtils;
import com.jyq.qs.utils.ToastUtil;
import com.jyq.qs.utils.Urls;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatisticsFragment extends BaseFragment implements IRequestListener, View.OnClickListener
{

    @BindView(R.id.tv_order_count)
    TextView tvOrderCount;
    @BindView(R.id.tv_delivery)
    TextView tvDelivery;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_wx_total)
    TextView tvWxTotal;
    @BindView(R.id.tv_start_year)
    TextView tvStartYear;
    @BindView(R.id.tv_start_month)
    TextView tvStartMonth;
    @BindView(R.id.tv_start_day)
    TextView tvStartDay;
    @BindView(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_end_year)
    TextView tvEndYear;
    @BindView(R.id.tv_end_month)
    TextView tvEndMonth;
    @BindView(R.id.tv_end_day)
    TextView tvEndDay;
    @BindView(R.id.rl_end_time)
    RelativeLayout rlEndTime;
    @BindView(R.id.tv_query)
    TextView tvQuery;
    private View rootView = null;
    private Unbinder unbinder;
    private String mStartTime;
    private String mEndTime;
    private static final String QUERY_ORDER = "query_order";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_SUCCESS:
                    StatisticsHandler mStatisticsHandler = (StatisticsHandler) msg.obj;

                    ResponseHeaderInfo responseHeaderInfo = mStatisticsHandler.getResponseHeaderInfo();
                    StatisticsInfo statisticsInfo = mStatisticsHandler.getStatisticsInfo();


                    if (null != responseHeaderInfo)
                    {
                        if ("0000".equals(responseHeaderInfo.getRetCode()))
                        {
                            if (null != statisticsInfo)
                            {
                                tvOrderCount.setText(statisticsInfo.getDataCount());
                                tvDelivery.setText(statisticsInfo.getDistributorCommissionAmount());
                                tvTotal.setText(statisticsInfo.getAmount());
                                tvWxTotal.setText(statisticsInfo.getPayOnlineAmount());
                            }
                        }
                        else
                        {
                            ToastUtil.show(getActivity(), responseHeaderInfo.getRetMsg());
                        }
                    }


                    break;

                case REQUEST_FAIL:

                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_statistics, null);
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
        rlStartTime.setOnClickListener(this);
        rlEndTime.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DATE);

        String monthStr = m < 10 ? "0" + m : m + "";
        String dayStr = d < 10 ? "0" + d : d + "";

        tvStartYear.setText(y + "");
        tvStartMonth.setText(monthStr);
        tvStartDay.setText(dayStr);

        tvEndYear.setText(y + "");
        tvEndMonth.setText(monthStr);
        tvEndDay.setText(dayStr);

        mStartTime = y + "-" + monthStr + "-" + dayStr;
        mEndTime = y + "-" + monthStr + "-" + dayStr;


    }


    @Override
    public void onClick(View v)
    {
        if (v == rlStartTime)
        {
            TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener()
            {
                @Override
                public void onTimeSelect(Date date, View v)
                {
                    //获取默认选中的日期的年月日星期的值，并赋值
                    Calendar calendar = Calendar.getInstance();//日历对象
                    calendar.setTime(date);//设置当前日期

                    String yearStr = calendar.get(Calendar.YEAR) + "";//获取年份
                    int month = calendar.get(Calendar.MONTH) + 1;//获取月份
                    String monthStr = month < 10 ? "0" + month : month + "";
                    int day = calendar.get(Calendar.DATE);//获取日
                    String dayStr = day < 10 ? "0" + day : day + "";

                    tvStartYear.setText(yearStr);
                    tvStartMonth.setText(monthStr);
                    tvStartDay.setText(dayStr);

                    mStartTime = yearStr + "-" + monthStr + "-" + dayStr;
                }
            }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    //                .setContentSize(18)//滚轮文字大小
                    //                .setTitleSize(20)//标题文字大小
                    //                //.setTitleText("Title")//标题文字
                    //                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                    //                .isCyclic(true)//是否循环滚动
                    //                //.setTitleColor(Color.BLACK)//标题文字颜色
                    //                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                    //                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                    //                //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                    //                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                    ////                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    ////                .setRangDate(startDate,endDate)//起始终止年月日设定
                    //                //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    //.isDialog(true)//是否显示为对话框样式
                    .build();

            pvTime.show();
        }
        else if (v == rlEndTime)
        {
            TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener()
            {
                @Override
                public void onTimeSelect(Date date, View v)
                {
                    //获取默认选中的日期的年月日星期的值，并赋值
                    Calendar calendar = Calendar.getInstance();//日历对象
                    calendar.setTime(date);//设置当前日期

                    String yearStr = calendar.get(Calendar.YEAR) + "";//获取年份
                    int month = calendar.get(Calendar.MONTH) + 1;//获取月份
                    String monthStr = month < 10 ? "0" + month : month + "";
                    int day = calendar.get(Calendar.DATE);//获取日
                    String dayStr = day < 10 ? "0" + day : day + "";

                    tvEndYear.setText(yearStr);
                    tvEndMonth.setText(monthStr);
                    tvEndDay.setText(dayStr);

                    mEndTime = yearStr + "-" + monthStr + "-" + dayStr;
                }
            }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    //.isDialog(true)//是否显示为对话框样式
                    .build();

            pvTime.show();
        }
        else if (v == tvQuery)
        {
            int result = StringUtils.compare_date(mEndTime, mStartTime);
            LogUtil.e("TAG", "result = " + result);

            if (result == -1)
            {
                ToastUtil.show(getActivity(), "请选择正确的查询周期");
                return;
            }
            showProgressDialog(getActivity());
            Map<String, String> requestHeaderPairs = new HashMap<>();
            requestHeaderPairs.put("requestId", UUID.randomUUID().toString());
            requestHeaderPairs.put("appId", "com.jyq.qs");


            Map<String, String> conditionsPairs = new HashMap<>();
            conditionsPairs.put("distributorId", ConfigManager.instance().getUserID());
            conditionsPairs.put("startDate", mStartTime);
            conditionsPairs.put("endDate", mEndTime);

            Map<String, Object> valuePairs = new HashMap<>();
            valuePairs.put("requestHeader", requestHeaderPairs);
            valuePairs.put("conditions", conditionsPairs);
            Gson gson = new Gson();
            Map<String, String> postMap = new HashMap<>();
            postMap.put("json", gson.toJson(valuePairs));
            DataRequest.instance().request(getActivity(), Urls.getQuerySellerUrl(), this, HttpRequest.POST, QUERY_ORDER, postMap, new
                    StatisticsHandler());
        }


    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
        if (QUERY_ORDER.equals(action))
        {
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

}
