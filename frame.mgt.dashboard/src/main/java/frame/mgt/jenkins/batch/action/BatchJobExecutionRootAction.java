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
import hudson.model.AbstractDescribableImpl;
import hudson.model.AbstractProject;
import hudson.model.RootAction;
import jenkins.model.Jenkins;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class BatchJobExecutionRootAction extends AbstractDescribableImpl<BatchJobExecutionDescriptor> implements RootAction {
	
	private final static Logger LOGGER = Logger.getLogger(BatchJobExecutionRootAction.class.getName());

    
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

		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("JOB_NM",jobName);
		paramMap.put("START_DATE",startDate);
		paramMap.put("END_DATE",endDate);
		paramMap.put("pageSize",pageSize);
		paramMap.put("pageNumber",pageNumber);
		PageVo vo = new PageVo(pageSize, pageNumber);
		paramMap.put("LIMIT", vo.getPageLimit());
		paramMap.put("OFFSET", vo.getPageOffset());
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
			int rowcount = mapper.getBatJobExecutionDetailListCount(paramMap);
			vo.setTotalRowCount(rowcount);
			paramMap.put("TOTAL_PAGE", vo.getTotalPage());
		} finally {
			sqlSession.close();
		}
		return HttpResponses.forwardToView(this, "index").with("exelist", list).with(paramMap);

	}
	
	/**
	 * job실행 상세
	 * batchService.getBatJobExecutionDetailList : {"JOB_EXECUTION_ID":"2759","JOB_ID":"sample_db2db"}: 
	 * batchService.getBatStepExecution.json{"searchData":{"JOB_EXECUTION_ID":"2759"}}: 
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
        
		Map<String,Object> resultMap = new HashMap();
		List<Map> stepList = new ArrayList();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			resultMap = mapper.getBatJobExecutionDetailList(paramMap);
			stepList = mapper.getBatStepExecution(paramMap);
			resultMap.put("stepList", stepList);
		} finally {
			sqlSession.close();
		}
		return resultMap;
	}
	/**
	 * Job 중지
	 * - 업데이트 => STARTING, STARTED 상태를  -> STOPPING, END_TIME=SYSDATE, VERSION = VERSION+1
	 * - version을 증가시키는 방법?
	 * @param jobExecutionId
	 * @return
	 * @throws Exception
	 */
	@JavaScriptMethod
	public Map doStop(String jobExecutionId,String jobId) throws Exception {

		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");

		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_ID",jobId);
		paramMap.put("JOB_EXECUTION_ID",jobExecutionId);
		paramMap.put("STATUS","STOPPING");
        
		Map<String,Object> resultMap = new HashMap();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			int affectedRow = mapper.updateBatJob(paramMap);
			resultMap.put("affectedRow", affectedRow);
			final AbstractProject buildJob = Jenkins.getInstance().getItemByFullName(jobId, AbstractProject.class);
			if( buildJob != null) {
				HttpResponse response = buildJob.getLastBuild().doStop();
				resultMap.put("RESULT", buildJob.getLastBuild().getResult());
				resultMap.put("BUILD_NUMBER",buildJob.getLastBuild().number);
				resultMap.put("STOP_RESULT",response);
			}
			sqlSession.commit();
		} catch(Exception e) {
			LOGGER.info(e.getMessage());
			sqlSession.rollback();
		} finally {
			 sqlSession.close();
		}
		return resultMap;

	}
	/**
	 * Job 폐기
	 * - 업데이트 => stop, stopping 상태를  -> ABANDONED, END_TIME=SYSDATE, VERSION = VERSION+1
	 * - version을 증가시키는 방법?
	 * @param jobExecutionId
	 * @return
	 * @throws Exception
	 */
	@JavaScriptMethod
	public Map doAbandon(String jobExecutionId,String jobId) throws Exception {
		
		SqlSession sqlSession = frameworkRepositoryDbConfiguration.getSqlSessionFactory().openSession();
		if (sqlSession == null)
			throw new IllegalArgumentException("SqlSession isn't configured yet");
		
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("JOB_ID",jobId);
		paramMap.put("JOB_EXECUTION_ID",jobExecutionId);
		paramMap.put("STATUS","ABANDONED");
		Map<String,Object> resultMap = new HashMap();
		try {
			LOGGER.info("=============param====>"+paramMap);	
			BatJobInstanceMapper mapper = sqlSession.getMapper(BatJobInstanceMapper.class);
			int affectedRow = mapper.updateBatJob(paramMap);
			resultMap.put("affectedRow", affectedRow);
			final AbstractProject buildJob = Jenkins.getInstance().getItemByFullName(jobId, AbstractProject.class);
			if( buildJob != null) {
				HttpResponse response = buildJob.getLastBuild().doStop();
				resultMap.put("RESULT", buildJob.getLastBuild().getResult());
				resultMap.put("BUILD_NUMBER",buildJob.getLastBuild().number);
				resultMap.put("STOP_RESULT",response);
			}
			sqlSession.commit();
		} catch(Exception e) {
			LOGGER.info(e.getMessage());
			sqlSession.rollback();
		} finally {
			 sqlSession.close();
		}
		return resultMap;
		
	}

}
