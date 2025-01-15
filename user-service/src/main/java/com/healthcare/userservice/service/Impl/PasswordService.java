package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.common.exceptions.InvalidPasswordException;
import com.healthcare.userservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.userservice.common.exceptions.RecordNotFoundException;
import com.healthcare.userservice.config.AuthConfig;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.ChangePasswordRequest;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.service.BaseService;
import com.healthcare.userservice.service.IPasswordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService extends BaseService implements IPasswordService {

    private final UserRepository userRepository;
    private final AuthConfig authConfig;

    @Override
    public Void changePassword(ChangePasswordRequest request) {
        String userId = getUserIdentity();

        validateChangePasswordRequest(request);

        Optional<User> userOpt = userRepository.findByUserId(userId);

        if(userOpt.isEmpty()){
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

        User user = userOpt.get();
        if(!authConfig.passwordEncoder().matches(request.getOldPassword(), user.getPassword())){
            throw new InvalidPasswordException(ResponseMessage.OLD_PASSWORD_NOT_VALID);
        }

        user.setPassword(authConfig.passwordEncoder().encode(request.getNewPassword()));

        userRepository.save(user);
        return null;
    }

    private void validateChangePasswordRequest(ChangePasswordRequest request) {
        if(Objects.isNull(request)  ||
            StringUtils.isEmpty(request.getOldPassword()) ||
            StringUtils.isEmpty(request.getNewPassword()) ||
            StringUtils.isEmpty(request.getConfirmPassword())) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordException(ResponseMessage.NEW_PASSWORD_MISMATCH);
        }
    }
}
