package com.bh.service.impl;

import com.bh.common.ProductStatusEnum;
import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.Cart;
import com.bh.pojo.OrderItem;
import com.bh.pojo.Product;
import com.bh.service.ICartService;
import com.bh.service.IOrderService;
import com.bh.service.IProductService;
import com.bh.utils.BigDecimalUtils;
import com.bh.vo.OrderItemVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("orderServiceImpl")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ICartService cartService;
    @Autowired
    IProductService productService;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //1.参数的非空检验
            if (shippingId == null)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"收货地址不能为空！");
        //2.根据userid查看用户购物车中查看购物车中已经选中的商品
           ServerResponse<List<Cart>> serverResponse = cartService.findCartsByUseridAndChecked(userId);
        //3.list<cart> ->list<orderItemVO>
            List<Cart> cartList = serverResponse.getData();
            if (cartList == null||cartList.size() ==0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车为空或者为选中任何商品！");
            List<OrderItemVO> orderItemVOList = Lists.newArrayList();
            for(Cart cart:cartList){
                //cart->orderItemVO

            }
        //4.创建一个order实体类，并保存到DB

        //5.保存订单明细list<OrderItemVO>

        //6.扣库存

        //7.清空购物车中下单的商品

        //8.返回OrderVO
        return null;
    }

    private  ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){

        List<OrderItem> orderItemList= Lists.newArrayList();

        for(Cart cart:cartList){

            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            ServerResponse<Product> serverResponse=productService.detail(cart.getProductId());
            if (!serverResponse.isSuccess())
                return serverResponse;
            Product product = serverResponse.getData();
            if(product==null){
                return  ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品不存在");
            }
            if(product.getStatus()!= ProductStatusEnum.PRODUCT_SALE.getStatus()){//商品下架
                return ServerResponse.serverResponseByError("id为"+product.getId()+"的商品已经下架");
            }
            if(product.getStock()<cart.getQuantity()){//库存不足
                return ServerResponse.serverResponseByError("id为"+product.getId()+"的商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }

        return  ServerResponse.serverResponseBySuccess(orderItemList);
    }
}
