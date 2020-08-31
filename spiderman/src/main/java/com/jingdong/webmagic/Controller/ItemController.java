package com.jingdong.webmagic.Controller;

import com.jingdong.webmagic.Annotation.LogOperator;
import com.jingdong.webmagic.Annotation.UserLoginToken;
import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.RESTful.Result;
import com.jingdong.webmagic.Repository.PriceRepository;
import com.jingdong.webmagic.Service.ItemService;
import com.jingdong.webmagic.Service.PriceService;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private PriceService priceService;

    @RequestMapping("/getInfo")
    @UserLoginToken
    @LogOperator(method = "获取手机条目")
    @ResponseBody
    public Result getInfo(@RequestParam(name = "_channels",required = false) List<String> _channels,
                          @RequestParam(name = "_brands",required = false) List<String> _brands,
                          @RequestParam(name = "_models",required = false) List<String> _models,
                          @RequestParam(name = "_specs",required = false) List<String> _specs,
                          @RequestParam(name = "_price_low",required = false) Double _price_low,
                          @RequestParam(name = "_price_high",required = false) Double _price_high,
                          @RequestParam(name = "_date_start",required = false) String _date_start,
                          @RequestParam(name = "_date_end",required = false) String _date_end,
                          Pageable page){
        Page<PriceEntity> pageList = priceService.getInfoByWhere(_channels,_brands,_models,_specs,_price_low,_price_high,_date_start,_date_end,page);
        return Result.success(pageList);
    }

    @RequestMapping("/selectChannels")
    @UserLoginToken
    @LogOperator(method = "查找渠道")
    @ResponseBody
    public Result selectChannels(){
        List<String> channels = itemService.selectChannels();
        return Result.success(channels);
    }

    @RequestMapping("/selectBrands")
    @UserLoginToken
    @LogOperator(method = "查找手机品牌")
    @ResponseBody
    public Result selectBrands(@RequestParam(name = "_channels",required = true) List<String> _channels){
        Map<String,List<String>> brands = itemService.selectBrands(_channels);
        return Result.success(brands);
    }

    @RequestMapping("/selectModels")
    @UserLoginToken
    @LogOperator(method = "查找手机型号")
    @ResponseBody
    public Result selectModels(@RequestParam(name = "_channels",required = true) List<String> _channels,
                               @RequestParam(name = "_brands",required = true) List<String> _brands){
        Map<String,List<String>> models = itemService.selectModels(_channels,_brands);
        return Result.success(models);
    }

    @RequestMapping("/selectSpecs")
    @UserLoginToken
    @LogOperator(method = "查找手机规格")
    @ResponseBody
    public Result selectSpecs(@RequestParam(name = "_channels",required = true) List<String> _channels,
                              @RequestParam(name = "_brands",required = true) List<String> _brands,
                              @RequestParam(name = "_models",required = true) List<String> _models){
        Map<String,List<String>> specs = itemService.selectSpecs(_channels,_brands,_models);
        return Result.success(specs);
    }

    @RequestMapping("/priceTrend")
    @UserLoginToken
    @LogOperator(method = "获取近期价格趋势")
    @ResponseBody
    public Result priceTrend(@RequestParam(name = "itemId",required = true) Long itemId){
        List<PriceEntity> list = priceService.findByItemId(itemId);
        return Result.success(list);
    }


}
