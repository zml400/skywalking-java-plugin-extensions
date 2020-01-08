/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.skywalking.apm.plugin.jdbc.db2.v105.define;

import io.skywalking.apm.plugin.jdbc.db2.v105.Constants;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.MultiClassNameMatch;

import static io.skywalking.apm.plugin.jdbc.db2.v105.Constants.STATEMENT_INTERCEPT_CLASS;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class DB2StatementInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String CALLABLE_STATEMENT_CLASS = "com.ibm.db2.jcc.am.CallableStatement";
    public static final String DQ_STATEMENT_CLASS = "com.ibm.db2.jcc.am.dq";
    public static final String DR_STATEMENT_CLASS = "com.ibm.db2.jcc.am.dr";
    public static final String DS_STATEMENT_CLASS = "com.ibm.db2.jcc.am.ds";
    public static final String DT_STATEMENT_CLASS = "com.ibm.db2.jcc.am.dt";
    public static final String DU_STATEMENT_CLASS = "com.ibm.db2.jcc.am.du";
    public static final String I_STATEMENT_CLASS = "com.ibm.db2.jcc.t4.i";
    public static final String J_STATEMENT_CLASS = "com.ibm.db2.jcc.t4.j";
    public static final String K_STATEMENT_CLASS = "com.ibm.db2.jcc.t4.k";
    public static final String KH_STATEMENT_CLASS = "com.ibm.db2.jcc.am.kh";
    public static final String KI_STATEMENT_CLASS = "com.ibm.db2.jcc.am.ki";
    public static final String L_STATEMENT_CLASS = "com.ibm.db2.jcc.t4.l";


    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        System.out.println("DB2StatementInstrumentation");
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named("execute")
                                .or(named("executeQuery"))
                                .or(named("executeUpdate"))
                                .or(named("executeLargeUpdate"))
                                .or(named("executeBatchInternal"))
                                .or(named("executeUpdateInternal"));
                    }
                    @Override
                    public String getMethodsInterceptor() {
                        return STATEMENT_INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                },
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named("setNull")
                                .or(named("setBoolean"))
                                .or(named("setByte"))
                                .or(named("setShort"))
                                .or(named("setInt"))
                                .or(named("setLong"))
                                .or(named("setFloat"))
                                .or(named("setDouble"))
                                .or(named("setBigDecimal"))
                                .or(named("setString"))
                                .or(named("setBytes"))
                                .or(named("setDate"))
                                .or(named("setTime"))
                                .or(named("setTimestamp"))
                                .or(named("setDBTimestamp"))
                                .or(named("setObject"));
                    }
                    @Override
                    public String getMethodsInterceptor() {
                        return Constants.PREPARED_STATEMENT_SET_PARAMS_INTERCEPT_CLASS;
                    }
                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(
                CALLABLE_STATEMENT_CLASS,
                DQ_STATEMENT_CLASS,
                DS_STATEMENT_CLASS,
                DT_STATEMENT_CLASS,
                DU_STATEMENT_CLASS,
                KH_STATEMENT_CLASS,
                DR_STATEMENT_CLASS,
                I_STATEMENT_CLASS,
                J_STATEMENT_CLASS,
                K_STATEMENT_CLASS,
                L_STATEMENT_CLASS,
                KI_STATEMENT_CLASS);
    }
}
