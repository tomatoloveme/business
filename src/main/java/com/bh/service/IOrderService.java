package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    /*
    * 创建订单
    * */
    public ServerResponse createOrder(Integer userId,Integer shippingId);

    /*
    *
    * 支付接口
    * */
    public ServerResponse pay(Integer userId,Long orderNo);

    /*
    * 支付回调接口
    *
    * */

    public String callback(Map<String,String> requestParams);

    /*
    *
    * 发货
    * */
    public ServerResponse sendGoods(Long orderNo);

    /*
    *
    * 查询需要关闭的订单
    *
    *
    * */
    public List<Order> closeOrder(String closeOrderDate);

    /*
     * 根据订单号查询订单支付状态
     *
     * */

    public ServerResponse paymentStatus(Long orderNo);

    /*
    * 根据状态查询订单
    * */
    public ServerResponse getOrderListByStatus( Integer status,Integer userid);

    /*
    * 根据订单号查询订单明细
    * */
    public ServerResponse getOrderItemByOrderId( Long orderId);
}
