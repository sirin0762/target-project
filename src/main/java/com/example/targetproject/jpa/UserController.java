package com.example.targetproject.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeanFactory beanFactory;

    @GetMapping("/ping")
    public String ping() {
        log.info("Bean Detected: {}", beanFactory.getBean(OncePerRequestFilter.class));
        return "pong";
    }

    @GetMapping("/{id}")
    public String getUserName(@PathVariable(name = "id") Long id) {
        log.info("Bean Detected: {}", beanFactory.getBean(OncePerRequestFilter.class));
        return userRepository.findById(id).get().getName();
    }

    @PostMapping
    public void addUser(
        @RequestParam String name,
        @RequestParam String address
    ) {
       log.info("Bean Detected: {}", beanFactory.getBean(OncePerRequestFilter.class));
       userRepository.save(new UserEntity(name, address));
    }

}
