package com.jyq.wm.bean;

import org.json.JSONObject;

public class UserInfo
{

    private String id;

    private String name;

    private boolean modifyPwdFlag;
    private String phone;
    private String sameTimeSize;
    private String status;
    private String address;


    public UserInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
        this.modifyPwdFlag = obj.optBoolean("modifyPwdFlag");
        this.phone = obj.optString("phone");
        this.sameTimeSize = obj.optString("sameTimeSize");
        this.status = obj.optString("status");
        this.address = obj.optString("address");
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isModifyPwdFlag()
    {
        return modifyPwdFlag;
    }

    public void setModifyPwdFlag(boolean modifyPwdFlag)
    {
        this.modifyPwdFlag = modifyPwdFlag;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSameTimeSize()
    {
        return sameTimeSize;
    }

    public void setSameTimeSize(String sameTimeSize)
    {
        this.sameTimeSize = sameTimeSize;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
