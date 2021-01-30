package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Service
public class ChooseBetaReadersDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        Set<String> betaReaders = UtilService.parseBetaReaders((String) data.get("beta_readers"));
        delegateExecution.setVariable("beta_readers", new ArrayList<>(betaReaders));
    }
}
