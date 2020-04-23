package com.ysj.planetmessage.services.buss.receiver;


import com.ysj.planetmessage.services.buss.Payload;
import org.springframework.amqp.core.Message;

/**
 * @ClassName CorrelationDataX
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 14:45
 * @Version 1.0
 * 函数式接口，用于自定义处理异常消息，如保存到缓存或数据库中
 */
@FunctionalInterface
public interface ErrorMessageHandler<P extends Payload, M extends Message> {
    void accept(P payload, M message);
}
