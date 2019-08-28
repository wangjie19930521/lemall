package com.leyoumall.service.impl;

import com.leyoumall.client.GoodsClient;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.common.utils.IdWorker;
import com.leyoumall.dto.CartDto;
import com.leyoumall.dto.OrderDto;
import com.leyoumall.entity.UserInfo;
import com.leyoumall.enums.OrderStatusEnum;
import com.leyoumall.interceptor.UserInterceptor;
import com.leyoumall.item.pojo.Sku;
import com.leyoumall.mapper.OrderDetailMapper;
import com.leyoumall.mapper.OrderMapper;
import com.leyoumall.mapper.OrderStatusMapper;
import com.leyoumall.pojo.Order;
import com.leyoumall.pojo.OrderDetail;
import com.leyoumall.pojo.OrderStatus;
import com.leyoumall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName OrderServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsClient goodsClient;

    @Override
    @Transactional
    public Long createOrder(OrderDto orderDto) {
        Order order = new Order();
        //1.新增订单
        // 1.1  订单编号 基本信息 雪花算法生成分布式自增长ID
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPayType());
        //1.2 用户信息
        UserInfo user = UserInterceptor.getLoginUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        //1.3  收货人地址    根据前台传入的addressId  去数据库查询收货地址信息，设置到  order对象
        @NotNull Long addressId = orderDto.getAddressId();
        //order.setReceiver();
        //1.4  金额
        //将CartDto集合转为map  key 为 skuid  value 为数量
        Map<Long, Integer> map = orderDto.getCarts().stream()
                .collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        /*List<Long> idList = orderDto.getCarts().stream().map(CartDto::getSkuId).collect(Collectors.toList());*/
        //set 转 list
        List<Long> idList = new ArrayList<>(map.keySet());
        List<Sku> skus = goodsClient.querySkuByIds(idList);
        //准备orderdetail 集合
        List<OrderDetail> orderDetails = new ArrayList<>();
        //获取总金额
        long totalPay = 0L;
        for (Sku sku : skus) {
            totalPay += sku.getPrice() * map.get(sku.getId());
            //封装orderDetails
            OrderDetail orderDetail = new OrderDetail();
            //取第一个 ， 之前的的
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setNum(map.get(sku.getId()));
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetails.add(orderDetail);
        }
        order.setTotalPay(totalPay);
        //实际支付金额   0  应该是去查询优惠活动计算优惠金额
        order.setActualPay(totalPay - 0);
        //写入数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            log.error("创建订单失败，订单ID:{}",orderId);
            throw  new LyException(ExceptionEnum.ORDER_CREATE_FAIL);
        }
        //2.  新增订单详情  //组织订单数据
        count = orderDetailMapper.insertList(orderDetails);
        if (count != orderDetails.size()) {
            log.error("创建订单详情失败，订单ID:{}",orderId);
            throw  new LyException(ExceptionEnum.ORDER_CREATE_FAIL);
        }
        //3. 新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = orderStatusMapper.insertSelective(orderStatus);
        if (count != 1) {
            log.error("创建订单状态失败，订单ID:{}", orderId);
            throw new LyException(ExceptionEnum.ORDER_CREATE_FAIL);
        }
        // todo 减少库存.此处才用分布式锁效率低，  其他要等待完成后才去操作库存  （调用goodsClient 操作）
         // 才用的此方式解决超卖，   update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock > 0
        //根据返回的count  p判断是否update成功   失败返回  throw new LyException(ExceptionEnum.‘库存不够’);
        return orderId;
    }
}
