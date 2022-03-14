package com.bdqn.controller;

import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripHotel.ItripHotelMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.ItripAreaDic;
import cn.itrip.pojo.ItripLabelDic;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.ItripUserLinkUser;
import com.alibaba.fastjson.JSONArray;
import common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
public class bizController {

        @Resource
        ItripAreaDicMapper dao;


        @RequestMapping(value="/api/hotel/queryhotcity/{type}",produces="application/json; charset=utf-8")
        @ResponseBody
        public Dto getCitty(@PathVariable("type") int t) throws Exception {
              List<ItripAreaDic> list=dao.selectChinese(t);
                return DtoUtil.returnDataSuccess(list);
    }

   @Resource
   ItripLabelDicMapper dao1;

    @RequestMapping(value="/api/hotel/queryhotelfeature",produces="application/json; charset=utf-8")
    @ResponseBody
    public Dto getfirst() throws Exception {
        List<ItripLabelDic> list=dao1.fisttop();
        return DtoUtil.returnDataSuccess(list);
    }








}
