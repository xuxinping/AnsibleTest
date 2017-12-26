package com.example.demo;

import com.example.demo.VO.ResultVO;
import com.example.demo.service.MysqlService;
import com.example.demo.service.PlatformService;
import com.example.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhang Yu on 2017/12/21.
 */
@RestController
@Slf4j
public class AnsibleController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private MysqlService mysqlService;

    /**
     * 得到主机列表
     *
     * @return
     */
    @RequestMapping(value = "/get_host_list", method = RequestMethod.GET)
    public ResultVO getHostList() {
        try {
            List<String> hostList = platformService.getHostList();
            StringBuffer sb = new StringBuffer();
            sb.append(hostList.get(0));
            for (int i = 1; i < hostList.size(); i++) {
                sb.append("  ||  ").append(hostList.get(i));
            }
            String result = sb.toString();
            log.info(result);
            log.info("-----------------gethost finished----------------------");
            return ResultVOUtil.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }

    }

    /**
     * 推送公钥
     *
     * @return
     */
    @RequestMapping(value = "/push_rsa", method = RequestMethod.GET)
    public ResultVO pushRsa() {
        return platformService.pushRsa();
    }

    /**
     * 安装mysql 并返回账号密码
     *
     * @param Ip
     * @return
     */
    @RequestMapping(value = "/install_mysql", method = RequestMethod.POST)
    public ResultVO installMysql(@RequestParam("ip") String Ip) {
        try {
            List<String> hostList = platformService.getHostList();
            if (hostList.contains(Ip)) {
                return mysqlService.installMysql(Ip);
            }
            else return ResultVOUtil.error(500, "IP不在列表中");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }

    /**
     * 安装mysql 并返回账号密码
     *
     * @param Ip1 Ip2
     * @return
     */
    @RequestMapping(value = "/install_mysql_masterandslave", method = RequestMethod.POST)
    public ResultVO installMysql(@RequestParam("ip1") String Ip1, @RequestParam("ip2") String Ip2) {
        try {
            List<String> hostList = platformService.getHostList();
            if (hostList.contains(Ip1) && hostList.contains(Ip2)) {
                return mysqlService.installMysqlMasterAndSlave(Ip1, Ip2);
            }
            else return ResultVOUtil.error(500, "IP不在列表中");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }
}
