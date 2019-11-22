package com.bh.service;

import com.bh.common.ServerResponse;
import com.bh.pojo.Shipping;

public interface IShippingService {
    public ServerResponse add(Shipping shipping);

    public ServerResponse findShippingById(Integer shippingId);
/*
* 查看所有收获地址
* */
    public ServerResponse listAll();
}
