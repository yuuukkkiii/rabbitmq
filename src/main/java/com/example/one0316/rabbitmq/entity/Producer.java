package com.example.one0316.rabbitmq.entity;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String QUEUE_NAME ="hello";

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
        //这里使用默认的交换机，所以直接连接队列
        /*
        * 生成一个队列
        * 参数：
        * 1.队列名称
        * 2.队列的消息是否持久化，默认情况下是不持久化的存放在内存中
        * 3.该队列是否只供一个消费者消费，是否消息共享，true可以多个消费者消费，false只能一个消费者消费
        * 4。是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除
        * 5.其他参数
        * */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发消息
        String message ="Hello World:";
        /*
        * 发送一个消费
        * 1.发送到哪个交换机
        * 2.路由的Key是哪个，本次是队列的名称
        * 3.其他参数信息
        * 4.发送的消息的消息体
        * */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.printf("消息发送完毕");
    }
}
