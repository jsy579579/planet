package com.ysj.planetmessage.services.buss.receiver.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.ysj.planetmessage.services.buss.Payload;
import com.ysj.planetmessage.services.buss.receiver.ErrorMessageHandler;
import com.ysj.planetmessage.services.constant.RabbitConst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;


/**
 * @ClassName CorrelationDataX
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 14:41
 * @Version 1.0
 */
@Component
@ConditionalOnClass({RabbitTemplate.class})
public class MessageListener {

    private final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    //
    @RabbitListener(queues = "topic.consumeQuene")
    public void onReceiveQueue1Message(Payload payload, Channel channel, Message message) {
        try {
            doReceiveMessage(RabbitConst.TOPIC_CONSUMEKEY, payload, channel, message,
                    p -> logger.info("队列[{}]接收到消息已被处理，消息Id:{} 消息体:{}", RabbitConst.TOPIC_CONSUMEKEY,
                            message.getMessageProperties().getClusterId(), JSON.toJSONString(payload)),
                    (p, m) -> logger.info("队列[{}]接收到异常消息已被处理，消息Id:{} 消息体:{}", RabbitConst.TOPIC_CONSUMEKEY,
                            message.getMessageProperties().getClusterId(), JSON.toJSONString(payload)));
        } catch (Exception e) {
            logger.info("消息处理异常----------------------");
            e.printStackTrace();
        }
    }

    //
    @RabbitListener(queues = "topic.repaymentQuene")
    public void onReceiveQueue2Message(Payload payload, Channel channel, Message message) {
        try {
            doReceiveMessage(RabbitConst.TOPIC_REPAYMENTKEY, payload, channel, message,
                    p -> logger.info("队列[{}]接收到消息已被处理，消息Id:{} 消息体:{}", RabbitConst.TOPIC_REPAYMENTKEY,
                            message.getMessageProperties().getClusterId(), JSON.toJSONString(payload)),
                    (p, m) -> logger.info("队列[{}]接收到异常消息已被处理，消息Id:{} 消息体:{}", RabbitConst.TOPIC_REPAYMENTKEY,
                            message.getMessageProperties().getClusterId(), JSON.toJSONString(payload)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doReceiveMessage(String queueName, Payload payload, Channel channel, Message message,
                                  Consumer<Payload> handleMessage,
                                  ErrorMessageHandler<Payload, Message> handleErrorMessage) throws IOException {
        String jsonPayload = JSON.toJSONString(payload);
        logger.info("队列[{}]接收到数据，消息体：{}", queueName, jsonPayload);
        // Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
        // 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
        // RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(deliveryTag);

        Map<String,Object> map = message.getMessageProperties().getHeaders();
        //获取到消息id
        String correlationId = (String) map.get("spring_listener_return_correlation");

        //这种方式实际上获取不到的
//        String correlationId = message.getMessageProperties().getCorrelationId();
//        String msgId = message.getMessageProperties().getMessageId();
        try {
            // 处理消息
            if (null != handleMessage) {
                handleMessage.accept(payload);
                // 确认消息消费成功
                // basicAck参数：
                // 第一个参数是 Delivery Tag
                // 第二个参数 multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认；如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认（批量确认针对的是整个信道）。
                channel.basicAck(deliveryTag, false);
            } else {
                logger.info("队列[{}]接收到消息已被处理，但没有设置处理机制：消息Id:{} 消息体:{}", queueName, correlationId, jsonPayload);
            }

        } catch (Exception e) {
            logger.error("消息处理异常:\n消息Id:{}，\n 消息体:{}\n 异常信息:{}", correlationId, jsonPayload, e);
            try {
                // 异常消息处理
                if (null != handleErrorMessage) {
                    handleErrorMessage.accept(payload, message);
                } else {
                    logger.info("队列[{}]接收到异常消息，但没有设置处理机制，消息Id:{} 消息体:{}", queueName, correlationId, jsonPayload);
                }
                // 确认消息已经消费成功
                channel.basicAck(deliveryTag, false);
            } catch (Exception e2) {
                logger.error("异常消息持久化失败，放到死信队列，消息体：{}", jsonPayload, e2);
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException ie) {
                    // 如果将消息放到死信队列时也失败，则丢弃该消息
                    channel.basicReject(deliveryTag, false);
                }
            }
        }
    }
}
