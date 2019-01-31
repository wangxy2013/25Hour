package com.jyq.qs.json;


import com.jyq.qs.bean.UserInfo;
import com.jyq.qs.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class UserInfoHandler extends JsonHandler
{

    private UserInfo mUserInfo;

    public UserInfo getUserInfo()
    {
        return mUserInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                mUserInfo = new UserInfo(jsonObject);

                if (null != mUserInfo)
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