package com.healthcare.tfaservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "APPLICATION_SETTING")
public class ApplicationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SETTING_NAME")
    private String settingName;

    @Column(name = "SETTING_CODE")
    private String settingCode;

    @Column(name = "SETTING_VALUE")
    private String settingValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted = Boolean.FALSE;

}
