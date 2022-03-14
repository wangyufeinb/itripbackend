package com.bdqn.controller;

import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.*;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import common.DateUtil;
import common.Dto;

import common.DtoUtil;
import common.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Controller
public class Userinfocontroller {

    @Resource
    RedisUtil redisUtil;


      @Resource
      ItripUserLinkUserMapper dao;

    @RequestMapping(value="/api/userinfo/queryuserlinkuser",produces="application/json; charset=utf-8")
    @ResponseBody
    public Dto select(@RequestBody ItripSearchUserLinkUserVO vo,HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        ItripUser itripUser = (ItripUser) JSONArray.parseObject(redisUtil.getRedis(token), ItripUser.class);
        List<ItripUserLinkUser> list= dao.select(itripUser.getId());
        return DtoUtil.returnDataSuccess(list);
    }




    @RequestMapping(value="/api/userinfo/adduserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto adduserlinkuser(@RequestBody ItripAddUserLinkUserVO vo,HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        ItripUser itripUser = (ItripUser) JSONArray.parseObject(redisUtil.getRedis(token), ItripUser.class);
        ItripUserLinkUser i=new ItripUserLinkUser();
        i.setLinkUserName(vo.getLinkUserName());
        i.setLinkIdCard(vo.getLinkIdCard());
        i.setLinkPhone(vo.getLinkPhone());
        i.setUserId(itripUser.getId());
        dao.insertItripUserLinkUser(i);
        return DtoUtil.returnSuccess();
    }


    @RequestMapping(value="/api/userinfo/deluserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public Dto deluserlinkuser(Integer[] ids) throws Exception {
       int num=dao.deleteItripUserLinkUserById(ids);
       if(num>0){
           return DtoUtil.returnSuccess("删除成功");
       }else{
           return DtoUtil.returnFail("删除常用联系人失败","100432 ");
       }

    }



    @RequestMapping(value="/api/userinfo/modifyuserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto modifyuserlinkuser(@RequestBody ItripModifyUserLinkUserVO vo) throws Exception {
       ItripUserLinkUser i=new ItripUserLinkUser();
       i.setLinkUserName(vo.getLinkUserName());
       i.setLinkPhone(vo.getLinkPhone());
       i.setLinkIdCard(vo.getLinkIdCard());
       i.setId(vo.getId());
        int num=dao.updateItripUserLinkUser(i);
        if(num>0){
            return DtoUtil.returnSuccess("修改成功");
        }else {
            return DtoUtil.returnFail("修改失败","100421");
        }



    }




}