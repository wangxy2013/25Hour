package com.jyq.wm.bean;

import org.json.JSONObject;

public class OrderInfo
{
    private String id;//2977857348699136
    private String storeId;//2949009943544832
    private String storeName;//张亮麻辣烫仙林店
    private String name;//建哥
    private String phone;//15265986565
    private String address;//福建省泉州市南安市石井镇石井镇 实景路23号1020 实景路23号1020
    private String payType;//offline
    private String addTime;
    private String orderId;
    private double storeLnt;//,
    private double storeLat;//null,
    private double userLnt;// 118.432,
    private double userLat;// 24.62567,

    public OrderInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.storeId = obj.optString("storeId");
        this.storeName = obj.optString("storeName");
        this.name = obj.optString("name");
        this.phone = obj.optString("phone");
        this.address = obj.optString("address");
        this.payType = obj.optString("payType");
        this.addTime = obj.optString("submitOrderTime");
        this.storeLnt = obj.optDouble("storeLnt",0);
        this.storeLat = obj.optDouble("storeLat",0);
        this.userLnt = obj.optDouble("userLnt",0);
        this.userLat = obj.optDouble("userLat",0);
        this.orderId = obj.optString("orderId");


    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getAddTime()
    {
        return addTime;
    }

    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }

    public double getStoreLnt()
    {
        return storeLnt;
    }

    public void setStoreLnt(double storeLnt)
    {
        this.storeLnt = storeLnt;
    }

    public double getStoreLat()
    {
        return storeLat;
    }

    public void setStoreLat(double storeLat)
    {
        this.storeLat = storeLat;
    }

    public double getUserLnt()
    {
        return userLnt;
    }

    public void setUserLnt(double userLnt)
    {
        this.userLnt = userLnt;
    }

    public double getUserLat()
    {
        return userLat;
    }

    public void setUserLat(double userLat)
    {
        this.userLat = userLat;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
}
