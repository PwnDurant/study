package com.zqq.studyredisrabbitmq.springjava.listener;

import com.rabbitmq.client.Channel;
import com.zqq.studyredisrabbitmq.springjava.constant.Constants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectListener {

    @RabbitListener(queues = Constants.DIRECT_QUEUE1)
    public void queueListener1(Message message, Channel channel){
        System.out.println("listener 1 ["+Constants.DIRECT_QUEUE1+"] 接收到消息："+message+",channel："+channel);
    }

    @RabbitListener(queues = Constants.DIRECT_QUEUE2)
    public void queueListener2(String message){
        System.out.println("listener 2 ["+Constants.DIRECT_QUEUE2+"] 接收到消息："+message);
    }

}
