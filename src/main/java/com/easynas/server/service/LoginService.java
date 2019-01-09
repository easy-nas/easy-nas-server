package com.easynas.server.service;

import com.easynas.server.model.User;
import com.easynas.server.model.request.LoginRequest;
import org.springframework.stereotype.Service;

/**
 * @author liangyongrui
 */
@Service
public interface LoginService {

    User getUser(LoginRequest loginRequest);
}
