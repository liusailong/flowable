package com.nanjing.xiaoqia;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test02 {


    ProcessEngineConfiguration configuration = null;

    @Before
    public void before() {

        configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("123456");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true");
        //如果数据库中的表结构不存在就新建
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }


    /**
     * 部署流程
     */
    @Test
    public void testDeploy() {

        //1.获取ProcessEngine 对象
        ProcessEngine processEngine = configuration.buildProcessEngine();

        //2.获取RepositoryService 对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3. 完成流程的部署操作
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("Myholidaybpmn20.xml")
                .name("水墨江南请www假流程")
                .deploy();

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());

    }


    @Test
    public void testQueryDeploy() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("1")
                .singleResult();
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
    }


    /**
     * 删除流程实例
     */
    @Test
    public void testDeleteDeploy() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("2501", true);
    }


    /**
     * 流程启动
     */
    @Test
    public void testRunProcess() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        HashMap<String, Object> varibles = new HashMap<>();
//        varibles.put("enployee", "张三");
//        varibles.put("nrogholidays", 3);
//        varibles.put("desciption", "工作累了，出去散散心");
        //启动流程实例
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("myProcess");
        System.out.println("holidayRequest.getName() = " + holidayRequest.getName());
        System.out.println("holidayRequest.getProcessDefinitionId() = " + holidayRequest.getProcessDefinitionId());
        System.out.println("holidayRequest.getDescription() = " + holidayRequest.getDescription());

    }

    @Test
    public void testQueryTask() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> holidayRequest = taskService.createTaskQuery()
                .processDefinitionKey("myProcess")    //指定是哪一个流程
                .taskAssignee("张三")
                .list();

        for (Task task : holidayRequest) {
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getDescription() = " + task.getDescription());
            System.out.println("task.getId() = " + task.getId());

        }
    }


    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
              //  .processDefinitionKey("myProcess")
                .processInstanceId("10001")
                .taskAssignee("郭总")
                .singleResult();

        System.out.println(task+"3333333333333333");

        //完成任务
        taskService.complete(task.getId());
    }


    @Test
    public void testHistory() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("myProcess:1:4")
                .finished()
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();


        for (HistoricActivityInstance history : list) {
            System.out.println(history.getActivityName() + ":" + history.getAssignee() + "--"
                    + history.getActivityId() + ":" + history.getDurationInMillis());
        }
    }

    /**
     * 流程挂起
     */

    @Test
    public void testSuspended() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("myProcess:1:4")
                .singleResult();
        System.out.println(processDefinition.toString());
        boolean suspended = processDefinition.isSuspended();

        if (suspended) {
            //如果挂起，就激活
            repositoryService.activateProcessDefinitionById("myProcess:1:4");
            System.out.println("原来是挂起的现在被激活了");
        } else {
            //如果激活就挂起
            repositoryService.suspendProcessDefinitionById("myProcess:1:4");
            System.out.println("原来是激活的现在挂起了");

        }


    }


}
