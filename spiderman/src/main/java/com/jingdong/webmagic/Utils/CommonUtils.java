package com.jingdong.webmagic.Utils;

import com.jingdong.webmagic.Constant.Constant;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static String splitUrl(String url){
        String[] s = url.split("https://item.jd.com/");
        String[] s1 = s[1].split("\\.");
        return s1[0];
    }

    public static List<String> idToUrl(List<String> ids){
        String first = "https://item.jd.com/";
        String end = ".html#crumb-wrap";
        List<String> result = new ArrayList<>();
        for (String id : ids){
            result.add(first + id + end);
        }
        return result;
    }

    public static String regexBrand(String input) {
        if(input != null){
            for (Map.Entry<String, String> entry : Constant.brandMap.entrySet()) {
                Pattern patter = Pattern.compile(entry.getValue());
                Matcher matcher = patter.matcher(input);
                if (matcher.find()){
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    public static String regexFormat(String input){
        if(input != null){
            for (Map.Entry<String,String> entry : Constant.formatMap.entrySet()){
                Pattern patter = Pattern.compile(entry.getValue());
                Matcher matcher = patter.matcher(input);
                if (matcher.find()){
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    public static String regexMoney(String money){
        Pattern pattern = Pattern.compile("([1-9][0-9]*)+(.[0-9]{1,2})");
        Matcher matcher = pattern.matcher(money);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }

    //优惠券提取数字
    public static Map<String,Double> getNumFromString(String input){
        Map<String,Double> map = new HashMap<>();
        //零宽断言
        Pattern patter1 = Pattern.compile("(?<=满)\\d*");
        Pattern patter2 = Pattern.compile("(?<=减)\\d*");
        Matcher matcher1 = patter1.matcher(input);
        Matcher matcher2 = patter2.matcher(input);
        if(matcher1.find()){
            try{
                map.put("man",Double.valueOf(matcher1.group()));
            }catch (Exception e){
                map.put("man",null);
            }
        }
        if(matcher2.find()){
            try{
                map.put("jian",Double.valueOf(matcher2.group()));
            }catch (Exception e){
                map.put("jian",null);
            }
        }
        return map;
    }

    //返回最低价格
    public static Double calcMinNum(Double price,List<Map<String,Double>> mapList){
        Double min = price;
        for(Map<String,Double> m : mapList){
            if(m.get("jian") != null){
                if(m.get("man") == null){
                    m.put("man",0.0);
                }
                if(price >= m.get("man")){
                    Double temp = price - m.get("jian");
                    if(temp < min){
                        min = temp;
                    }
                }
            }
        }
        return min;
    }

    //判断官网详情页面
    public static String brandOfUrl(String url){
        if(url.contains("www.vmall.com")){
            return "HUAWEI";
        }
        if(url.contains("www.mi.com")){
            return "MI";
        }
        if(url.contains("shop.vivo.com.cn")){
            return "VIVO";
        }
        if(url.contains("www.opposhop.cn")){
            return "OPPO";
        }
        return "";
    }

    //随机数
    public static Long getRandomNum(){
        Long random;
        Long time = new Date().getTime();
        random = time * 1000;
        Random r = new Random();
        random = random + r.nextInt(1000);
        return random;
    }
}
