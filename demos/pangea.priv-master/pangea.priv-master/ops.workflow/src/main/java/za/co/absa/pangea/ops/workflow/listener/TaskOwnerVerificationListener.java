package za.co.absa.pangea.ops.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskOwnerVerificationListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		String senderVar = (String) delegateTask.getExecution().getVariable("sender");
		delegateTask.setOwner(senderVar);
	}


}
