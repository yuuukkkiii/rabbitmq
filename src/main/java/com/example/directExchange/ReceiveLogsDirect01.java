package com.example.directExchange;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

//        声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//        声明一个队列
        channel.queueDeclare("console",false,false,false,null);

        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        DeliverCallback deliverCallback=(consumerTag , message)->{
            System.out.println("ReceiveLogsDirect01控制台收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("console",true,deliverCallback,consumerTag->{});
    }
}
