package com.example.demo.service;

import com.example.demo.VO.ResultVO;

/**
 * Created by Zhang Yu on 2017/12/23.
 */
public interface MysqlService {

    ResultVO installMysql(String Ip);

    ResultVO installMysqlMasterAndSlave(String Ip1, String Ip2);
}
