package frame.mgt.jenkins.batch.action;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;

import frame.mgt.jenkins.batch.config.BatchLogConfiguration;
import hudson.Extension;
import hudson.model.RootAction;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchJobLogViewAction implements RootAction {
	
	private final static Logger LOGGER = Logger.getLogger(BatchJobLogViewAction.class.getName());
    
	@Inject
	BatchLogConfiguration batchLogConfiguration;
	
	public String getDisplayName() {
		return "Bach-Job-logView";
	}

	public String getIconFileName() {
		return "terminal.png";
	}

	public String getUrlName() {
		return "batchlog";
	}

	public HttpResponse doLogView(@QueryParameter String jobName, @QueryParameter String date) throws Exception {

		LOGGER.info("=============param====>"+jobName + ",date=" + date);	
		return HttpResponses.ok();

	}
}
