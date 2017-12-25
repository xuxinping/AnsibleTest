package com.example.demo.service;

import com.example.demo.VO.ResultVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhang Yu on 2017/12/23.
 */
public interface PlatformService {

    List<String> getHostList() throws Exception;

    ResultVO pushRsa();

}
