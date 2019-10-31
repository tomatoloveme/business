package com.bh.service;

import com.bh.common.ServerResponse;

public interface IOrderService {
    /*
    * 创建订单
    * */
    public ServerResponse createOrder(Integer userId,Integer shippingId);
}
