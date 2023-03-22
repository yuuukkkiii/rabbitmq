package com.example.loopModel;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Task01 {

    public static final  String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        /*
         * 生成一个队列
         * 参数：
         * 1.队列名称
         * 2.队列的消息是否持久化，默认情况下是不持久化的存放在内存中
         * 3.该队列是否只供一个消费者消费，是否消息独占，true独占
         * 4。是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除
         * 5.其他参数
         * */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            String message =scanner.next();
            new String();
            /*
             * 发送一个消费
             * 1.发送到哪个交换机
             * 2.路由的Key是哪个，本次是队列的名称
             * 3.其他参数信息
             * 4.发送的消息的消息体
             * */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成： "+message);
        }

    }
}
