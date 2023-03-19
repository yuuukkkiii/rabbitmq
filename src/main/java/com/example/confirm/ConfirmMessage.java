package com.example.confirm;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

//发布确认
/*
* 1.单个确认
* 2.批量确认
* 3.异步批量确认
* */
public class ConfirmMessage {

    public static  final  int MESSAGE_COUNT=1000;

    public static void main(String[] args) throws Exception {
    //    ConfirmMessage.publishMessageIndividually();  //发布1000单独确认消息，耗时37731ms
      //  ConfirmMessage.publishMessageBatch(); //发布1000批量确认消息，耗时741ms
        ConfirmMessage.publishMessageAsync();//发布1000条异步发布确认消息，耗时43512ms
    }

    //单个发送
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
//        开启发布确认
//        记录开始时间
        channel.confirmSelect();
        long begin =System.currentTimeMillis();

//        批量发消息，并确认
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message=i+" ";
            channel.basicPublish("",queueName,null,message.getBytes());
//            单个消息就马上发布确认
            boolean flag =channel.waitForConfirms();
            if(flag){
                System.out.println("消息发布成功");
            }
        }
        long end =System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"单独确认消息，耗时"+(end-begin)+"ms");
    }

//批量发布
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
//        开启发布确认
//        记录开始时间
        channel.confirmSelect();
        long begin =System.currentTimeMillis();

        //批量确认消息大小
        int batchSize=100;
//        批量发消息，并确认
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message=i+" ";
            channel.basicPublish("",queueName,null,message.getBytes());
            if((i+1)%batchSize==0){
                channel.waitForConfirms();
            }
        }
        long end =System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"每"+batchSize+"条批量确认消息，耗时"+(end-begin)+"ms");
    }

//    异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
//        开启发布确认
//        记录开始时间
        channel.confirmSelect();
        /*
        * 线程安全有序地一个哈希表，适用于高并发的情况下
        * 1.轻松的将序号与消息关联
        * 2.轻松的批量删除条目
        * 3.支持高并发（多线程）
        *
        * */
        ConcurrentSkipListMap<Long,String> outstandingConfirms= new ConcurrentSkipListMap<>();

//        准备消息的监听器，监听那些消息成功了，那些消息失败了

//        消息确认成功的回调函数
        ConfirmCallback ackCallback=(deliveryTag,mulType)->{
            if(mulType){
//                感觉可以理解成，如果是批量的，则从deliveryTag标识的ack序号处批量往前删除，headMap感觉可以理解成一个结束位置
                ConcurrentNavigableMap<Long,String> confirmed=outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else{
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认的消息： "+deliveryTag);
        };

        //        消息确认失败的回调函数
        /*
        * 1.消息的标记
        * 2.是否为批量确认*/
        ConfirmCallback nackCallback=(deliveryTag,mulType)->{
            String message =outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息： "+message);
        };
        channel.addConfirmListener(ackCallback,nackCallback);

        long begin =System.currentTimeMillis();

//        批量发消息，并确认
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message=i+" ";
            channel.basicPublish("",queueName,null,message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
            //此处记录下要所有发送的消息、

        }
        long end =System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"条异步发布确认消息，耗时"+(end-begin)+"ms");
    }

}
