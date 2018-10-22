package frame.mgt.jenkins.batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;

import org.jenkinsci.plugins.database.Database;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.RootAction;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchJobExecutionListRootAction implements RootAction {
    @Inject
    FrameworkRepositoryDbConfiguration frameworkRepositoryDbConfiguration;

    private String jobId = "";
    private String fromDate = "";
    private String toDate = "";
    
//    @DataBoundConstructor
//    public BatchJobExecutionListRootAction(String jobId, String fromDate, String toDate) {
//		super();
//		this.jobId = jobId;
//		this.fromDate = fromDate;
//		this.toDate = toDate;
//	}

	public String getDisplayName() {
        return "BachJobExecution";
    }

    public String getIconFileName() {
        return "terminal.png";
    }

    public String getUrlName() {
        return "batch-list";
    }

    public HttpResponse doExecute(@QueryParameter String sql) throws SQLException {
        Database db = frameworkRepositoryDbConfiguration.getDatabase();
        if (db==null)
            throw new IllegalArgumentException("Database isn't configured yet");

        Connection con = db.getDataSource().getConnection();
        Statement s = con.createStatement();
        if (s.execute(sql)) {
            return HttpResponses.forwardToView(this,"index").with("r",s.getResultSet());
        } else {
            return HttpResponses.forwardToView(this,"index").with("message","OK");
        }
    }
}
