package org.jvnet.hudson.test;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

public class SleepBuilder extends Builder {
    public final long time;

    @DataBoundConstructor
    public SleepBuilder(long time) {
        this.time = time;
    }

    
    public long getTime() {
		return time;
	}


	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("Sleeping "+time+"ms");
        Thread.sleep(time);
        return true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public String getDisplayName() {
            return "Sleep Builder2";
        }

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			// TODO Auto-generated method stub
			return jobType == FreeStyleProject.class;
		}
    }
}
