package com.jyq.wm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
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
import com.jyq.wm.utils.DialogUtils;
import com.jyq.wm.utils.LogUtil;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.statusbar.StatusBarUtil;

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

                    uploadLocation();
                    mHandler.sendEmptyMessageDelayed(UPLOAD_LOCATION, 60 * 1000);
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
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(MainActivity.this, false);
    }

    @Override
    protected void initEvent()
    {

    }


    private static final int PRIVATE_CODE = 1315;//开启GPS权限

    @Override
    protected void initViewData()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable
            // .main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);

        // getPersimmions();

        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity
        // ，都是通过此种方式获取locationservice实例的
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

        requestLocationPermission();

        //        if (isLocServiceEnable(this))
        //        {
        //            locationService.start();// 定位SDK
        //            mHandler.sendEmptyMessageDelayed(UPLOAD_LOCATION, 60 * 1000);
        //        }
        //        else
        //        {
        //            DialogUtils.showToastDialog2Button(this, "未开启定位，请立即设置", new View.OnClickListener()
        //            {
        //                @Override
        //                public void onClick(View view)
        //                {
        //                    Intent intent = new Intent();
        //                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //                    startActivityForResult(intent, PRIVATE_CODE);
        //                }
        //            });
        //        }
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


    private void requestLocationPermission()
    {
        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        //        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[0]))
        //        {
        //            Toast.makeText(MainActivity.this, "11111111", Toast.LENGTH_LONG).show();
        //        }
        //        else
        //        {
        PermissionsUtil.TipInfo mTipInfo = new PermissionsUtil.TipInfo("帮助", "请打开位置信息", "取消", "设置");

        PermissionsUtil.requestPermission(getApplication(), new PermissionListener()
        {
            @Override
            public void permissionGranted(@NonNull String[] permissions)
            {
                // Toast.makeText(MainActivity.this, "访问摄像头", Toast.LENGTH_LONG).show();
                locationService.start();// 定位SDK
                mHandler.sendEmptyMessageDelayed(UPLOAD_LOCATION, 60 * 1000);
            }

            @Override
            public void permissionDenied(@NonNull String[] permissions)
            {
                Toast.makeText(MainActivity.this, "用户拒绝了访问位置信息", Toast.LENGTH_LONG).show();
            }
        }, permission, true, mTipInfo);
        //        }

    }


    private LocationService locationService;

    @Override
    protected void onStart()
    {
        super.onStart();


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
        if (null != location && location.getLatitude() != 4.9E-324)
        {

            MyApplication.getInstance().setLocation(location);
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("deliverUserId", ConfigManager.instance().getUserID());
            valuePairs.put("lat", String.valueOf(location.getLatitude()));
            valuePairs.put("lng", String.valueOf(location.getLongitude()));
            Gson gson = new Gson();
            Map<String, String> postMap = new HashMap<>();
            postMap.put("json", gson.toJson(valuePairs));
            DataRequest.instance().request(MainActivity.this, Urls.getUplaodLocationUrl(), this, HttpRequest.POST, "UPLOAD_LOCATION_REQUEST", postMap, new
                    ResultHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

    }


    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network)
        {
            return true;
        }

        return false;
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            DialogUtils.showToastDialog2Button(MainActivity.this, "是否退出骑手端APP", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   finish();

                }
            });

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }
}
