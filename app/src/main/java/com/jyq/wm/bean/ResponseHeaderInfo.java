package com.jyq.wm.bean;

import org.json.JSONObject;

public class ResponseHeaderInfo
{
    private String requestId;//20e0a988-6404-4c9b-90c5-5977c569d2b9private String  ,
    private String appId;//com.jyq.qsprivate String  ,
    private String retCode;//0000private String  ,
    private String retMsg;//成功private String


    public ResponseHeaderInfo(JSONObject obj)
    {
        this.requestId = obj.optString("requestId");
        this.appId = obj.optString("appId");
        this.retCode = obj.optString("retCode");
        this.retMsg = obj.optString("retMsg");
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getRetCode()
    {
        return retCode;
    }

    public void setRetCode(String retCode)
    {
        this.retCode = retCode;
    }

    public String getRetMsg()
    {
        return retMsg;
    }

    public void setRetMsg(String retMsg)
    {
        this.retMsg = retMsg;
    }
}
