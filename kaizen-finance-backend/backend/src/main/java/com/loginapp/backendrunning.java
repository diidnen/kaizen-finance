package com.loginapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class backendrunning {
    public static void main(String[] args) {
        SpringApplication.run(backendrunning.class, args);
        System.out.println("Server is running on port 8080");
    }
}


/*@SpringBootApplication
public class LoginApplication {
    public static void main(String[] args) {
        // 添加这个参数来查看自动配置报告
        SpringApplication app = new SpringApplication(LoginApplication.class);
        app.setAddCommandLineProperties(true);
        app.run("--debug");
    }
}*/


