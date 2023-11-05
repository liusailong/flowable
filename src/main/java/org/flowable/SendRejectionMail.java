package org.flowable;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {


    /**
     * 这是flowable中的一个触发器
     * @param delegateExecution
     */

    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("你的想法被驳回了。。。。。。。。");
    }
}
