package com.zqq.studyredisrabbitmq.rabbitmqjava.topic;

import com.rabbitmq.client.*;
import com.zqq.studyredisrabbitmq.springjava.constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class topicConsumer1 {

    public static void main(String[] args) throws IOException, TimeoutException {

        //        建立连接
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("chase");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
//        开启信道
        Channel channel = connection.createChannel();
//        声明队列
        channel.queueDeclare(Constants.TOPIC_QUEUE1,true,false,false,null);
//        消费队列
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收消息："+new String(body));
            }
        };

        channel.basicConsume(Constants.TOPIC_QUEUE1,true,consumer);

    }
}
