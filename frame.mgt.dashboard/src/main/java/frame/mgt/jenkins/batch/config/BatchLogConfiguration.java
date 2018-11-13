package frame.mgt.jenkins.batch.config;

import java.sql.SQLException;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.jenkinsci.plugins.database.Database;
import org.kohsuke.stapler.StaplerRequest;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import frame.mgt.server.manage.batch.mapper.BatJobInstanceMapper;
import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

/**
 * Example of how to define a configuration that involves database.
 *
 * <ul>
 * <li>Define a database property in your class (doesn't have to be {@link GlobalConfiguration}
 * <li>Use <tt>dropdownDescriptorSelector</tt> in the UI (see {@code config.groovy})
 * </ul>
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchLogConfiguration extends GlobalConfiguration {
    private String baseDir ;
  
    public BatchLogConfiguration() {
        load();
    }

    @Override
    public String getDisplayName() {
        return "Batch log base dir Configuration";
    }

    
    public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
     * Applies the submitted configuration to this object
     * (That is, update the {@link #database} field.
     */
    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        save();
        return true;
    }
}
