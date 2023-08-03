package com.study.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class SampleTest {

    @Autowired
    private ApplicationContext ac;

    @Test
    void 샘플_테스트() {
        String[] names = ac.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(
                    "@@@@@ Bean Name = " + name);
        }

    }

}
