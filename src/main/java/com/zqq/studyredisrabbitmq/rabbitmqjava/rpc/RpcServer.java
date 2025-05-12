package com.zqq.studyredisrabbitmq.rabbitmqjava.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * RPC 服务端
 */
public class RpcServer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory con = new ConnectionFactory();
        con.setHost("127.0.0.1");
        con.setPort(5672);
        con.setUsername("admin");
        con.setPassword("admin");
        con.setVirtualHost("chase");
        Connection connection=con.newConnection();
//            开启信道
            Channel channel = connection.createChannel();
//            接收请求
            channel.basicQos(1);
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String request = new String(body, "UTF-8");
                    System.out.println("接收到请求:"+request);
                    String response="针对request："+request+",响应成功";
                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    channel.basicPublish("","rpc.response.queue",basicProperties,response.getBytes());
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };
            channel.basicConsume("rpc.request.queue",false,consumer);

        }


}
