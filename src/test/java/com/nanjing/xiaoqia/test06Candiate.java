package com.nanjing.xiaoqia;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class test06Candiate {


    /**
     * 部署流程
     */
    @Test
    public void deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("Myholiday-candiatebpmn20.xml")
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
        varibles.put("candidate1", "张三");
        varibles.put("candidate2", "李四");
        varibles.put("candidate3", "王武");
        runtimeService.startProcessInstanceById("myProcess:1:4", varibles);
    }


    /**
     * 任务被拾取之后才可以用
     */
    @Test
    public void queryTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId("2501")
                .taskCandidateUser("张三")
                .list();

        for (Task task : list) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
        }

    }

    /**
     * 拾取任务
     */
    @Test
    public void claimTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task list = taskService.createTaskQuery()
                .processInstanceId("2501")
                .taskCandidateUser("王武")
                .singleResult();
        if (list != null) {
            // System.out.println(list);
            taskService.claim(list.getId(), "王武");
        }
    }


    /**
     * 任务退回
     */
    @Test
    public void unClaimTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task list = taskService.createTaskQuery()
                .processInstanceId("2501")
                .taskAssignee("张三")
                .singleResult();
        if (list != null) {
            // System.out.println(list);
            taskService.unclaim(list.getId());
        }

    }


    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                //  .processDefinitionKey("myProcess")
                .processInstanceId("2501")
                .taskAssignee("王武")
                .singleResult();

        System.out.println(task+"3333333333333333");

        //完成任务
        taskService.complete(task.getId());
    }


}
