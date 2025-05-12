package com.zqq.studyredisrabbitmq.rabbitmqjava.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory=new ConnectionFactory();

        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("chase");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare("test",true,false,false,null);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收到消息："+new String(body));
            }
        };

        channel.basicConsume("test", true, consumer);

        TimeUnit.SECONDS.sleep(5);

    }
}


//只有前台线程（非守护线程）才会影响进程是否结束
//被创建出来线程默认就是前台线程，要设置 Daemon 属性为 true 才可以变为后台线程（守护线程）
