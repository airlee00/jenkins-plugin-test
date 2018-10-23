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
public class FrameworkRepositoryDbConfiguration extends GlobalConfiguration {
    private Database database;

    @XStreamOmitField
    private SqlSessionFactory factory;
    
    public FrameworkRepositoryDbConfiguration() {
        load();
    }

    @Override
    public String getDisplayName() {
        return "framework Repository Database Configuration";
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public SqlSessionFactory getSqlSessionFactory() {
    	if(factory != null) {
    		return factory;
    	}else {
            
    		TransactionFactory transactionFactory = new JdbcTransactionFactory();
    		Environment environment = null;
    		try {
    			environment = new Environment("development", transactionFactory, database.getDataSource());
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		Configuration configuration = new Configuration(environment);
    		configuration.setMapUnderscoreToCamelCase(true);
    		configuration.addMapper(BatJobInstanceMapper.class);
    		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    		this.factory =  sqlSessionFactory;
    		return factory;
    		
    	}
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
