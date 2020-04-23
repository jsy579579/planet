package com.ysj.planetmessage.services.buss.prod.impl;


import com.alibaba.fastjson.JSON;
import com.ysj.planetmessage.services.buss.CorrelationDataX;
import com.ysj.planetmessage.services.buss.Payload;
import com.ysj.planetmessage.services.buss.prod.MessageProduce;
import com.ysj.planetmessage.services.constant.RabbitConst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName CorrelationDataX
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/14 14:45
 * @Version 1.0
 */
@Component
public class BasicMessageProduce implements MessageProduce {
    private final Logger logger = LoggerFactory.getLogger(BasicMessageProduce.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("{spring.rabbitmq.retry.maxAttempts}")
    private String retryMaxAttempts;



    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void sendMessage(String routingKey, Payload payload, String msgId){
        CorrelationDataX correlationData = new CorrelationDataX
                .Builder()
                .id(msgId)
                .message(payload)
                .exchange(RabbitConst.TOPIC_EXCHANGE)
                .routingKey(routingKey)
                .build();
        logger.info("发送MQ消息，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                correlationData.getId(), JSON.toJSONString(payload), RabbitConst.TOPIC_EXCHANGE, routingKey);

        convertAndSend(routingKey, payload, correlationData,msgId);

        logger.info("结束发送消息：{}", JSON.toJSONString(payload));
    }

    private void convertAndSend(String routingKey, final Object payload, CorrelationDataX correlationData,String msgId) {
        try {

            rabbitTemplate.convertAndSend(RabbitConst.TOPIC_EXCHANGE, routingKey, payload, correlationData);
        } catch (Exception e) {
            logger.error("MQ消息发送异常，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                    correlationData.getId(), JSON.toJSONString(payload), RabbitConst.TOPIC_EXCHANGE, routingKey, e);

            // TODO 保存到数据库或缓存中

        }
    }

    /**
     * 用于实现消息发送到RabbitMQ交换器后接收ack回调。
     * 如果消息发送确认失败就进行重试。
     * @param correlationData CorrelationDataX
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 消息回调确认失败处理
        if (!ack && correlationData instanceof CorrelationDataX) {
            CorrelationDataX correlationDataX = (CorrelationDataX) correlationData;
            //消息发送失败,就进行重试，重试过后还不能成功就记录到数据库或缓存
            if (correlationDataX.getRetryCount() < Integer.valueOf(retryMaxAttempts)) {
                logger.info("MQ消息发送失败，消息重发，消息ID：{}，重发次数：{}，消息体:{}", correlationDataX.getId(),
                        correlationDataX.getRetryCount(), JSON.toJSONString(correlationDataX.getMessage()));
                // 将重试次数加一
                correlationDataX.setRetryCount(correlationDataX.getRetryCount() + 1);
                // 重新发送
                convertAndSend(correlationDataX.getRoutingKey(),
                        correlationDataX.getMessage(), correlationDataX,correlationDataX.getId());
            } else {
                //消息重试发送失败,将消息放到数据库等待补发
                logger.warn("MQ消息重发失败，消息入库，消息ID：{}，消息体:{}", correlationData.getId(),
                        JSON.toJSONString(correlationDataX.getMessage()));
                // TODO 保存到数据库或缓存

            }
        } else {
            logger.info("消息发送成功,消息ID:{}", correlationData.getId());
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        logger.error("MQ消息发送失败，replyCode:{}, replyText:{}，exchange:{}，routingKey:{}，消息体:{}",
                replyCode, replyText, exchange, routingKey, JSON.toJSONString(message.getBody()));

        // TODO 保存消息到数据库或缓存
    }
}
