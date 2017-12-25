package com.example.demo.service.impl;

import com.example.demo.VO.ResultVO;
import com.example.demo.object.Userinfo;
import com.example.demo.service.MysqlService;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Zhang Yu on 2017/12/23.
 */
@Service
@Slf4j
public class MysqlServiceImpl implements MysqlService {

    @Override
    public ResultVO installMysql(String Ip) {
        try {
            String installmysql1 = "ansible-playbook /develop/installmysql.yml -l ";
            String installmysql = installmysql1.concat(Ip);
            Process ps2 = Runtime.getRuntime().exec(installmysql);
            ps2.waitFor();
            //日志记录
            CommonUtils.recordLog(ps2);
            log.info("-----------------------installmysql finished------------------------");

            String[] cmd1 = new String[3];
            cmd1[0] = "/bin/sh";
            cmd1[1] = "-c";
            StringBuffer buf = new StringBuffer();
            buf.append("ansible ");
            buf.append(Ip);
            buf.append(" -a \"grep 'temporary password' /var/log/mysqld.log\"");
            cmd1[2] = buf.toString();
            Process ps3 = Runtime.getRuntime().exec(cmd1);
            ps3.waitFor();
            //日志记录
            BufferedReader br3 = new BufferedReader(new InputStreamReader(ps3.getInputStream()));
            StringBuffer sb3 = new StringBuffer();
            String line3;
            while ((line3 = br3.readLine()) != null) {
                sb3.append(line3).append("\n");
            }
            String result3 = sb3.toString();
            String usrroot = result3.substring(result3.length() - 29, result3.length() - 25);
            String tempass = result3.substring(result3.length() - 13, result3.length() - 1);
            br3.close();
            if (usrroot.equals("root")) {
                log.info(result3);
                log.info("--------------------getpass finished--------------------------");
                String newpassword = CommonUtils.genRandomPassword(15);
                String[] cmd2 = new String[3];
                cmd2[0] = "/bin/sh";
                cmd2[1] = "-c";
                StringBuffer buf1 = new StringBuffer();
                buf1.append("ansible-playbook /develop/createuser.yml -l ");
                buf1.append(Ip);
                buf1.append(" -e \"password=");
                buf1.append(tempass);
                buf1.append(" newpassword=");
                buf1.append(newpassword);
                buf1.append("\"");
                cmd2[2] = buf1.toString();
                Process ps4 = Runtime.getRuntime().exec(cmd2);
                ps4.waitFor();
                log.info(cmd2[2]);
                log.info(newpassword);
                Userinfo userinfo = new Userinfo();
                userinfo.setUsername("root");
                userinfo.setPassword("MCloud2017@");
                userinfo.setNewusername("chinaunicom");
                userinfo.setNewpassword(newpassword);
                CommonUtils.removeHost(Ip);
                return ResultVOUtil.success(userinfo);
            } else return ResultVOUtil.error(500, "出错啦！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }

    @Override
    public ResultVO installMysqlMasterAndSlave(String Ip1, String Ip2) {
        try {
            StringBuffer buf = new StringBuffer();
            buf.append("ansible-playbook /develop/installmysql.yml -l ");
            buf.append(Ip1);
            buf.append(",");
            buf.append(Ip2);
            String install = buf.toString();
            Process ps = Runtime.getRuntime().exec(install);
            ps.waitFor();
            //日志记录
            CommonUtils.recordLog(ps);
            log.info("-----------------------installmysql finished------------------------");

            String[] cmd1 = new String[3];
            cmd1[0] = "/bin/sh";
            cmd1[1] = "-c";
            StringBuffer buf1 = new StringBuffer();
            buf1.append("ansible ");
            buf1.append(Ip1);
            buf1.append(" -a \"grep 'temporary password' /var/log/mysqld.log\"");
            cmd1[2] = buf1.toString();
            Process ps1 = Runtime.getRuntime().exec(cmd1);
            ps1.waitFor();
            //日志记录
            BufferedReader br1 = new BufferedReader(new InputStreamReader(ps1.getInputStream()));
            StringBuffer sb1 = new StringBuffer();
            String line1;
            while ((line1 = br1.readLine()) != null) {
                sb1.append(line1).append("\n");
            }
            String result1 = sb1.toString();
            String usrroot1 = result1.substring(result1.length() - 29, result1.length() - 25);
            String tempass1 = result1.substring(result1.length() - 13, result1.length() - 1);
            br1.close();

            String[] cmd2 = new String[3];
            cmd2[0] = "/bin/sh";
            cmd2[1] = "-c";
            StringBuffer buf2 = new StringBuffer();
            buf.append("ansible ");
            buf.append(Ip2);
            buf.append(" -a \"grep 'temporary password' /var/log/mysqld.log\"");
            cmd2[2] = buf2.toString();
            Process ps2 = Runtime.getRuntime().exec(cmd2);
            ps2.waitFor();
            //日志记录
            BufferedReader br2 = new BufferedReader(new InputStreamReader(ps2.getInputStream()));
            StringBuffer sb2 = new StringBuffer();
            String line2;
            while ((line2 = br2.readLine()) != null) {
                sb2.append(line2).append("\n");
            }
            String result2 = sb2.toString();
            String usrroot2 = result2.substring(result2.length() - 29, result2.length() - 25);
            String tempass2 = result2.substring(result2.length() - 13, result2.length() - 1);
            br2.close();

            if (usrroot1.equals("root") && usrroot2.equals("root")) {
                log.info(result1);
                log.info(result2);
                log.info("--------------------getpass finished--------------------------");

                //TODO

            } else return ResultVOUtil.error(500, "出错啦！");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
        return null;
    }
}
