package com.example.one0316.rabbitmq.entity;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public  static  final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory =new ConnectionFactory();
        //工厂IP
        factory.setHost("8.142.110.33");
        //用户名
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建链接
        Connection connection=factory.newConnection();
        //获取连接里的信道，连接里含有多个信道
        Channel channel= connection.createChannel();

        /*
        * 消费者消费消息
        * 1.消费那个队列
        * 2.消费成功后是否要自动应答，true表示自动答复
        * 3.处理推送过来消息的回调函数
        * 4.消费者取消消费的回调
        * */
//声明接收消息
        DeliverCallback deliverCallback=(consumerTag,message) ->{
            if(message==null){
                System.out.println("无生产消息");
            }
            System.out.println(message.toString());
        };

//        取消消息时的回调
        CancelCallback cancelCallback=consumer ->{
            System.out.printf("消息被中断");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
