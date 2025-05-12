package com.zqq.studyredisrabbitmq.rabbitmqjava.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zqq.studyredisrabbitmq.springjava.constant.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

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
        channel.queueDeclare(Constants.WORK_QUEUE,true,false,false,null);
//        发送消息
        for (int i = 0; i < 10; i++) {
            String msg="hello work";
            channel.basicPublish("",Constants.WORK_QUEUE,null,msg.getBytes());
        }
        System.out.println("消息发送成功");
//        关闭资源
        channel.close();
        connection.close();
    }
}
