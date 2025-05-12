package com.zqq.studyredisrabbitmq.rabbitmqjava.topic;

import com.rabbitmq.client.BuiltinExchangeType;
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
//        声明交换机
        channel.exchangeDeclare(Constants.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC,true);
//        声明队列
        channel.queueDeclare(Constants.TOPIC_QUEUE1,true,false,false,null);
        channel.queueDeclare(Constants.TOPIC_QUEUE2,true,false,false,null);
//        绑定交换机和队列
        channel.queueBind(Constants.TOPIC_QUEUE1,Constants.TOPIC_EXCHANGE,"*.a.*");
        channel.queueBind(Constants.TOPIC_QUEUE2,Constants.TOPIC_EXCHANGE,"*.*.b");
        channel.queueBind(Constants.TOPIC_QUEUE2,Constants.TOPIC_EXCHANGE,"c.#");
//        发送消息
        String msg="hello topic ae.a.f...";
        channel.basicPublish(Constants.TOPIC_EXCHANGE,"ae.a.f",null,msg.getBytes());
        String msg_b="hello topic ef.a.b...";
        channel.basicPublish(Constants.TOPIC_EXCHANGE,"ef.a.b",null,msg_b.getBytes());
        String msg_c="hello topic c.ef.d...";
        channel.basicPublish(Constants.TOPIC_EXCHANGE,"c.ef.d",null,msg_c.getBytes());
        System.out.println("消息发送成功");
//        释放资源
        channel.close();
        connection.close();
    }

}
