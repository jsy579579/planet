package com.ysj.planetmessage.services.controller;

import com.ysj.planetmessage.services.buss.Payload;
import com.ysj.planetmessage.services.buss.entity.PatientPayload;
import com.ysj.planetmessage.services.buss.prod.MessageProduce;
import com.ysj.planetmessage.services.constant.RabbitConst;
import com.ysj.planetmessage.services.constant.UrlConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName TestCtl
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 14:18
 * @Version 1.0
 */
@RestController
public class TestCtl {
    private static final Logger LOG = LoggerFactory.getLogger(TestCtl.class);
    @Autowired
    private MessageProduce messageProduce;
    //测试队列
    @RequestMapping(method = RequestMethod.GET, value = UrlConstant.MESSAGE_SERVICE_URL+"/test")
    public void test(){
        Payload payload2 = new PatientPayload.Builder()
                .id(1)
                .name("jsy")
                .gender("ysj")
                .birthday(new Date())
                .build();
        messageProduce.sendMessage(RabbitConst.TOPIC_CONSUMEQUENE,payload2,"'");
    }
}
