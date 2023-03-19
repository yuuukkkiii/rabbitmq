package com.example.eight;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

//死信队列生产者
public class Producer01 {
    public static final String NORMAL_EXCHANGE="normal_exchange";
    public static final String DEAD_EXCHANGE="dead_exchange";
    public static final String NORMAL_QUEUE="normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMQUtil.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        Map<String,Object> arguments=new HashMap<>();
//        过期时间
//        10s
        //       arguments.put("x-message-ttl",10000);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

//        私信消息 设置TTL时间
        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message="info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan", properties, message.getBytes());
        }
    }
}
