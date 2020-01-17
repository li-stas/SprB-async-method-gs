package com.example.gsasyncmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
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
        // массив запросов
        ArrayList<String> aUrl = new ArrayList<String>();
        aUrl.add("PivotalSoftware");
        aUrl.add("CloudFoundry");
        aUrl.add("Spring-Projects");
        aUrl.add("PivotalSoftware");
        aUrl.add("CloudFoundry");
        aUrl.add("Spring-Projects");

        // постановка всех задач в поток
        int nLen_aUrl = aUrl.size();
        Future<User> [] thr = new Future[nLen_aUrl];
        for (int i = 0; i < nLen_aUrl; i++) {
            thr[i] = gitHubLookupService.findPage(aUrl.get(i));
        }

        // Подождите, пока они все не сделали // Wait until they are all done
        while (true) {
            int nChk = 0;
            for (int i = 0; i < nLen_aUrl; i++) {
                nChk += thr[i].isDone() ? 1 : 0;
            }
            if (nChk == nLen_aUrl) {
                break;
            } else {
                Thread.sleep(10); //millisecond pause between each check
            }
        }

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        for (int i = 0; i < nLen_aUrl; i++) {
            logger.info("--> " + thr[i].get());
        }

        /*System.out.println("Elapsed time: " + (System.currentTimeMillis() - start));
        System.out.println(page1.get());
        System.out.println(page2.get());
        System.out.println(page3.get());*/
    }
}
