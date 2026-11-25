package com.udea.tecnica.authentication_backend.service;

import java.util.List;
import com.udea.tecnica.authentication_backend.dto.UserResponse;

public interface UserService {
    UserResponse findById(Long id);

    List<UserResponse> findAll();
}
