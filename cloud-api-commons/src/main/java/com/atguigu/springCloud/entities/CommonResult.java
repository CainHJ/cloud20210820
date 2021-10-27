package com.atguigu.springCloud.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/28 16:22
 */
@Data
@AllArgsConstructor//这个注解是有参构造
@NoArgsConstructor //这个注解是无参构造
//1
public class CommonResult<T> {
    private Integer code;
    private String  message;
    private T       data;

    public CommonResult(Integer code, String message){
        this(code,message,null);
    }
}
