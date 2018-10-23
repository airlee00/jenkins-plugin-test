package frame.mgt.jenkins.batch.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;

import frame.mgt.jenkins.batch.config.FrameworkRepositoryDbConfiguration;
import frame.mgt.server.manage.batch.mapper.BatJobInstanceMapper;
import hudson.Extension;
import hudson.model.RootAction;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchJobExecutionListRootAction implements RootAction {
	
	private final static Logger LOGGER = Logger.getLogger(BatchJobExecutionListRootAction.class.getName());
			
	@Inject
	FrameworkRepositoryDbConfiguration frameworkRepositoryDbConfiguration;

	public String getDisplayName() {
		return "BachJobExecution";
	}

	public String getIconFileName() {
		return "terminal.png";
	}

	public String getUrlName() {
		return "batch-list";
	}

	public HttpResponse doExecute(@QueryParameter String jobId, @QueryParameter String startDate,
			@QueryParameter String endDate) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		List<Map> list = new ArrayList<Map>();
		try {
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("JOB_NM",jobId);
			paramMap.put("START_DATE",startDate);
			paramMap.put("END_DATE",endDate);
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			list = mapper.getBatJobExecutionList(paramMap);
		} finally {
			sqlSession.close();
		}
		return HttpResponses.forwardToView(this, "index").with("exelist", list);

	}
}
