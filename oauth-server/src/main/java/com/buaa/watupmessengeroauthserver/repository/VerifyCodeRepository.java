package com.buaa.watupmessengeroauthserver.repository;

import com.buaa.watupmessengeroauthserver.model.VerifyCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifyCodeRepository extends MongoRepository<VerifyCode, String> {
    VerifyCode findByEmail(String email);
}
