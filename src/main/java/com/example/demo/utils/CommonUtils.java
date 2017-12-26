package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Zhang Yu on 2017/12/25.
 */
@Slf4j
public class CommonUtils {

    /**
     * 从/etc/ansible/hosts把已用的IP删除
     * @param ip
     */
    public static void removeHost(String ip) {
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

    /**
     * 生成随机字符串，作为密码
     * @param len
     * @return
     */
    public static String genRandomPassword(int len) {
        Random rd = new Random();
        final int maxNum = 62;
        StringBuffer sb = new StringBuffer();
        int rdGet;//取得随机数
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int count = 0;
        while (count < len) {
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count++;
            }
        }
        sb.append('@');
        return sb.toString();
    }

    /**
     * 返回当前线程的输出内容到log
     * @param ps
     */
    public static void recordLog(Process ps) {
        try {
            BufferedReader brr = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sbr = new StringBuffer();
            String liner;
            while ((liner = brr.readLine()) != null) {
                sbr.append(liner).append("\n");
            }
            brr.close();
            String resultr = sbr.toString();
            log.info(resultr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
