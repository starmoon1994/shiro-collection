package com.company.testshiro3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestShiro3ApplicationTests {

    @Test
    public void contextLoads(ApplicationContext applicationContext) {

        applicationContext.getResource("");
    }

}
