package com.nanjing.xiaoqia;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class test05ZiDingyi {


    /**
     * 部署流程
     */
    @Test
    public void deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("MyholidayBianLiangbpmn20.xml")
                .deploy();

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }


    /**
     * 启动流程
     */
    @Test
    public void runProcess() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> varibles = new HashMap<>();
        varibles.put("assignee0", "张三");
        varibles.put("assignee1", "李四");
        varibles.put("assignee2", "王武");
        varibles.put("assignee3", "赵会");
        runtimeService.startProcessInstanceById("myProcess:1:4", varibles);
    }


    /**
     * 完成流程
     * 全局变量是跟流程实例绑定的
     */
    @Test
    public void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId("2501")
                .taskAssignee("李四")
                .singleResult();
        //查询 当前实例的所有变量
        System.out.println(task);

       // Map<String, Object> processVariables = task.getProcessVariables();
        //processVariables.put("num", 2);
        taskService.complete(task.getId());

    }


    /**
     * 修改局部变量
     */
    @Test
    public void updateVariable() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId("2501")
                .taskAssignee("李四")
                .singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num", 5 );
        //设置本地变量修改
        taskService.setVariablesLocal(task.getId(), variables);
    }


}
