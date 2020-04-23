package com.ysj.planetmessage.services.config;


import com.ysj.planetmessage.services.constant.RabbitConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitConfig
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 11:18
 * @Version 1.0
 */
@Configuration
public class RabbitConfig {
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    @Bean
    public Queue consumeQuene() {
        return new Queue(RabbitConst.TOPIC_CONSUMEQUENE);
    }

    @Bean
    public Queue repaymentQuene() {
        return new Queue(RabbitConst.TOPIC_REPAYMENTQUENE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(RabbitConst.TOPIC_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeConsumeMessages(Queue consumeQuene, TopicExchange exchange) {
        return BindingBuilder.bind(consumeQuene).to(exchange).with(RabbitConst.TOPIC_CONSUMEKEY);
    }

    @Bean
    Binding bindingExchangeRepaymentMessages(Queue repaymentQuene, TopicExchange exchange) {
        return BindingBuilder.bind(repaymentQuene).to(exchange).with(RabbitConst.TOPIC_REPAYMENTKEY);
    }


}
