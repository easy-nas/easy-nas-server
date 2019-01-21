package com.easynas.server.ftp;

import com.easynas.server.util.HashUtils;
import org.apache.ftpserver.usermanager.PasswordEncryptor;

/**
 * @author liangyongrui
 */
public class EasyNasPasswordEncryptor implements PasswordEncryptor {

    @Override
    public String encrypt(String password) {
        return password;
    }

    @Override
    public boolean matches(String passwordToCheck, String storedPassword) {
        return HashUtils.hash(passwordToCheck).equals(storedPassword);
    }
}
