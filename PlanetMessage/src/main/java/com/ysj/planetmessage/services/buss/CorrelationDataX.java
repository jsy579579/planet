package com.ysj.planetmessage.services.buss;


import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * @ClassName CorrelationDataX
 * @Description TODO
 * @Author ysj
 * @Date 2020/4/23 14:15
 * @Version 1.0
 */
public class CorrelationDataX extends CorrelationData {
    private final String exchange;
    private final String routingKey;
    private int retryCount = 0;
    private final Object message;

    public Object getMessage() {
        return message;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public int getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    private CorrelationDataX(Builder builder) {
        this.setId(builder.id);
        this.message = builder.message;
        this.exchange = builder.exchange;
        this.routingKey = builder.routingKey;
    }

    public static class Builder {
        private String id;
        private String exchange;
        private String routingKey;
        private volatile Object message;

        public Builder id(String messageId) {
            this.id = messageId;
            return this;
        }

        public Builder message(Object message) {
            this.message = message;
            return this;
        }

        public Builder exchange(String exchange) {
            this.exchange = exchange;
            return this;
        }

        public Builder routingKey(String routingKey) {
            this.routingKey = routingKey;
            return this;
        }

        public CorrelationDataX build() {
            return new CorrelationDataX(this);
        }
    }
}
