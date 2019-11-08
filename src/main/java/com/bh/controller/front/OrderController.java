package com.bh.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.bh.common.ResponseCode;
import com.bh.common.ServerResponse;
import com.bh.pojo.User;
import com.bh.service.IOrderService;
import com.bh.utils.Const;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    IOrderService orderService;

    /*
    *
    * 创建订单接口
    * */
    @RequestMapping("{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId") Integer shippingId, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN, "未登录");
        }
        return orderService.createOrder(user.getId(),shippingId);
    }



    /*
    * 支付接口
    * */
    @RequestMapping("pay/{orderNo}")
    public ServerResponse pay(@PathVariable("orderNo") long orderNo,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN, "未登录");
        }
        return orderService.pay(user.getId(),orderNo);
    }



    /*
    *
    * 支付宝回调接口
    * */
    @RequestMapping("callback.do")
    public String alipay_callback(HttpServletRequest request){
        Map<String,String[]> callbackParams = request.getParameterMap();
        Map<String,String> signParams = Maps.newHashMap();
        Iterator<String> iterable = callbackParams.keySet().iterator();
        while (iterable.hasNext()){
            String key =  iterable.next();
            String[] values = callbackParams.get(key);
            StringBuffer stringBuffer = new StringBuffer();
            if (values!=null&&values.length>0){
                for (int i= 0; i < values.length; i++) {
                    stringBuffer.append(values[i]);
                    if (i!=values.length-1){
                        stringBuffer.append(",");
                    }

                }
            }
            signParams.put(key,stringBuffer.toString());
        }

        try {
            signParams.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(signParams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (result){
                //验签通过
                System.out.println("验签通过");
                return orderService.callback(signParams);
            }else {
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return "success";
    }



}
