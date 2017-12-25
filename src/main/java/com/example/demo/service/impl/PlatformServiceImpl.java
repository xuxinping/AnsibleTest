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
import java.util.Random;

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
            String pushssh = "ansible-playbook /develop/pushssh.ymal";
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

    @Override
    public void removeHost(String ip) {
        try {
            String[] cmd1 = new String[3];
            cmd1[0] = "/bin/sh";
            cmd1[1] = "-c";
            StringBuffer buf = new StringBuffer();
            buf.append("sed -i '/");
            buf.append(ip);
            buf.append("/d' /etc/ansible/hosts");
            cmd1[2] = buf.toString();
            Process ps1 = Runtime.getRuntime().exec(cmd1);
            ps1.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String genRandomPassword(int len) {
        Random rd = new Random();
        final int maxNum = 62;
        StringBuffer sb = new StringBuffer();
        int rdGet;//取得随机数
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A','B','C','D','E','F','G','H','I','J','K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        int count=0;
        while(count < len){
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count ++;
            }
        }
        sb.append('@');
        return sb.toString();
    }
}