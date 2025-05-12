package com.zqq.studyredisrabbitmq.rabbitmqjava.confirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zqq.studyredisrabbitmq.springjava.constant.Constants;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 发布确认机制 客户端 --> broker 之间的确认关系
 */
public class PublisherConfirms {
    private static final Integer MESSAGE_COUNT=200;
    static Connection createConnection() throws Exception{
        ConnectionFactory con = new ConnectionFactory();
        con.setHost("127.0.0.1");
        con.setPort(5672);
        con.setUsername("admin");
        con.setPassword("admin");
        con.setVirtualHost("chase");
        return con.newConnection();
    }

    public static void main(String[] args) throws Exception{

//        单独确认
//        publishingMessagesIndividually();

//        批量确认
        publishingMessagesInBatches();

//        异步确认
        handlingPublisherConfirmsAsynchronously();

    }

    /**
     * 异步确认
     * @throws Exception
     */
    private static void handlingPublisherConfirmsAsynchronously() throws Exception {
        try(Connection connection=createConnection()){
//            开启信道
            Channel channel = connection.createChannel();
//            设置信道为 confirm 模式
            channel.confirmSelect();
//            声明队列
            channel.queueDeclare("publish.confirm.queue3",true,false,false,null);
//            监听 confirm
//            集合中存储的是未确认的消息ID
            long start=System.currentTimeMillis();
            SortedSet<Long> confirmsSeqNo= Collections.synchronizedSortedSet(new TreeSet<>());

            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    if(multiple){
                        confirmsSeqNo.headSet(deliveryTag+1).clear();
                    }else{
                        confirmsSeqNo.remove(deliveryTag);
                    }
                }
//              失败的场景要根据实际业务来看：比如重发
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    if(multiple){
                        confirmsSeqNo.headSet(deliveryTag+1).clear();
                    }else{
                        confirmsSeqNo.remove(deliveryTag);
                    }
                }
            });
//            发送消息
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String msg="hello publisher confirms"+i;
                long seqNo=channel.getNextPublishSeqNo();
                channel.basicPublish("","publish.confirm.queue3",null,msg.getBytes());
                confirmsSeqNo.add(seqNo);
            }
//            while (!confirmsSeqNo.isEmpty()){
//                Thread.sleep(10);
//            }
            long end=System.currentTimeMillis();
            System.out.println("异步确认策略耗时："+(end-start));
        }

    }

    /**
     * 批量确认
     */
    private static void publishingMessagesInBatches() throws Exception {
        try(Connection connection=createConnection()){
//            开启信道
            Channel channel = connection.createChannel();
//            设置信道为 confirm 模式
            channel.confirmSelect();
//            声明队列
            channel.queueDeclare("publish.confirm.queue2",true,false,false,null);
//            发送消息并确认
            long start= System.currentTimeMillis();
            int batchSize=100;
            int outstandingMessageCount=0;
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String msg="hello publisher confirms"+i;
                channel.basicPublish("","publish.confirm,queue2",null,msg.getBytes());
                outstandingMessageCount++;
                if(outstandingMessageCount==batchSize){
                    channel.waitForConfirmsOrDie(5000);
                    outstandingMessageCount=0;
                }
            }
            if(outstandingMessageCount>0){
                channel.waitForConfirmsOrDie(5000);
            }
            long end=System.currentTimeMillis();
            System.out.println("批量确认策略："+(end-start));
        }


    }

    /**
     * 单独确认
     */
    private static void publishingMessagesIndividually() throws Exception {
        try(Connection connection=createConnection()){
//            开启信道
            Channel channel = connection.createChannel();
//            设置信道为 confirm 模式
            channel.confirmSelect();
//            声明队列
            channel.queueDeclare("publish.confirm.queue1",true,false,false,null);
//            发送消息
            long start=System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String msg="hello publisher confirm "+i;
                channel.basicPublish("","publish.confirm.queue1",null,msg.getBytes());
//                等待确认
                channel.waitForConfirms(5000);
            }
            long end=System.currentTimeMillis();
            System.out.println("单独确认所花费的时间:"+(end-start));
        }
    }

}
