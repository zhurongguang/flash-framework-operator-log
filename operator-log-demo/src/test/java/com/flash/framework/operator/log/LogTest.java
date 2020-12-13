package com.flash.framework.operator.log;

import com.flash.framework.operator.log.core.model.Demo;
import com.flash.framework.operator.log.core.service.DemoService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

/**
 * @author zhurg
 * @date 2019/4/15 - 下午5:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OperatorLogCoreApplication.class})
public class LogTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void demo1() {
        System.out.println(demoService.demo(buildDemo()));
    }

    @Test
    public void demo2() {
        System.out.println(demoService.demo2(buildDemo()));
    }

    @Test
    public void demo3() {
        System.out.println(demoService.demo3(buildDemo()));
    }

    @Test
    public void demo4() {
        System.out.println(demoService.demo4(buildDemo()));
    }

    @Test
    public void demo5() {
        System.out.println(demoService.demo5(buildDemo()));
    }

    @Test
    public void demo6() {
        demoService.demo6(1L);
    }

    @Test
    public void demo7() {
        demoService.demo7(1L, "dd");
    }

    private Demo buildDemo() {
        Demo demo = new Demo();
        demo.setAge(new Random().nextInt(100));
        demo.setUsername(UUID.randomUUID().toString());
        demo.setExtra(ImmutableMap.of("aa", "bb", "cc", "dd"));
        demo.setInfos(Lists.newArrayList("ee", "ff"));
        return demo;
    }
}