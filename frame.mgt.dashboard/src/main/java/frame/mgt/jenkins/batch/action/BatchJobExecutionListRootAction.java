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
import org.kohsuke.stapler.bind.JavaScriptMethod;

import frame.mgt.jenkins.batch.config.FrameworkRepositoryDbConfiguration;
import frame.mgt.server.manage.batch.mapper.BatJobInstanceMapper;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.RootAction;
import jenkins.model.Jenkins;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchJobExecutionListRootAction implements RootAction {
	
	private final static Logger LOGGER = Logger.getLogger(BatchJobExecutionListRootAction.class.getName());
			
	@Inject
	FrameworkRepositoryDbConfiguration frameworkRepositoryDbConfiguration;

	public String getDisplayName() {
		return "Bach-JobExecution";
	}

	public String getIconFileName() {
		return "terminal.png";
	}

	public String getUrlName() {
		return "batchjob";
	}

	public HttpResponse doExecute(@QueryParameter String jobName, @QueryParameter String startDate,
			@QueryParameter String endDate, @QueryParameter String pageNumber, @QueryParameter String pageSize) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_NM",jobName);
		paramMap.put("START_DATE",startDate);
		paramMap.put("END_DATE",endDate);
		paramMap.put("pageNumber", (pageNumber==null||"".equals(pageNumber)?"0":pageNumber));
		paramMap.put("pageSize",pageSize==null||"".equals(pageSize)?"10":pageSize);
		List<Map> list = new ArrayList<Map>();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			list = mapper.getBatJobExecutionList(paramMap);
			if(list != null) {
				for(Map map : list) {
					final AbstractProject buildJob = Jenkins.getInstance().getItemByFullName(""+map.get("JOB_NM"), AbstractProject.class);
					if( buildJob != null) {
						map.put("RESULT", buildJob.getLastBuild().getResult());
						map.put("BUILD_NUMBER",buildJob.getLastBuild().number);
					}
				}
				//LOGGER.info("=============list====>"+ list);	
			}
		} finally {
			sqlSession.close();
		}
		return HttpResponses.forwardToView(this, "index").with("exelist", list).with(paramMap);

	}
	
	/**
	 * 사용x
	 * @param jobExecutionId
	 * @param jobId
	 * @return
	 * @throws Exception
	 * @Deprecated
	 */
	public HttpResponse doGetList(@QueryParameter String jobExecutionId, @QueryParameter String jobId) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_EXECUTION_ID",jobExecutionId);
		paramMap.put("JOB_ID",jobId);
        
		Map<?,?> resultMap = new HashMap();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			resultMap = mapper.getBatJobExecutionDetailList(paramMap);
		} finally {
			sqlSession.close();
		}
		return HttpResponses.forwardToView(this, "index2").with("resultMap", resultMap).with(paramMap);

	}
	
	//batchService.getBatJobExecutionDetailList : {"JOB_EXECUTION_ID":"2759","JOB_ID":"sample_db2db"}: 
	/**
	 * job실행 상세
	 * @param jobExecutionId
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	@JavaScriptMethod
	public Map getJobDetail(String jobExecutionId, String jobId) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_EXECUTION_ID",jobExecutionId);
		paramMap.put("JOB_ID",jobId);
        
		Map<?,?> resultMap = new HashMap();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			resultMap = mapper.getBatJobExecutionDetailList(paramMap);
		} finally {
			sqlSession.close();
		}
		return resultMap;
		//return HttpResponses.forwardToView(this, "index2").with("resultMap", resultMap).with(paramMap);

	}
	//batchService.getBatStepExecution.json{"searchData":{"JOB_EXECUTION_ID":"2759"}}: 
	/**
	 * 스텝 상세 리스트 조회 
	 * @param jobExecutionId
	 * @return
	 * @throws Exception
	 */
	@JavaScriptMethod
	public List<Map> getStepList(String jobExecutionId) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_EXECUTION_ID",jobExecutionId);
        
		List<Map> resultList = new ArrayList();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			resultList = mapper.getBatStepExecution(paramMap);
		} finally {
			sqlSession.close();
		}
		return resultList;

	}
}
