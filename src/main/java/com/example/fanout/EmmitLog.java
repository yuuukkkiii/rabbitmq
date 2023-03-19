package com.example.fanout;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class EmmitLog {

    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMQUtil.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner =new Scanner(System.in);

        while(scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
