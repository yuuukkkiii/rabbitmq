package com.example.delayQueue.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

//发送延时消息
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{}，发送一条信息给两个TTL队列：{}", LocalDateTime.now(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列"+message);
    }
}
