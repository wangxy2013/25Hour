package com.jyq.qs.json;


import com.jyq.qs.bean.VersionInfo;

import org.json.JSONObject;

/**
 * 描述：版本更新
 */
public class VersionInfoHandler extends JsonHandler
{

    private VersionInfo mVersionInfo;

    public VersionInfo getVersionInfo()
    {
        return mVersionInfo;
    }

    @Override
    protected void parseJson(JSONObject obj) throws Exception
    {


        try
        {
            if (null != obj)
            {
                mVersionInfo = new VersionInfo(obj);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
