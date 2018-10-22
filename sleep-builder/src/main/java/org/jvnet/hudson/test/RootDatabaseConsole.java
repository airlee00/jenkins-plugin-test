package org.jvnet.hudson.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.jenkinsci.plugins.database.Database;
import org.jvnet.hudson.test.mybatis.MyBatisUtil;
import org.jvnet.hudson.test.mybatis.User;
import org.jvnet.hudson.test.mybatis.mapper.UserMapper;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.RootAction;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class RootDatabaseConsole implements RootAction {
    @Inject
    Sample sample;

    public String getDisplayName() {
        return "Database Console";
    }

    public String getIconFileName() {
        return "terminal.png";
    }

    public String getUrlName() {
        return "database-console";
    }

    public HttpResponse doExecute(@QueryParameter String sql) throws SQLException {
        Database db = sample.getDatabase();
//        if (db==null)
//            throw new IllegalArgumentException("Database isn't configured yet");
//
//        Connection con = db.getDataSource().getConnection();
//        Statement s = con.createStatement();
//        if (s.execute(sql)) {
//            return HttpResponses.forwardToView(this,"index").with("r",s.getResultSet());
//        } else {
//            return HttpResponses.forwardToView(this,"index").with("message","OK");
//        }
    	
    	SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
    	List<User> list = new ArrayList();
    	User  user ;
    	  try{
    	  UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    	  list =  userMapper.getAllUsers();
    	   //user = userMapper.getUserById(Integer.valueOf(sql));
    	  }finally{
    	   sqlSession.close();
    	  }
    	  return HttpResponses.forwardToView(this,"index").with("r",list);

    	
    }
}
