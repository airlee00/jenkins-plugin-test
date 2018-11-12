package frame.mgt.jenkins.batch.action;

import java.io.Serializable;

/**
 * 페이지 처리 Vo
 * --
 * <pre>
 *  inBcVo의 include 형태로 추가하되 UI와 약속된 명칭인 pageVo이름으로 생성되야 한다. 
 * </pre>
 * @author kclee01
 * @version 1.0
 * @since 2017.10.30 신규작성
 */
public class PageVo implements Serializable {

    private static final long serialVersionUID = 8132334251378821232L;

    private int pageSize = 10; //기본값 10 
    
    private int pageNumber =1 ;

    private int totalRowCount;
    
    public PageVo(String pageSize, String pageNumber) {
		this.pageSize = (pageSize ==null || "".equals(pageSize)) ? 10 :Integer.valueOf(pageSize);
		this.pageNumber = (pageNumber==null||"".equals(pageNumber))? 1 :Integer.valueOf(pageNumber);
	}
    
    public PageVo(int pageSize, int pageNumber) {
		super();
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageOffset() {
        return (this.pageNumber - 1) * this.pageSize;
    }
    
    public int getPageLimit() {
        return this.pageSize;
    }
    
    public int getTotalRowCount() {
        return totalRowCount;
    }
    public int getTotalPage() {
    	int totalPage = totalRowCount/pageSize;
    	if(totalRowCount % pageSize > 0) {
    		totalPage++;
    	}
    	return totalPage;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    @Override
    public String toString() {
        return "PageVo [pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", totalRowCount=" + totalRowCount + "]";
    }

    public static void main(String[] args) {
    	PageVo vo = new PageVo(10,1);
    	vo.setTotalRowCount(11);
    	System.out.println(vo.getTotalPage());
	}
	
}
