import java.io.IOException;
import java.util.Set;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;

public class Test {

	public static void main(String[] args) throws IOException {
		//String packageName ="org/jvnet/hudson/test/mybatis"; 
		String packageName ="org.jvnet.hudson.test.mybatis.mapper"; 
		//List<String> children = VFS.getInstance().list("org/jvnet/hudson/test/mybatis");
		// System.out.println(children);
		 
		 
//		    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
//		    resolverUtil.find(new ResolverUtil.IsA(Object.class), packageName);
//		    Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
//		    System.out.println(mapperSet);
		    
		    
		    Configuration configuration = new  Configuration();
		    configuration.addMappers("org.jvnet.hudson.test.mybatis.mapper");
		    System.out.println(configuration.getMapperRegistry().getMappers());
	}
}
