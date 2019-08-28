package com.leyoumall.mq;

import com.leyoumall.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ItemListener
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@Component
public class ItemListener {

    @Autowired
    private PageService pageService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.item.insert.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenInsertOrUpdate(Long spuid){
        if (spuid == null) {
            return;
        }
        //处理消息
        pageService.createHtml(spuid);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page.item.delete.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void listenDelete(Long spuid){
        if (spuid == null) {
            return;
        }
        //处理消息
        pageService.deleteHtml(spuid);
    }
}
