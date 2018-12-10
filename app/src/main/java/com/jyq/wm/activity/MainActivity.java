package com.jyq.wm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.jyq.wm.MyApplication;
import com.jyq.wm.R;
import com.jyq.wm.fragment.HomeFragment;
import com.jyq.wm.fragment.SettingFragment;
import com.jyq.wm.fragment.StatisticsFragment;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.LoginHandler;
import com.jyq.wm.json.ResultHandler;
import com.jyq.wm.map.LocationService;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.LogUtil;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IRequestListener
{


    @BindView(android.R.id.tabhost)
    FragmentTabHost fragmentTabHost;
    private String texts[] = {"首页", "统计", "设置"};
    private int imageButton[] = {R.drawable.ic_home, R.drawable.ic_statistics, R.drawable.ic_setting};
    private Class fragmentArray[] = {HomeFragment.class, StatisticsFragment.class, SettingFragment.class};


    private static final int UPLOAD_LOCATION = 0x01;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(MainActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UPLOAD_LOCATION:


                    mHandler.sendEmptyMessageDelayed(UPLOAD_LOCATION, 30 * 1000);
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
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);

        getPersimmions();
    }

    private View getView(int i)
    {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }

    private final int SDK_PERMISSION_REQUEST = 127;

    @TargetApi(23)
    private void getPersimmions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE))
            {
                //permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0)
            {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission)
    {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission))
            {
                return true;
            }
            else
            {
                permissionsList.add(permission);
                return false;
            }

        }
        else
        {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private LocationService locationService;

    @Override
    protected void onStart()
    {
        super.onStart();
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0)
        {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        }
        else if (type == 1)
        {
            locationService.setLocationOption(locationService.getOption());
        }
        //    locationService.start();// 定位SDK

        mHandler.sendEmptyMessageDelayed(UPLOAD_LOCATION, 30 * 1000);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        locationService.stop();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener()
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError)
            {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty())
                {
                    for (int i = 0; i < location.getPoiList().size(); i++)
                    {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation)
                {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude())
                    {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeOffLineLocation)
                {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                }
                else if (location.getLocType() == BDLocation.TypeServerError)
                {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                }
                else if (location.getLocType() == BDLocation.TypeCriteriaException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                setLocation(location);

                LogUtil.e("TAG", sb.toString());
            }
        }

    };

    private BDLocation location;

    private void setLocation(BDLocation location)
    {
        this.location = location;
    }

    public BDLocation getLocation()
    {
        return location;
    }


    private void uploadLocation()
    {
        BDLocation location = getLocation();
        if (null != location && location.getLatitude() != 0)
        {
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("deliverUserId", ConfigManager.instance().getUserID());
            valuePairs.put("lat", String.valueOf(location.getLatitude()));
            valuePairs.put("lng", String.valueOf(location.getLongitude()));
            Gson gson = new Gson();
            Map<String, String> postMap = new HashMap<>();
            postMap.put("json", gson.toJson(valuePairs));
            DataRequest.instance().request(MainActivity.this, Urls.getUplaodLocationUrl(), this, HttpRequest.POST, "UPLOAD_LOCATION_REQUEST",
                    postMap, new ResultHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

    }
}
