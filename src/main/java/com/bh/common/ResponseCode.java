package com.bh.common;
/*
 * 维护状态码
 *
 * */

public class ResponseCode {

    /*
        成功的状态码
     */
    public static final int SUCCESS = 0;

    /*
    失败的状态码
     */
    public static final int ERROR = 100;

    /*
    参数不为空
     */
    public static final int PARAM_NOT_NULL = 1;

    /*
    用户名已经存在
     */
    public static final int USERNAME_EXISTS = 2;

    /*
   邮箱已经存在
    */
    public static final int EMAIL_EXISTS = 3;
    /*
    未登陆
    */
    public static final int NOT_LOGIN = 99;



}
