package com.jyq.qs.json;


import com.jyq.qs.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class LoginHandler extends JsonHandler
{


    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                String token = jsonObject.optString("token");
                ConfigManager.instance().setToken(token);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}