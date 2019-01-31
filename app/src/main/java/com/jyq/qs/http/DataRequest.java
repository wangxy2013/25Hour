package com.jyq.qs.http;


import android.content.Context;


import com.jyq.qs.json.JsonHandler;

import java.io.File;
import java.util.Map;


/**
 */
public class DataRequest
{
    private static DataRequest sDataManage = null;

    public static DataRequest instance()
    {
        if (sDataManage == null)
        {
            sDataManage = new DataRequest();
        }
        return sDataManage;
    }

    /**
     * @param listener   监听
     * @param httpType   get 还是post
     * @param reqType    请求标识
     * @param valuePairs 请求参数
     * @param handler    json解析
     */
    public void request(Context mContext, String url, IRequestListener listener, int httpType, String reqType,
                        Map<String, String> valuePairs, JsonHandler handler)
    {
        HttpRequest request = new HttpRequest(mContext, httpType, reqType, url, valuePairs, listener, handler);
        ThreadPoolFactory.execute(request);
    }


    public void request(Context mContext, String url, IRequestListener listener, int httpType, String reqType,
                        Map<String, String> valuePairs, File mFile, JsonHandler handler)
    {
        HttpRequest request = new HttpRequest(mContext, httpType, reqType, url, valuePairs, mFile, listener, handler);
        ThreadPoolFactory.execute(request);
    }

}
