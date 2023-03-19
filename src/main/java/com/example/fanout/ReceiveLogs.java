package com.example.fanout;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs {

    public static final String EXCHANGE_NAME="logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
//        声明一个队列   临时队列
        /*
        *
        *
        * 队列名称随机
        * 消费者断开后队列自动删除
        * */
        String queueName= channel.queueDeclare().getQueue();
        /*
        * 绑定交换机与队列
        * */
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("ReceiveLogs等待接收消息，把结果打印在屏幕上");

        DeliverCallback deliverCallback=(consumerTag ,message)->{
            System.out.println("ReceiveLogs控制台收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }
}
