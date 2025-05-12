package com.zqq.studyredisrabbitmq.rabbitmqjava.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();

        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("chase");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare("test",true,false,false,null);

        channel.basicPublish("","test",null,"测试".getBytes());

        channel.close();
        connection.close();
    }
}
