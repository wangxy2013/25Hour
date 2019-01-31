package com.jyq.qs.json;


import android.content.Context;
import android.util.Log;


import com.jyq.qs.utils.ConstantUtil;

import org.json.JSONObject;

import okhttp3.Response;


public abstract class JsonHandler
{

    private String resultCode = null;
    private String resultMsg = null;
    private String header = null;

    protected abstract void parseJson(JSONObject obj) throws Exception;

    public void parseJson(Context mContext, Response response)
    {
        String jsonString = null;
        try
        {
            if (null == response)
            {
                setResultCode(ConstantUtil.RESULT_FAIL);
            }
            else
            {
                jsonString = response.body().string();


                Log.e("tag", "response result : " + jsonString);
                setHeader(response.header("Date"));


                JSONObject jsonObject = new JSONObject(jsonString);

                if ("".equals(jsonObject.optString("status")))
                {
                    setResultCode(ConstantUtil.RESULT_SUCCESS);
                }
                else
                {
                    setResultCode(ConstantUtil.RESULT_FAIL);
                }
                setResultMsg(jsonObject.optString("message"));
                parseJson(jsonObject);
            }
        }
        catch (Exception e)
        {
            setResultCode(ConstantUtil.RESULT_FAIL);
            setResultMsg("网络请求失败...");

        }

    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultMsg()
    {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg)
    {
        this.resultMsg = resultMsg;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }
}
