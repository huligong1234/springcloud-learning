package org.jeedevframework.springcloud.common;

import lombok.Data;

@Data
public class R<T>  {
    /**
     *给前台返回的数据要求根据公司或者自己的要求而定
     *提供5种成功或失败的返回方法，灵活性比较高
     */

    //返回的状态码
    private Integer code;

    //返回的状态消息
    private String message;

    //返回的具体数据
    private T data;

    //把构造方法私有
    public R() {}

    /**
     *访问成功，没有数据返回，只返回访问是否成功的状态
     */
    public static R ok() {
        R r = new R();
        r.setCode(200);
        r.setMessage("succeed");
        return r;
    }
    /**
     *自定义返回成功的所有数据
     */
    public static<T> R ok(Integer code, String message, T data){
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
    /**
     *只返回自定义状态消息
     */
    public static R ok(String message){
        R r = new R();
        r.setMessage(message);
        return r;
    }
    /**
     *状态码和返回状态固定写法，自定义data数据
     */
    public static<T> R ok(T data){
        R r = new R();
        r.setCode(200);
        r.setMessage("succeed");
        r.setData(data);
        return r;
    }
    /**
     *自定义返回状态码和返回访问状态，
     */
    public static R ok(Integer code,String message){
        R r = new R();
        r.setMessage(message);
        r.setCode(code);
        return r;
    }


    /**
     *访问失败，同理成功的方法
     */
    public static R error() {
        R r = new R();
        r.setCode(0);
        r.setMessage("failure");
        return r;
    }

    public static<T> R error(Integer code, String message, T data){
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R error(String message){
        R r = new R();
        r.setMessage(message);
        return r;
    }
    public static<T> R error(T data){
        R r = new R();
        r.setCode(500);
        r.setMessage("failure");
        r.setData(data);
        return r;
    }

    public static R error(Integer code,String message){
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void put(String code, int code1) {

    }
}