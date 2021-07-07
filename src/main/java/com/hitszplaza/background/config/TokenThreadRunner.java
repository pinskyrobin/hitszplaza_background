package com.hitszplaza.background.config;

import com.hitszplaza.background.service.TokenThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TokenThreadRunner implements CommandLineRunner {

    @Autowired
    private TokenThread tokenThread;

    @Override
    public void run(String... args) {
        new Thread(tokenThread).start();
    }
}
