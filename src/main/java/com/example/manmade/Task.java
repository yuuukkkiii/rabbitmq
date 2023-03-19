package com.example.manmade;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class Task {
        public static final String TASK_QUEUE_NAME="ack";

        public static void main(String[] args) throws Exception {
                Channel channel = RabbitMQUtil.getChannel();
//                开启发布确认
                channel.confirmSelect();
                //持久化
                boolean durable =true;
                channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
                Scanner scanner=new Scanner(System.in);
                while(scanner.hasNext()){
                        String message =scanner.next();
                        /*
                         * 发送一个消费
                         * 1.发送到哪个交换机
                         * 2.路由的Key是哪个，本次是队列的名称
                         * 3.其他参数信息
                         * 4.发送的消息的消息体
                         * */
                        //3.参数：持久化，保存到磁盘上 MessageProperties.PERSISTENT_TEXT_PLAIN
                        channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
                        System.out.println("生产者发出消息： "+message);
                }
        }
}
