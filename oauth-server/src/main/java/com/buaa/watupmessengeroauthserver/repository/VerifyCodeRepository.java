package com.buaa.watupmessengeroauthserver.repository;

import com.buaa.watupmessengeroauthserver.model.Code;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifyCodeRepository extends MongoRepository<Code, String> {
    Code findByEmail(String email);
}
