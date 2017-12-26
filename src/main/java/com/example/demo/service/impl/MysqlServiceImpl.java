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

    /**
     * mysql单例安装
     * @param Ip
     * @return
     */
    @Override
    public ResultVO installMysql(String Ip) {
        try {
            //执行类似：ansible-playbook /develop/installmysql.yml -l 192.168.91.131
            String installmysql1 = "ansible-playbook /develop/installmysql.yml -l ";
            String installmysql = installmysql1.concat(Ip);
            Process ps2 = Runtime.getRuntime().exec(installmysql);
            ps2.waitFor();
            //日志记录
            CommonUtils.recordLog(ps2);
            log.info("-----------------------installmysql finished------------------------");

            //调用的shell命令中若包含单双引号，则必须使用/bin/sh -c,原因还未知。。。
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
            //截取结果的相应位置字符串，若执行正确，则返回root与root密码
            String usrroot = result3.substring(result3.length() - 29, result3.length() - 25);
            String tempass = result3.substring(result3.length() - 13, result3.length() - 1);
            br3.close();
            //判断是否执行正确
            if (usrroot.equals("root")) {
                log.info(result3);
                log.info("--------------------getpass finished--------------------------");
                String newpassword = CommonUtils.genRandomPassword(15);
                String[] cmd2 = new String[3];
                //执行类似：ansible-playbook /develop/createmaster.yml -l 192.168.91.131 -e
                // "password=上面所得密码 newpassword=随机生成的新密码"
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
                //输出相应结果
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

    /**
     * 安装mysql的主从结构
     * @param Ip1
     * @param Ip2
     * @return
     */
    @Override
    public ResultVO installMysqlMasterAndSlave(String Ip1, String Ip2) {
        try {
            //执行ansible-playbook /develop/installmysql.yml -l 192.168.91.131,192.168.91.132
            //为主从节点安装mysql
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


            //执行ansible 192.168.91.131 -a "grep 'temporary password' /var/log/mysqld.log"
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

            //执行ansible 192.168.91.132 -a "grep 'temporary password' /var/log/mysqld.log"
            String[] cmd2 = new String[3];
            cmd2[0] = "/bin/sh";
            cmd2[1] = "-c";
            StringBuffer buf2 = new StringBuffer();
            buf2.append("ansible ");
            buf2.append(Ip2);
            buf2.append(" -a \"grep 'temporary password' /var/log/mysqld.log\"");
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

                //执行ansible-playbook /develop/createmaster.yml -l 192.168.91.131 -e
                // "password=q*3e=j&o=plH newpassword=MCloud2017@"
                //配置主节点
                String newpasswordmaster = CommonUtils.genRandomPassword(15);
                String[] cmd3 = new String[3];
                cmd3[0] = "/bin/sh";
                cmd3[1] = "-c";
                StringBuffer buf3 = new StringBuffer();
                buf3.append("ansible-playbook /develop/createmaster.yml -l ");
                buf3.append(Ip1);
                buf3.append(" -e \"password=");
                buf3.append(tempass1);
                buf3.append(" newpassword=");
                buf3.append(newpasswordmaster);
                buf3.append("\"");
                cmd3[2] = buf3.toString();
                Process ps3 = Runtime.getRuntime().exec(cmd3);
                ps3.waitFor();
                //日志记录
                CommonUtils.recordLog(ps3);
                log.info("--------------------createmaster finished--------------------------");

                //执行ansible 192.168.91.131 -a "mysql -uroot -pMCloud2017@ -e 'SHOW MASTER STATUS'"
                //收集主节点相应信息 file 与 position 信息
                String[] cmd4 = new String[3];
                cmd4[0] = "/bin/sh";
                cmd4[1] = "-c";
                StringBuffer buf4 = new StringBuffer();
                buf4.append("ansible ");
                buf4.append(Ip1);
                buf4.append(" -a \"mysql -uroot -pMCloud2017@ -e 'SHOW MASTER STATUS'\"");
                cmd4[2] = buf4.toString();
                Process ps4 = Runtime.getRuntime().exec(cmd4);
                ps4.waitFor();
                //日志记录
                BufferedReader br4 = new BufferedReader(new InputStreamReader(ps4.getInputStream()));
                String linetemp;
                String line4 = null;
                while ((linetemp = br4.readLine()) != null) {
                    line4 = linetemp;
                }
                log.info("--------------------SHOW MASTER STATUS--------------------------");
                log.info(line4);
                String file = line4.substring(0,16);
                String position = line4.substring(16,24);
                file = file.trim();
                position = position.trim();
                log.info(file);
                log.info(position);
                br4.close();
                log.info("--------------------get file and position finished--------------------------");

                //执行类似：ansible-playbook /develop/createslave.yml -l 192.168.91.132 -e "password=gqGSekYOh1/i
                // newpassword=MCloud2017@ mysql_repl_master='192.168.91.131' File='mysql-bin.000001' Position=1067"
                //为从节点配置信息
                String newpasswordslave = CommonUtils.genRandomPassword(15);
                String[] cmd5 = new String[3];
                cmd5[0] = "/bin/sh";
                cmd5[1] = "-c";
                StringBuffer buf5 = new StringBuffer();
                buf5.append("ansible-playbook /develop/createslave.yml -l ");
                buf5.append(Ip2);
                buf5.append(" -e \"password=");
                buf5.append(tempass2);
                buf5.append(" newpassword=");
                buf5.append(newpasswordslave);
                buf5.append(" mysql_repl_master='");
                buf5.append(Ip1);
                buf5.append("' File='");
                buf5.append(file);
                buf5.append("' Position=");
                buf5.append(position);
                buf5.append("\"");
                cmd5[2] = buf5.toString();
                log.info(cmd5[2]);
                Process ps5 = Runtime.getRuntime().exec(cmd5);
                ps5.waitFor();
                //日志记录
                CommonUtils.recordLog(ps5);
                log.info("--------------------createslave finished--------------------------");

                //结果返回
                Userinfo userinfo = new Userinfo();
                userinfo.setUsername("chinaunicom");
                userinfo.setPassword(newpasswordmaster);
                userinfo.setNewusername("chinaunicom");
                userinfo.setNewpassword(newpasswordslave);
//                CommonUtils.removeHost(Ip1);
//                CommonUtils.removeHost(Ip2);
                return ResultVOUtil.success(userinfo);

            } else return ResultVOUtil.error(500, "出错啦！");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }
}
