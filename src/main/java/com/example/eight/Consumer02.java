package com.example.eight;

/*
* 死信队列
* */

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {

//    死信队列
    public static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

//        正常接收消息
        DeliverCallback deliverCallback=(consumerTag , message)->{
            System.out.println("consumer02："+new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag->{});
    }
}
