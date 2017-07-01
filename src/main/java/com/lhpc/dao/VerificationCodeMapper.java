package com.lhpc.dao;

import com.lhpc.model.VerificationCode;

public interface VerificationCodeMapper {
    int deleteByMobile(VerificationCode verificationCode);

    int insert(VerificationCode verificationCode);

    VerificationCode selectByMobile(VerificationCode verificationCode);
}