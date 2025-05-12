package com.zqq.studyredisrabbitmq.rabbitmqjava.fanout;

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
//        声明交换机(BuiltinExchangeType：内置交换机类型)
        channel.exchangeDeclare(Constants.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT,true);
//        声明队列
        channel.queueDeclare(Constants.FANOUT_QUEUE1,true,false,false,null);
        channel.queueDeclare(Constants.FANOUT_QUEUE2,true,false,false,null);
//        交换机和队列绑定
        channel.queueBind(Constants.FANOUT_QUEUE1,Constants.FANOUT_EXCHANGE,"");
        channel.queueBind(Constants.FANOUT_QUEUE2,Constants.FANOUT_EXCHANGE,"");
//        发布消息
        String msg="hello fanout .........";
        channel.basicPublish(Constants.FANOUT_EXCHANGE,"",null,msg.getBytes());
//        释放资源
        channel.close();
        connection.close();

    }

}
