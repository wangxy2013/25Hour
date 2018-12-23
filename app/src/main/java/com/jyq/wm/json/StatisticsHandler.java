package com.jyq.wm.json;


import com.jyq.wm.bean.ResponseHeaderInfo;
import com.jyq.wm.bean.StatisticsInfo;
import com.jyq.wm.bean.UserInfo;
import com.jyq.wm.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class StatisticsHandler extends JsonHandler
{

    private ResponseHeaderInfo responseHeaderInfo;

    private StatisticsInfo statisticsInfo;


    public ResponseHeaderInfo getResponseHeaderInfo()
    {
        return responseHeaderInfo;
    }

    public StatisticsInfo getStatisticsInfo()
    {
        return statisticsInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            JSONObject responseHeaderObj = jsonObject.optJSONObject("responseHeader");
            JSONObject statisticsObj = jsonObject.optJSONObject("reportData");
            if (null != responseHeaderObj)
            {
                responseHeaderInfo = new ResponseHeaderInfo(responseHeaderObj);
            }


            if (null != statisticsObj)
            {
                statisticsInfo = new StatisticsInfo(statisticsObj);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}