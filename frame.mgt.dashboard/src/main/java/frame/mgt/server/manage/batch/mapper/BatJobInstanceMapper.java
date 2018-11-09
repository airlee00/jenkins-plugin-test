package frame.mgt.server.manage.batch.mapper;

import java.util.List;
import java.util.Map;

public interface BatJobInstanceMapper {
	public List<Map> getBatJobExecutionList(Map paramMap) throws Exception;
	
//    public List<Map> getBatJobInstance(Map paramMap) throws Exception;
//
//    //[2080607]public List<Map> getBatJobParams(Map paramMap) throws Exception;
//
//    public List<Map> getBatJobExecution(Map paramMap) throws Exception;
//
    public List<Map> getBatStepExecution(Map paramMap) throws Exception;
//
//    public List<Map> getBatJobExecutionTimeStatics(Map paramMap)  throws Exception;	
//    
//	public List<Map> getBatJobExecutionDayGraph(Map paramMap)   throws Exception;   
//	
	public Map getBatJobExecutionDetailList(Map paramMap)   throws Exception;   
	
	public int getBatJobExecutionDetailListCount(Map paramMap)   throws Exception;   
	
	public int updateBatJob(Map paramMap)   throws Exception;   
//	
//    //현재작업상태(메인)
//    public List<Map> getBatJobExecutionCurrentGraph(Map paramMap) throws Exception;
//    //작업상태모니터링(메인)
//    public List<Map> getBatJobExecutionCurrentMonitorGraph(Map paramMap) throws Exception;
//
//    //배치서버 자원 사용량 모니터링 시간대별 조회
//    public List<Map> getSvrMonitorHoursList(Map paramMap)  throws Exception;	
//    //배치서버 자원 사용량 모니터링 잡별 조회
//    public List<Map> getSvrMonitorJobList(Map paramMap)  throws Exception;	
    
}
