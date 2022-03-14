package com.bdqn.controller;

import cn.itrip.dao.itripHotel.ItripHotelMapper;

import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSONArray;

import com.bdqn.Email.Mail;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller

public class itripController {

            public static void sentsm(String phone,String message){
                //生产环境请求地址：app.cloopen.com
                String serverIp = "app.cloopen.com";
                //请求端口
                String serverPort = "8883";
                //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
                String accountSId="8a216da87f63aaf1017f6c3424f301a0";
                String accountToken="8fa72237867047089abcce23983f181a";
                //请使用管理控制台中已创建应用的APPID
                String appId = "8a216da87f63aaf1017f6c34260101a7";
                CCPRestSmsSDK sdk = new CCPRestSmsSDK();
                sdk.init(serverIp, serverPort);
                sdk.setAccount(accountSId, accountToken);
                sdk.setAppId(appId);
                sdk.setBodyType(BodyType.Type_JSON);
                String to = phone;
                String templateId= "1";
                String[] datas = {message};
                //String subAppend="1234";  //可选	扩展码，四位数字 0~9999
               // String reqId="fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
                HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
               // HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
                if("000000".equals(result.get("statusCode"))){
                    //正常返回输出data包体信息（map）
                    HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                    Set<String> keySet = data.keySet();
                    for(String key:keySet){
                        Object object = data.get(key);
                        System.out.println(key +" = "+object);
                    }
                }else{
                    //异常返回输出错误码和错误信息
                    System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
                }
            }



    @Resource
    ItripHotelMapper dao;

    @Resource
    ItripUserMapper dao1;

    @Resource
    TokenBiz biz;

    @Resource
    RedisUtil redisUtil;

    @Resource
    ItripUserMapper dao2;



        /*手机号注册*/
    @RequestMapping(value="/api/registerbyphone",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Register(@RequestBody ItripUserVO vo) throws Exception {
        //把前台数据插入到数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(vo.getUserCode());
        user.setUserName(vo.getUserName());
        user.setUserPassword(vo.getUserPassword());
        //发送短信验证码
        dao2.insertItripUser(user);
        Random random=new Random(4);
        int math=random.nextInt(9999);
        sentsm(vo.getUserCode(),math+"");
        //存在redis中 一会去redis比较
        redisUtil.setRedis(vo.getUserCode(),math+"");
        return DtoUtil.returnSuccess();
    }



        /*手机号激活*/
 @RequestMapping(value="/api/validatephone",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Description(String user,String code,HttpServletRequest request) throws Exception {
        String s=redisUtil.getRedis(user);
        if(s.equals(code)){
           dao2.update(user);
            return DtoUtil.returnSuccess("激活成功");
        }
        return DtoUtil.returnFail("激活失败","10000");
    }











    @RequestMapping(value="/api/dologin",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Login(String name, String password, HttpServletRequest request) throws Exception {
        Map b=new HashMap<>();
        b.put("a",name);
        b.put("b",password);
        ItripUser user=dao1.getItripUserListByMap(b);
        if(user!=null){
            //模拟session的票据
            String token=biz.generateToken(request.getHeader("User-Agent"),user);

            //把tonke存储到Redis中
            redisUtil.setRedis(token,JSONArray.toJSONString(user));
            //获取当时时间 存储两个小时
             ItripTokenVO itripTokenVO= new ItripTokenVO(token,Calendar.getInstance().getTimeInMillis()*3600*2,Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(itripTokenVO);
        }
        return DtoUtil.returnFail("登录失败","1000");
       // return JSONArray.toJSONString(user);
    }



        /*判断是否注册*/
    @RequestMapping(value="/api/ckusr",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object ckusr(String name) throws Exception {
        ItripUser user = dao1.ckusr(name);
        if (user != null) {
            return DtoUtil.returnFail("邮箱已被注册", "10000");
        } else {
            return DtoUtil.returnSuccess();
        }
    }



        /*邮箱注册*/
    @RequestMapping(value="/api/doregister",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object email(@RequestBody ItripUserVO vo) throws Exception {
        //把前台数据插入到数据库中
        ItripUser u=new ItripUser();
        u.setUserCode(vo.getUserCode());
        u.setUserName(vo.getUserName());
        u.setUserPassword(vo.getUserPassword());
        //发送短信验证码
        dao2.insertItripUser(u);
        Random random=new Random(4);
        int math=random.nextInt(9999);
        Mail m=new Mail();
       m.SentSmail(vo.getUserCode(),math+"");
        //存在redis中 一会去redis比较
        redisUtil.setRedis(vo.getUserCode(),math+"");
        return DtoUtil.returnSuccess();
    }

        /*邮箱激活*/
    @RequestMapping(value="/api/activate",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object activate(String user,String code) throws Exception {
        String s=redisUtil.getRedis(user);
        if(s.equals(code)){
            dao2.update(user);
            return DtoUtil.returnSuccess("激活成功");
        }
        return DtoUtil.returnFail("激活失败","10000");
    }








}
