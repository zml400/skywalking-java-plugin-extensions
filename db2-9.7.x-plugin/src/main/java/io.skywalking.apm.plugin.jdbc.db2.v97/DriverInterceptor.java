package io.skywalking.apm.plugin.jdbc.db2.v97;

import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.plugin.jdbc.connectionurl.parser.URLParser;
import org.apache.skywalking.apm.plugin.jdbc.trace.ConnectionInfo;

import java.lang.reflect.Method;
import java.util.Properties;

public class DriverInterceptor implements InstanceMethodsAroundInterceptor {
    @Override public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                       Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {

    }

    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        if (ret != null && ret instanceof EnhancedInstance) {
            //获得连接信息
            ConnectionInfo connectionInfo = URLParser.parser((String)allArguments[0]);
            if(allArguments.length>1) {
                //获得数据库用户名
                String userName = ((Properties) allArguments[1]).getProperty("user");
                //将原数据库名改为用户名，因为db2的真实数据库名称为用户名
                connectionInfo.setDatabaseName(userName);
            }
            ((EnhancedInstance)ret).setSkyWalkingDynamicField(connectionInfo);
        }
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                                Class<?>[] argumentsTypes, Throwable t) {

    }


}
