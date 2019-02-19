package com.jyq.qs.bean;

import org.json.JSONObject;

public class GoodsInfo
{
    //    attributesNames (string, optional): 属性值id ,
    //    attributesNamesValues (string, optional): 属性值 ,
    //    goodsImage (string, optional): 菜品图片 ,
    //    goodsName (string, optional): 菜品名称 ,
    //    isDiscount (string, optional): 是否有折扣 ,
    //    oldPrice (number, optional): 原价 ,
    //    price (number, optional): 现价 ,
    //    size (integer, optional): 数量 ,
    //    unit (string, optional): 单位
    private String goodsName;//秘制一号",
    private String goodsImage;//": null,
    private String oldPrice;//": 15.00,
    private String price;//": 15.00,
    private String attributesNames;//;//2949024409912320,2949024412173312,2949024412615680,2949024413090816,2949024413533184,2949024413991936,
    // 2949024414450688,2949024414958592",
    private String attributesNamesValues;//不辣,青菜,菠菜,海带,金针菇,木耳,腐竹,冻豆腐",
    private String size;//": 15,
    private String isDiscount;//": null,
    private String unit;//": null
    private String goodsTotalPrice;
    private String finalPrice;

    public GoodsInfo(JSONObject obj)
    {
        this.goodsName = obj.optString("goodsName");
        this.goodsImage = obj.optString("goodsImage");
        this.oldPrice = obj.optString("oldPrice");
        this.price = obj.optString("price");
        this.attributesNames = obj.optString("attributesNames");
        this.attributesNamesValues = obj.optString("attributesNamesValues");
        this.size = obj.optString("size");
        this.isDiscount = obj.optString("isDiscount");
        this.unit = obj.optString("unit");
        this.goodsTotalPrice = obj.optString("goodsTotalPrice");
        this.finalPrice = obj.optString("finalPrice");
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsImage()
    {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage)
    {
        this.goodsImage = goodsImage;
    }

    public String getOldPrice()
    {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice)
    {
        this.oldPrice = oldPrice;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getAttributesNames()
    {
        return attributesNames;
    }

    public void setAttributesNames(String attributesNames)
    {
        this.attributesNames = attributesNames;
    }

    public String getAttributesNamesValues()
    {
        return attributesNamesValues;
    }

    public void setAttributesNamesValues(String attributesNamesValues)
    {
        this.attributesNamesValues = attributesNamesValues;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getIsDiscount()
    {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount)
    {
        this.isDiscount = isDiscount;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getGoodsTotalPrice()
    {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(String goodsTotalPrice)
    {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public String getFinalPrice()
    {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice)
    {
        this.finalPrice = finalPrice;
    }
}
