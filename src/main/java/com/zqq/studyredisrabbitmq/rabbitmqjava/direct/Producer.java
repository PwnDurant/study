package com.zqq.studyredisrabbitmq.rabbitmqjava.direct;

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
        channel.exchangeDeclare(Constants.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT,true);
//        声明队列
        channel.queueDeclare(Constants.DIRECT_QUEUE1,true,false,false,null);
        channel.queueDeclare(Constants.DIRECT_QUEUE2,true,false,false,null);
//        绑定交换机和队列
        channel.queueBind(Constants.DIRECT_QUEUE1,Constants.DIRECT_EXCHANGE,"a");
        channel.queueBind(Constants.DIRECT_QUEUE2,Constants.DIRECT_EXCHANGE,"a");
        channel.queueBind(Constants.DIRECT_QUEUE2,Constants.DIRECT_EXCHANGE,"b");
        channel.queueBind(Constants.DIRECT_QUEUE2,Constants.DIRECT_EXCHANGE,"c");
//        发送消息
        String msg="hello direct a...";
        channel.basicPublish(Constants.DIRECT_EXCHANGE,"a",null,msg.getBytes());
        String msg_b="hello direct b...";
        channel.basicPublish(Constants.DIRECT_EXCHANGE,"b",null,msg_b.getBytes());
        String msg_c="hello direct c...";
        channel.basicPublish(Constants.DIRECT_EXCHANGE,"c",null,msg_c.getBytes());
        System.out.println("消息发送成功");
//        释放资源
        channel.close();
        connection.close();
    }

}
