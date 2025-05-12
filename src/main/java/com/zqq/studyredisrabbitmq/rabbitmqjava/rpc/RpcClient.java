package com.zqq.studyredisrabbitmq.rabbitmqjava.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * rpc 客户端
 * 1，发送请求
 * 2，响应请求
 */
public class RpcClient {


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
            channel.queueDeclare("rpc.request.queue", true, false, false, null);
            channel.queueDeclare("rpc.response.queue", true, false, false, null);
//            发送请求
            String msg="hello rpc ......";
//            设置请求的唯一标识
            String correlationID= UUID.randomUUID().toString();
//            设置请求的相关属性
            AMQP.BasicProperties props=new AMQP.BasicProperties().builder()
                    .correlationId(correlationID)
                    .replyTo("rpc.response.queue")
                    .build();
            channel.basicPublish("","rpc.request.queue",props,msg
                    .getBytes());

//            接收响应
//            使用阻塞队列
            final BlockingQueue<String> response=new ArrayBlockingQueue<>(1);
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("接收到回调消息："+new String(body));
                    if(correlationID.equals(properties.getCorrelationId())){
//                        校验一致的话就转发
                        response.offer(new String(body));
                    }
                }
            };

            channel.basicConsume("rpc.response.queue",true,consumer);
            String result = response.take();
            System.out.println("[RPC Client 响应结果]："+result);
        }

}
