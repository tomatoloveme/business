package com.bh.dao;

import com.bh.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    List<Order> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Order record);

    /*
    * 根据订单号查询订单
    * */
    Order findOrderByOrderNo(@Param("orderNo") Long orderNo);

    int updateOrderStatusAndPaymentTimeByOrderNo(@Param("order") Order order);

    int updateOrderStatusByOrderNo(@Param("order") Order order);


    /*
    *
    * 查询需要关闭的订单
    * */

    public List<Order> selectOrderByCreateTime(@Param("time") String time);

    /*
    * 关闭订单
    * */
    public Integer closeOrder(@Param("id") Integer id);


    /*
    * 查询支付状态
    *
    * */
    public Integer paymentStatus(@Param("orderNo") Long orderNo);


    public List<Order> getOrderListByStatus(@Param("status") Integer status,@Param("userid")Integer userid);
}