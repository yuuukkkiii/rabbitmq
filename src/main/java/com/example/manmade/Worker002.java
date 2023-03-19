package com.example.manmade;

import com.example.util.RabbitMQUtil;
import com.example.util.SleepUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Worker002 {
    public static final  String QUEUE_NAME="ack";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("C2等待接受消息处理时间较长");

//采用手动应答
        boolean autoAck = false;
        /*
         * 消费者消费消息
         * 1.消费那个队列
         * 2.消费成功后是否要自动应答，true表示自动答复
         * 3.处理推送过来消息的回调函数
         * 4.消费者取消消费的回调
         * */
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡
            SleepUtil.sleep(30);
            System.out.println("接收到的消息： " + new String(message.getBody()));
            //手动应答

            /*
             * 1.消息的标识
             * 2.是否批量应答
             * */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        int prefetchCount=5;
        channel.basicQos(prefetchCount);
//        取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.printf(consumerTag + "  消费者取消消费接口回调逻辑");
        };
        System.out.println("C2等待接收消息");
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
