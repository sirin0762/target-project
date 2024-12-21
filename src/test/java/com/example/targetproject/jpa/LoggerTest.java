package com.example.targetproject.jpa;

import jakarta.transaction.Transactional;
import org.innercircle.opensource.inspector.JpaQueryInspector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class LoggerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void loggerTest() {
        applicationContext.getBean(JpaQueryInspector.class);
        UserEntity userEntity = new UserEntity("name", "address");
        userRepository.save(userEntity);

        List<UserEntity> all = userRepository.findAll();
        System.out.println(all.get(0));
    }

    @Test
    void queryCountTest() {
        TeamEntity team = new TeamEntity("team");
        TeamEntity savedTeam = teamRepository.save(team);
        UserEntity userEntity = new UserEntity("name", "address");

        savedTeam.addUser(userEntity);
        userRepository.save(userEntity);

        TeamEntity all = teamRepository.findAll().get(0);

        all.getUsers().forEach(u -> System.out.println(u.getName()));

    }

    @Test
    void querySynchronousTest() throws InterruptedException {
        UserEntity userEntity = new UserEntity("name", "address");
        UserEntity savedUser = userRepository.save(userEntity);

        int threadCount = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executor.submit(() -> {
                final Long id = savedUser.getId();
                final Long temp = (long) finalI;
                userRepository.findById(id + temp);
            });
        }

        // 스레드 종료 대기
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

    }

}