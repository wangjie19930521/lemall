package com.leyoumall.mq;

import com.aliyuncs.exceptions.ClientException;
import com.leyoumall.common.utils.JsonUtils;
import com.leyoumall.config.SmsProperties;
import com.leyoumall.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @ClassName ListenSms
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
public class ListenSms {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties properties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.sms.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String,String> msg){
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        msg.remove("phone");
        smsUtils.sendSms(phone, JsonUtils.serialize(msg),properties.getSignName(),properties.getVerifyCodeTemplate());
    }
}
