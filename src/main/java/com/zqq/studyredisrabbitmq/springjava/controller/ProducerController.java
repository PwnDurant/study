package com.zqq.studyredisrabbitmq.springjava.controller;


import com.zqq.studyredisrabbitmq.springjava.constant.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/producer")
@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    工作模式
    @RequestMapping("/work")
    public String work(){
        for (int i = 0; i < 10; i++) {
            //        使用内置交换机，RoutingKey 和队列的名称一致
            rabbitTemplate.convertAndSend("", Constants.WORK_QUEUE,"hello spring amqp:work ...");
        }
        return "发送成功";
    }

//    订阅发布模式（fanout）
    @RequestMapping("/fanout")
    public String fanout(){
        rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE,"","hello fanout");
        return "发送成功";
    }

//    路由模式
    @RequestMapping("/direct/{routKey}")
    public String direct(@PathVariable("routKey") String routKey){
        rabbitTemplate.convertAndSend(Constants.DIRECT_EXCHANGE,routKey,"hello directModel "+routKey);
        return "发送成功";
    }

//    通配符模式
    @RequestMapping("/topic/{routKey}")
    public String topic(@PathVariable("routKey") String routKey){
        rabbitTemplate.convertAndSend(Constants.TOPIC_EXCHANGE,routKey,"hello topicModel "+routKey);
        return "发送成功";
    }





}
