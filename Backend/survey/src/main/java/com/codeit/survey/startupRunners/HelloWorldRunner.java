package com.codeit.survey.startupRunners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldRunner implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(HelloWorldRunner.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("Hello folks !!!");
    }
}
