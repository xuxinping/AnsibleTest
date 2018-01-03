package com.example.demo.service.impl;

import com.example.demo.VO.ResultVO;
import com.example.demo.service.RedisService;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Zhang Yu on 2018/1/3.
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Override
    public ResultVO installRedis(String Ip) {
        try {
            //执行类似：ansible-playbook /develop/redisInstall.yml -l 192.168.91.131
            String installredis1 = "ansible-playbook /develop/redisInstall.yml -l ";
            String installredis = installredis1.concat(Ip);
            Process ps = Runtime.getRuntime().exec(installredis);
            ps.waitFor();
            //日志记录
            CommonUtils.recordLog(ps);
            log.info("-----------------------installredis finished------------------------");
            return ResultVOUtil.success(Ip);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }
}
