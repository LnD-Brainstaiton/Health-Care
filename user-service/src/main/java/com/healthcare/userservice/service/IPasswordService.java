package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.request.ChangePasswordRequest;

public interface IPasswordService {

    Void changePassword(ChangePasswordRequest request);
}
