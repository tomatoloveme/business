package com.bh.service.impl;

import com.bh.common.*;
import com.bh.dao.OrderItemMapper;
import com.bh.dao.OrderMapper;
import com.bh.pojo.*;
import com.bh.service.ICartService;
import com.bh.service.IOrderService;
import com.bh.service.IProductService;
import com.bh.service.IShippingService;
import com.bh.utils.BigDecimalUtils;
import com.bh.utils.Const;
import com.bh.utils.DateUtils;
import com.bh.vo.OrderItemVO;
import com.bh.vo.OrderVO;
import com.bh.vo.ShippingVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service("orderServiceImpl")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ICartService cartService;
    @Autowired
    IProductService productService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    IShippingService shippingService;

    @Value("${business.imageHost}")
    private String imageHost;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //1.参数的非空检验
            if (shippingId == null)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"收货地址不能为空！");

            //判断shippingId是否存在
        ServerResponse shippingIsExits = shippingService.findShippingById(shippingId);
        if (!shippingIsExits.isSuccess())
            return shippingIsExits;

        //2.根据userid查看用户购物车中查看购物车中已经选中的商品
           ServerResponse<List<Cart>> serverResponse = cartService.findCartsByUseridAndChecked(userId);
        //3.list<cart> ->list<orderItemVO>
            List<Cart> cartList = serverResponse.getData();
            if (cartList == null||cartList.size() ==0)
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车为空或者未选中任何商品！");
                //cart->orderItemVO
            ServerResponse serverResponse1 = getCartOrderItem(userId,cartList);
            ServerResponse orderItems_serverResponse = getCartOrderItem(userId,cartList);

            if (!orderItems_serverResponse.isSuccess()) {
                return orderItems_serverResponse;
            }
            List<OrderItem> orderItemList = (List<OrderItem>) orderItems_serverResponse.getData();
        //4.创建一个order实体类，并保存到DB
            ServerResponse<Order> order_serverResponse = createOrder(userId,shippingId,orderItemList);
        //5.保存订单明细list<OrderItemVO>
            if (!order_serverResponse.isSuccess())
                return order_serverResponse;
            Order order = order_serverResponse.getData();
            ServerResponse serverResponse2 = saveOrderItems(orderItemList,order);
            if (!serverResponse2.isSuccess())
                return serverResponse2;
        //6.扣库存
        ServerResponse serverResponse3 = reduceProductStock(orderItemList);
        if (!serverResponse3.isSuccess())
            return serverResponse3;
        //7.清空购物车中下单的商品
        ServerResponse cart_serverResponse = cartService.deleteBatch(cartList);
        if (!cart_serverResponse.isSuccess())
            return cart_serverResponse;
        //8.返回OrderVO

        return  assembleOrderVO(order,orderItemList,shippingId);
    }

    /*
    * 转化为orderVO
    *
    * */


    private ServerResponse  assembleOrderVO(Order order, List<OrderItem> orderItemList, Integer shippingId){
        OrderVO orderVO=new OrderVO();

        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO= assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(imageHost);
        ServerResponse<Shipping> serverResponse= shippingService.findShippingById(shippingId);
        if (!serverResponse.isSuccess())
            return serverResponse;
        Shipping shipping = serverResponse.getData();

        if(shipping!=null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO= assmbleShippingVO(shipping);
            orderVO.setShippingVo(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }

        orderVO.setStatus(order.getStatus());
        OrderStatusEnum orderStatusEnum= OrderStatusEnum.codeOf(order.getStatus());
        if(orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }

        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        PaymentEnum paymentEnum=PaymentEnum.codeOf(order.getPaymentType());
        if(paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());



        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    private ShippingVO assmbleShippingVO(Shipping shipping){
        ShippingVO shippingVO=new ShippingVO();

        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();

        if(orderItem!=null){

            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());

        }

        return orderItemVO;
    }




    /*
    * 扣库存的方法
    * */

    private ServerResponse reduceProductStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem:orderItemList){
            Integer productId = orderItem.getProductId();
            ServerResponse<Product> serverResponse = productService.findProductById(productId);
            Product product = serverResponse.getData();
            int stock = product.getStock() -orderItem.getQuantity();
            ServerResponse serverResponse1  = productService.reduceStock(productId,stock);
            if (!serverResponse1.isSuccess())
                return serverResponse1;

        }
        return ServerResponse.serverResponseBySuccess();
    }
    //保存订单明细list<OrderItemVO>
    private ServerResponse saveOrderItems(List<OrderItem> orderItemList,Order order){
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //这里是批量插入insert into（） values(),(),()...
       int result =  orderItemMapper.insertBatch(orderItemList);
        if (result!=orderItemList.size())
            //有些订单明细没有插入成功
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单明细保存失败！");
        return ServerResponse.serverResponseBySuccess();
    }


    /*
    * 创建order实体类
    *
    * */
    private ServerResponse createOrder(Integer userId,Integer shippingId,List<OrderItem> orderItems){
        Order order = new Order();

        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setOrderNo(generatorOrderNo());
        order.setPayment(getOrderTotalPrice(orderItems));
        order.setPaymentType(PaymentEnum.PAYMENT_ONLINE.getType());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.ORDER_NO_PAY.getStatus());//订单状态
        int result = orderMapper.insert(order);
        if (result<=0)
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"插入失败！");
        return ServerResponse.serverResponseBySuccess(order);
    }

    /*
    *生成订单号
    * */
    private Long generatorOrderNo(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    /*
    * 计算订单的总价格
    * */

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice = new BigDecimal("0");
        for (OrderItem orderItem:orderItems){
            orderTotalPrice = BigDecimalUtils.add(orderItem.getTotalPrice().doubleValue(),orderTotalPrice.doubleValue());
        }
        return orderTotalPrice;
    }

    private  ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){

        List<OrderItem> orderItemList= Lists.newArrayList();

        for(Cart cart:cartList){

            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            ServerResponse<Product> serverResponse=productService.findProductById(cart.getProductId());
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
