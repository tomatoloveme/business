package com.bh.service.impl;

import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.dao.ShippingMapper;
import com.bh.pojo.Shipping;
import com.bh.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.ObjectInputFilter;
import sun.plugin2.message.transport.SerializingTransport;

@Service("shippingServiceImpl")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping) {
        //判断参数是否为空
        if (shipping == null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空！");
        Integer shippingId = shipping.getId();
        if (shippingId == null){
            //添加收货地址
           int result =  shippingMapper.insert(shipping);
           if (result<0){
               return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加地址失败！");
           }
            else {
                return ServerResponse.serverResponseBySuccess(shipping.getId());
           }
        }else {
            //更新收货地址
        }
        return null;
    }

    @Override
    public ServerResponse findShippingById(Integer shippingId) {
        if (shippingId ==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"shippingId必须传递！");
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping ==null)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有找到这个收货地址！");
        return ServerResponse.serverResponseBySuccess(shipping);
    }
}
