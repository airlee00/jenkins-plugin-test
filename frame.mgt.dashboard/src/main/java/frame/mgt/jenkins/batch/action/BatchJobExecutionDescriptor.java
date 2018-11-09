package frame.mgt.jenkins.batch.action;

import java.text.SimpleDateFormat;

import org.jenkinsci.plugins.database.Database;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;

/**
 * {@link Descriptor} for {@link Database}
 *
 * @author Kohsuke Kawaguchi
 */
public class BatchJobExecutionDescriptor extends AbstractDescribableImpl<BatchJobExecutionDescriptor> {
    public BatchJobExecutionDescriptor() {
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<BatchJobExecutionDescriptor> {
        public FormValidation doCheckStartDate(@QueryParameter String value ) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        	try {
        		df.parse(value);
        		return FormValidation.ok();
        	}catch(Exception e) {
        		return FormValidation.warning("data format must be yyyy-MM-dd ");
        	}
        }
    }
}
