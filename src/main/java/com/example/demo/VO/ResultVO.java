package com.example.demo.VO;

import lombok.Data;


/**
 * http请求返回的最外层对象
 * Created by Zhang Yu on 2017/10/19.
 */
@Data
public class ResultVO<T> {

    /** 错误码 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /** 返回的具体内容 */
    private T data;
}
