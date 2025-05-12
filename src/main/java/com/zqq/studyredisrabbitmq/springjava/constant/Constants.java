package com.zqq.studyredisrabbitmq.springjava.constant;


/**
 * 一些常量属性，比如交换机和队列的名称
 */
public class Constants {

    //    创建一个工作模式的队列
    public static final String WORK_QUEUE="work.queue";
    //    创建发布订阅模式的队列和交换机
    public static final String FANOUT_QUEUE1="fanout.queue1";
    public static final String FANOUT_QUEUE2="fanout.queue2";
    public static final String FANOUT_EXCHANGE="fanout.exchange";
    //    创建路由模式的队列和交换机
    public static final String DIRECT_QUEUE1="direct.queue1";
    public static final String DIRECT_QUEUE2="direct.queue2";
    public static final String DIRECT_EXCHANGE="direct.exchange";
    //    创建通配符模式的队列和交换机
    public static final String TOPIC_QUEUE1="topic.queue1";
    public static final String TOPIC_QUEUE2="topic.queue2";
    public static final String TOPIC_EXCHANGE="topic.exchange";


}
