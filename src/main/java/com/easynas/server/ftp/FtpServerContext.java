package com.easynas.server.ftp;

import lombok.NonNull;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.ftpletcontainer.impl.DefaultFtpletContainer;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.DefaultFtpServerContext;
import org.apache.ftpserver.usermanager.impl.PropertiesUserManager;

import java.io.File;
import java.util.Map;

/**
 * @author liangyongrui
 */
public class FtpServerContext extends DefaultFtpServerContext {
    private UserManager userManager = new PropertiesUserManager(new EasyNasPasswordEncryptor(), (File) null, "admin");

    /**
     * Get user manager.
     */
    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    void setFtplets(@NonNull final Map<String, Ftplet> ftplets) {
        this.setFtpletContainer(new DefaultFtpletContainer(ftplets));
    }

    FtpServer getFtpServer() {
        return new DefaultFtpServer(this);
    }
}
