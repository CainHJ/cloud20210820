package com.atguigu.springCloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author HJ
 * @version 1.0
 * @date 2021/8/28 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 这个有什么用
 */
//1
public class Payment implements Serializable {
    private Long id;
    private String serial;
}
