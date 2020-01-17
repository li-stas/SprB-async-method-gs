package com.example.gsasyncmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@SpringBootApplication
/*
@Configuration
@EnableAutoConfiguration
@ComponentScan
*/
@EnableAsync
public class GsAsyncMethodApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GsAsyncMethodApplication.class);
    @Autowired
    GitHubLookupService gitHubLookupService;


    public static void main(String[] args) {
        SpringApplication.run(GsAsyncMethodApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        Future<User> page1 = gitHubLookupService.findPage("PivotalSoftware");
        Future<User> page2 = gitHubLookupService.findPage("CloudFoundry");
        Future<User> page3 = gitHubLookupService.findPage("Spring-Projects");

        // Wait until they are all done
        while (!(page1.isDone() && page2.isDone() && page3.isDone())) {
            Thread.sleep(10); //millisecond pause between each check
        }

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());

        /*System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
        System.out.println(page1.get());
        System.out.println(page2.get());
        System.out.println(page3.get());*/
    }
}
