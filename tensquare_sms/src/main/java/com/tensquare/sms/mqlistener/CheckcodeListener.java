package com.tensquare.sms.mqlistener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queuesToDeclare = @Queue(value = "sms.checkcode"))
public class CheckcodeListener {

    @RabbitHandler
    public void handleCheckcode(Map<String, Object> map) {
        String mobile = map.get("mobile").toString();
        String checkcode = map.get("checkCode").toString();
        System.out.println("mobile======"+mobile+"=======checkCode===="+checkcode);
    }
}
