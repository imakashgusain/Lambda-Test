package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * AWS Lambda handler for EventBridge events.
 * Simple boilerplate that logs "Hello World" when triggered.
 */
@Slf4j
public class HelloWorldHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object event, Context context) {
        // Log "Hello World" to console using SLF4J
        log.info("Hello World");
        log.info("testing works");

        return "Hello World - akash";
    }
}
