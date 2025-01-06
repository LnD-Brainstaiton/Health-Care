package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.presenter.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    IntegrationService integrationService;
}
