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

public class Consumer01 {
//    普通交换机

    public static final String NORMAL_EXCHANGE="normal_exchange";
//    死信交换机
    public static final String DEAD_EXCHANGE="dead_exchange";
//    普通队列
    public static final String NORMAL_QUEUE="normal_queue";
//    死信队列
    public static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

//        声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
//        死信
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
//声明交换机和普通交换机 类型是direct

        Map<String,Object> arguments=new HashMap<>();
//        过期时间
//        10s
 //       arguments.put("x-message-ttl",10000);
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");

        //   设置正常队列的长度限制

 //       arguments.put("x-max-length", 6);


        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

//        声明死信队列

        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

//        绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息。。。");

//        正常接收消息
        DeliverCallback deliverCallback=(consumerTag , message)->{
            String msg=new String(message.getBody(),"UTF-8");
            if(msg.equals("info5")){
                System.out.println("consumer01拒绝的消息为：" + new String(message.getBody(), "UTF-8"));
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("consumer01：" + new String(message.getBody(), "UTF-8"));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
//        注意开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag->{});
    }
}
