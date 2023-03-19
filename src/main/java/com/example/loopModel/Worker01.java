package com.example.loopModel;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

//工作线程（消费者）
public class Worker01 {
    public static final  String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMQUtil.getChannel();


        /*
         * 消费者消费消息
         * 1.消费那个队列
         * 2.消费成功后是否要自动应答，true表示自动答复
         * 3.处理推送过来消息的回调函数
         * 4.消费者取消消费的回调
         * */
        DeliverCallback deliverCallback=(consumerTag, message) ->{
            System.out.println("接收到的消息： "+new String(message.getBody()));
        };

//        取消消息时的回调
        CancelCallback cancelCallback= consumerTag ->{
            System.out.printf(consumerTag+"  消费者取消消费接口回调逻辑");
        };
        System.out.println("C2等待接收消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
