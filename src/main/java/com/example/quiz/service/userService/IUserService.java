package com.example.quiz.service.userService;

import com.example.quiz.dto.repoMaps.LoginRequestListModel;
import com.example.quiz.dto.request.JUserCreateRequestModel;
import com.example.quiz.dto.request.JUserLoginModel;
import com.example.quiz.utils.ServiceResponse;

public interface IUserService {
    ServiceResponse registerUser(JUserCreateRequestModel userCreateRequestModel);

    ServiceResponse loginUser (JUserLoginModel userLoginModel);
}
