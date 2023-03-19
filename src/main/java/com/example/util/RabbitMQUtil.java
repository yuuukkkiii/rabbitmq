package com.example.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {
    public static Channel getChannel() throws Exception {
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
        return channel;
    }
}
