package com.jyq.wm.utils;

/**
 * URL管理类
 *
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    public static final String HTTP_IP = "http://47.100.20.214:8081";


    //获取版本信息
    public static String getVersionUrl()
    {
        return HTTP_IP + "/app/appInfo/obtain";
    }


    //用戶登录
    public static String getLoginUrl()
    {
        return HTTP_IP + "/app/auth/login";
    }


    //operateType (string, optional): 1-上线0-下线
    //骑手上线或者下线
    public static String getOnOfflinUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/onOfflin";
    }

    public static String getUserInfoUrl()
    {
        return HTTP_IP + "/app/auth/userInfo";
    }

    public static String getOrderListUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/robbing/query";
    }

    //骑手抢单
    public static String getRobOrderUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/robbing";
    }


    //上传定位信息
    public static String getUplaodLocationUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/location";
    }
    //送单记录的查询status (string, optional): 8-骑士已接单,5-正在配送,1-已完成
    public static String getSendOutUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/sendOut/query";
    }


    //骑手确认取餐
    public static String getTakemealConfirmUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/takemeal/confirm";
    }

    //骑手确认完成送单
    public static String getRobbingConfirmUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/robbing/confirm";
    }


    //修改密码
    public static String getModifyPwdUrl()
    {
        return HTTP_IP + "/app/auth/modify/password";
    }

    //获取订单详情
    public static String getOrederDetail()
    {
        return HTTP_IP + "/app/deliverUserOperater/info";
    }


}

