package com.zqq.studyredisrabbitmq.springjava.listener;


import com.rabbitmq.client.Channel;
import com.zqq.studyredisrabbitmq.springjava.constant.Constants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkListener {

    @RabbitListener(queues = Constants.WORK_QUEUE)
    public void queueListener1(Message message, Channel channel){
        System.out.println("listener 1 ["+Constants.WORK_QUEUE+"] 接收到消息："+message+",channel："+channel);
    }

    @RabbitListener(queues = Constants.WORK_QUEUE)
    public void queueListener2(String message){
        System.out.println("listener 2 ["+Constants.WORK_QUEUE+"] 接收到消息："+message);
    }

}
