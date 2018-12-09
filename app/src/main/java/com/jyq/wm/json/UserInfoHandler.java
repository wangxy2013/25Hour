package com.jyq.wm.json;


import com.jyq.wm.bean.UserInfo;
import com.jyq.wm.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class UserInfoHandler extends JsonHandler
{


    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                UserInfo mUserInfo = new UserInfo(jsonObject);

                if(null != mUserInfo)
                {
                    ConfigManager.instance().setUserId(mUserInfo.getId());
                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}