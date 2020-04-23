package com.ysj.planetmessage.services.buss.prod;

import com.ysj.planetmessage.services.buss.Payload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;

/**
 * @ClassName CorrelationDataX
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/14 14:40
 * @Version 1.0
 */
public interface MessageProduce extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, InitializingBean {

    void sendMessage(String routingKey, Payload payload, String msgId);
}
