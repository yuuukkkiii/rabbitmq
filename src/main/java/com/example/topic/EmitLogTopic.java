package com.example.topic;

import com.example.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMQUtil.getChannel();

        Map<String,String> bindingKeyMap =new HashMap<>();

        bindingKeyMap.put("quick.orange.rabbit","Q1Q2接收");
        bindingKeyMap.put("lazy.orange.elephant","Q1Q2接收");
        bindingKeyMap.put("quick.orange.fox","Q1接收");
        bindingKeyMap.put("lazy.brown.fox","Q2接收");
        bindingKeyMap.put("lazy.pink.rabbit","两个都满足条件，但Q2接收一次");
        bindingKeyMap.put("quick.brown.fox","都不接收，丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","四个单词不匹配");
        bindingKeyMap.put("lazy.orange.male.rabbit","四个单词匹配Q2");

        for(Map.Entry<String,String> bindingKeyEntry:bindingKeyMap.entrySet()){
            String routingKey=bindingKeyEntry.getKey();
            String message =bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
