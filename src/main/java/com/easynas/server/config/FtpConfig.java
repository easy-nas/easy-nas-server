package com.easynas.server.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-1-21 下午7:58
 */
@Configuration
public class FtpConfig {
    FtpConfig() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();

        serverFactory.setFtplets(Map.of("default", new FtpService()));
        ListenerFactory factory = new ListenerFactory();
        //设置监听端口
        factory.setPort(2121);
        //替换默认监听
        serverFactory.addListener("default", factory.createListener());

        //用户名
        BaseUser user = new BaseUser();
        user.setName("admin");
        //密码 如果不设置密码就是匿名用户
        user.setPassword("123456");
        //用户主目录
        user.setHomeDirectory("/home/llysrv");

        List<Authority> authorities = new ArrayList<Authority>();
        //增加写权限
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        //增加该用户
        serverFactory.getUserManager().save(user);
        /**
         * 也可以使用配置文件来管理用户
         */
//      PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//      userManagerFactory.setFile(new File("users.properties"));
//      serverFactory.setUserManager(userManagerFactory.createUserManager());

        FtpServer server = serverFactory.createServer();
        server.start();
    }
}

class FtpService extends DefaultFtplet {
    /**
     * 上传结束 检查和转移文件，并创建同名软链接
     */
    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {

        String path = session.getUser().getHomeDirectory() + request.getArgument();
        System.out.println(path);
        String command = "mv " + path + " " + path + "123";
        System.out.println(command);
        Process exec = Runtime.getRuntime().exec(command);
        try {
            exec.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(request);

        return super.onUploadEnd(session, request);
    }

}