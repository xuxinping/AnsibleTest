package com.example.demo.utils;

import com.example.demo.VO.ResultVO;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
public class ResultVOUtil {

    /**
     * 返回成功，并携带相应数据
     * @param o
     * @return
     */
    public static ResultVO success(Object o){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(o);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    /**
     * 返回成功，携带数据为空
     * @return
     */
    public static ResultVO success(){
        return success(null);
    }

    /**
     * 构造函数
     * @param code
     * @param msg
     * @return
     */
    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }
}
