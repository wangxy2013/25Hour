package com.jyq.qs.bean;

import org.json.JSONObject;

public class StatisticsInfo
{
    private String distributorId;//3007497998337024
    private String startDate;//1545523200000,
    private String endDate;//1545523200000,
    private String dataCount;//0,
    private String payOfflineCount;//null,
    private String payOnlineCount;//null,
    private String remoteOrderCount;//null,
    private String amount;//null,
    private String payOfflineAmount;//null,
    private String payOnlineAmount;//null,
    private String distributorCommissionAmount;//null,
    private String quantity;//null


    public StatisticsInfo(JSONObject obj)
    {
        this.distributorId = obj.optString("distributorId");
        this.startDate = obj.optString("startDate");
        this.endDate = obj.optString("endDate");
        this.dataCount = obj.optString("dataCount");
        this.payOfflineCount = obj.optString("payOfflineCount");
        this.payOnlineCount = obj.optString("payOnlineCount");
        this.remoteOrderCount = obj.optString("remoteOrderCount");
        this.amount = obj.optString("amount");
        this.payOfflineAmount = obj.optString("payOfflineAmount");
        this.payOnlineAmount = obj.optString("payOnlineAmount");
        this.distributorCommissionAmount = obj.optString("distributorCommissionAmount");
        this.quantity = obj.optString("quantity");
    }


    public String getDistributorId()
    {
        return distributorId;
    }

    public void setDistributorId(String distributorId)
    {
        this.distributorId = distributorId;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getDataCount()
    {
        return dataCount;
    }

    public void setDataCount(String dataCount)
    {
        this.dataCount = dataCount;
    }

    public String getPayOfflineCount()
    {
        return payOfflineCount;
    }

    public void setPayOfflineCount(String payOfflineCount)
    {
        this.payOfflineCount = payOfflineCount;
    }

    public String getPayOnlineCount()
    {
        return payOnlineCount;
    }

    public void setPayOnlineCount(String payOnlineCount)
    {
        this.payOnlineCount = payOnlineCount;
    }

    public String getRemoteOrderCount()
    {
        return remoteOrderCount;
    }

    public void setRemoteOrderCount(String remoteOrderCount)
    {
        this.remoteOrderCount = remoteOrderCount;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getPayOfflineAmount()
    {
        return payOfflineAmount;
    }

    public void setPayOfflineAmount(String payOfflineAmount)
    {
        this.payOfflineAmount = payOfflineAmount;
    }

    public String getPayOnlineAmount()
    {
        return payOnlineAmount;
    }

    public void setPayOnlineAmount(String payOnlineAmount)
    {
        this.payOnlineAmount = payOnlineAmount;
    }

    public String getDistributorCommissionAmount()
    {
        return distributorCommissionAmount;
    }

    public void setDistributorCommissionAmount(String distributorCommissionAmount)
    {
        this.distributorCommissionAmount = distributorCommissionAmount;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
}
