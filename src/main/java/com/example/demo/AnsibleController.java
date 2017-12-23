package com.example.demo;

import com.example.demo.VO.ResultVO;
import com.example.demo.object.Userinfo;
import com.example.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Zhang Yu on 2017/12/21.
 */
@RestController
@Slf4j
public class AnsibleController {

    /**
     * 推送公钥
     * @return
     */
    @RequestMapping(value = "/pushrsa", method = RequestMethod.GET)
    public ResultVO pushrsa() {
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


    @RequestMapping(value = "/installmysql", method = RequestMethod.POST)
    public ResultVO installmysql(@RequestParam("ip") String Ip) {
        try {
            String installmysql1 = "ansible-playbook /develop/installmysql.ymal -l ";
            String installmysql = installmysql1.concat(Ip);
            Process ps2 = Runtime.getRuntime().exec(installmysql);
            ps2.waitFor();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(ps2.getInputStream()));
            StringBuffer sb2 = new StringBuffer();
            String line2;
            while ((line2 = br2.readLine()) != null) {
                sb2.append(line2).append("\n");
            }
            String result2 = sb2.toString();
            log.info(result2);
            log.info("-----------------------installmysql finished------------------------");
            br2.close();

            String[] cmd1 = new String[5];
            cmd1[0] = "/bin/sh";
            cmd1[1] = "-c";
            StringBuffer buf=new StringBuffer();
            buf.append("ansible ");
            buf.append(Ip);
            buf.append(" -a \"grep 'temporary password' /var/log/mysqld.log\"");
            cmd1[2] = buf.toString();
            System.out.println(cmd1[2]);
            Process ps3 = Runtime.getRuntime().exec(cmd1);
            ps3.waitFor();
            BufferedReader br3 = new BufferedReader(new InputStreamReader(ps3.getInputStream()));
            StringBuffer sb3 = new StringBuffer();
            String line3;
            while ((line3 = br3.readLine()) != null) {
                sb3.append(line3).append("\n");
            }
            String result3 = sb3.toString();
            String usrroot = result3.substring(result3.length() - 29, result3.length() - 25);
            String tempass = result3.substring(result3.length() - 13, result3.length() - 1);
            Userinfo userinfo = new Userinfo();
            userinfo.setUsername(usrroot);
            userinfo.setPassword(tempass);
            log.info(result3);
            log.info("getpass finished----------------------------");
            buf.close();
            br3.close();

            return ResultVOUtil.success(userinfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(500, "出错啦！");
        }
    }
}
