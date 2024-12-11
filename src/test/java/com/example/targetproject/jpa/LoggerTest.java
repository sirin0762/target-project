package com.example.targetproject.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LoggerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void loggerTest() {
        UserEntity userEntity = new UserEntity("name", "address");
        userRepository.save(userEntity);

        List<UserEntity> all = userRepository.findAll();
        System.out.println(all.get(0));
    }

}