package com.nanjing.xiaoqia;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.junit.Test;

public class ProcessEngineTest {


    @Test
    public void processEngine() {


        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        System.out.println(defaultProcessEngine);


    }


}
