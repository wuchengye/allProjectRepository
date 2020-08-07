package com.jingdong.webmagic.Constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constant {
    /**
     * @date 2020-07-09 16:55
     * 商家网址
     */
    public final static String JD_HUAWEI ="https://mall.jd.com/index-1000004259.html";
    public final static String JD_HONOR="https://mall.jd.com/index-1000000904.html";
    public final static String JD_MI="https://mall.jd.com/index-1000004123.html";
    public final static String JD_APPLE="https://mall.jd.com/index-1000000127.html";
    public final static String JD_VIVO="https://mall.jd.com/index-1000085868.html";
    public final static String JD_OPPO="https://mall.jd.com/index-1000004065.html";

    /**
     * @date 2020-07-09 16:55
     * 品牌 正则匹配商品标题
     */
    public final static Map<String,String> brandMap = new LinkedHashMap<String,String>(){
        //有序map，先匹配红米再匹配小米,不然可能匹配不到红米
        {
            put("Redmi","红米|Redmi|REDMI|RedMi|redmi");
            put("小米","小米|xiaomi|Xiaomi");
            put("荣耀","荣耀|HONOR|honor|Honor");
            put("华为","华为|HUAWEI|huawei|Huawei");
            put("苹果","苹果|Apple|iPhone|iphone|IPHONE");
            put("VIVO","VIVO|vivo|Vivo");
            put("OPPO","OPPO|oppo|Oppo");
        }
    };

    /**
     * @date 2020-07-10 11:33
     * 制式 正则
     */
    public final static Map<String,String> formatMap = new HashMap<String,String>(){
        {
            put("5G","5G(?!B|b)|5g(?!B|b)");
            put("4G","4G(?!B|b)|4g(?!B|b)");
        }
    };
}
