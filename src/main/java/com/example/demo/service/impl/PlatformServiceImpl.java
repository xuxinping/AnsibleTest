package com.example.demo.service.impl;

import com.example.demo.VO.ResultVO;
import com.example.demo.service.PlatformService;
import com.example.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhang Yu on 2017/12/23.
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {
    @Override
    public List<String> getHostList() throws Exception {
        try {
            String pushssh = "cat /etc/ansible/hosts";
            Process ps = Runtime.getRuntime().exec(pushssh);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            List<String> List = new ArrayList();
            String line;
            int a = 0;
            while ((line = br.readLine()) != null) {
                /* 排除第一行 */
                if (a != 0) {
                    String[] temp = line.split(" ");
                    List.add(temp[0]);
                }
                a++;
            }
            br.close();
            return List;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResultVO pushRsa() {
        try {
            String pushssh = "ansible-playbook /develop/pushssh.yml";
            Process ps1 = Runtime.getRuntime().exec(pushssh);
            ps1.waitFor();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(ps1.getInputStream()));
            StringBuffer sb1 = new StringBuffer();
            String line1;
            while ((line1 = br1.readLine()) != null) {
                sb1.append(line1).append("\n");
            }
            String result1 = sb1.toString();
            log.info(result1);
            log.info("-----------------pushssh finished----------------------");
            br1.close();
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }

}
