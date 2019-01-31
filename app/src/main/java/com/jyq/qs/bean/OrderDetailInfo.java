package com.jyq.qs.bean;

import com.jyq.qs.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

public class OrderDetailInfo
{
    private String deliveryFee;//(number, optional): 配送费 ,
    private String deposit;//((number, optional): 包装费 ,
    private String discountList;//( (Array[DiscountInfoResultVO], optional): 优惠活动 ,
    private String id;//((string, optional): id ,
    private String isMobilePay;//((string, optional): 是否是手机支付 ,
    private String minusPrice;//((number, optional): 优惠金额 ,
    private String name;//((string, optional): 点单人姓名 ,
    private String note;//((string, optional): 备注信息 ,
    private String onlinePay;//((number, optional): 在线支付金额 ,
    private String orderInterId;//((string, optional): 订单流水号 ,
    private String payStatue;//((string, optional): 支付状态 ,
    private String payType;//((string, optional): 支付方式 ,
    private String phone;//((string, optional): 联系电话 ,
    private String price;//( (number, optional): 实收 ,
    private String scoreDeducte;//( (number, optional): 积分抵扣金额 ,
    private String scoreUsedCount;//( (integer, optional): 积分使用数量 ,
    private String storeId;//((string, optional): 店铺id ,
    private String storeName;//((string, optional): 店铺id ,
    private String systemPay;//((number, optional): 使用商户余额 ,
    private String totalPrice;//((number, optional): 总价
    private String dateline;
    private String address;
    private String submitOrderTime;

    private String storeAddress;//": "北新泾",
    private String storePhone;//

    private List<GoodsInfo> goodsInfoList;


    public OrderDetailInfo(JSONObject obj)
    {
        this.address = obj.optString("address");
        this.dateline = obj.optString("dateline");
        this.deliveryFee = obj.optString("deliveryFee");
        this.deposit = obj.optString("deposit");
        this.discountList = obj.optString("discountList");
        this.id = obj.optString("id");
        this.isMobilePay = obj.optString("isMobilePay");
        this.minusPrice = obj.optString("minusPrice");
        this.name = obj.optString("name");
        this.note = obj.optString("note");
        this.onlinePay = obj.optString("onlinePay");
        this.orderInterId = obj.optString("orderInterId");
        this.payStatue = obj.optString("payStatue");
        this.phone = obj.optString("phone");
        this.payType = obj.optString("payType");
        this.price = obj.optString("price");
        this.scoreDeducte = obj.optString("scoreDeducte");
        this.scoreUsedCount = obj.optString("scoreUsedCount");
        this.storeId = obj.optString("storeId");
        this.storeName = obj.optString("storeName");
        this.totalPrice = obj.optString("totalPrice");
        this.submitOrderTime = obj.optString("submitOrderTime");
        this.storeAddress = obj.optString("storeAddress");
        this.storePhone=obj.optString("storePhone");
        if (StringUtils.stringIsEmpty(price))
        {
            this.price = "0.0";
        }


        if (StringUtils.stringIsEmpty(deposit))
        {
            this.deposit = "0.0";
        }

    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDeliveryFee()
    {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee)
    {
        this.deliveryFee = deliveryFee;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }

    public String getDiscountList()
    {
        return discountList;
    }

    public void setDiscountList(String discountList)
    {
        this.discountList = discountList;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIsMobilePay()
    {
        return isMobilePay;
    }

    public void setIsMobilePay(String isMobilePay)
    {
        this.isMobilePay = isMobilePay;
    }

    public String getMinusPrice()
    {
        return minusPrice;
    }

    public void setMinusPrice(String minusPrice)
    {
        this.minusPrice = minusPrice;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getOnlinePay()
    {
        return onlinePay;
    }

    public void setOnlinePay(String onlinePay)
    {
        this.onlinePay = onlinePay;
    }

    public String getOrderInterId()
    {
        return orderInterId;
    }

    public void setOrderInterId(String orderInterId)
    {
        this.orderInterId = orderInterId;
    }

    public String getPayStatue()
    {
        return payStatue;
    }

    public void setPayStatue(String payStatue)
    {
        this.payStatue = payStatue;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getScoreDeducte()
    {
        return scoreDeducte;
    }

    public void setScoreDeducte(String scoreDeducte)
    {
        this.scoreDeducte = scoreDeducte;
    }

    public String getScoreUsedCount()
    {
        return scoreUsedCount;
    }

    public void setScoreUsedCount(String scoreUsedCount)
    {
        this.scoreUsedCount = scoreUsedCount;
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

    public String getSystemPay()
    {
        return systemPay;
    }

    public void setSystemPay(String systemPay)
    {
        this.systemPay = systemPay;
    }

    public String getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public List<GoodsInfo> getGoodsInfoList()
    {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList)
    {
        this.goodsInfoList = goodsInfoList;
    }

    public String getDateline()
    {
        return dateline;
    }

    public void setDateline(String dateline)
    {
        this.dateline = dateline;
    }

    public String getSubmitOrderTime()
    {
        return submitOrderTime;
    }

    public void setSubmitOrderTime(String submitOrderTime)
    {
        this.submitOrderTime = submitOrderTime;
    }


    public String getStoreAddress()
    {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress)
    {
        this.storeAddress = storeAddress;
    }

    public String getStorePhone()
    {
        return storePhone;
    }

    public void setStorePhone(String storePhone)
    {
        this.storePhone = storePhone;
    }
}
