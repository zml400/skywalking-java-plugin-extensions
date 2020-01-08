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

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.MultiClassNameMatch;
import org.apache.skywalking.apm.plugin.jdbc.define.Constants;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class ConnectionInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String CONNECTION_CLASS = "com.ibm.db2.jcc.am.Connection";
    public static final String T2ZOS_CONNECTION_CLASS = "com.ibm.db2.jcc.t2zos.T2zosConnection";
    public static final String T2ZOS_REUSABLE_CONNECTION_CLASS = "com.ibm.db2.jcc.t2zos.T2zosReusableConnection";
    public static final String T4XA_CONNECTION_CLASS = "com.ibm.db2.jcc.t4.T4XAConnection";
    public static final String UW_CONNECTION_CLASS = "com.ibm.db2.jcc.uw.UWConnection";
    public static final String B_CONNECTION_CLASS = "com.ibm.db2.jcc.t4.b";
    public static final String DM_CONNECTION_CLASS = "com.ibm.db2.jcc.am.dm";
    public static final String U_CONNECTION_CLASS = "com.ibm.db2.jcc.t2zos.u";

    public static final String PREPARED_STATEMENT_INTERCEPT_CLASS = "io.skywalking.apm.plugin.jdbc.db2.v105.CreatePreparedStatementInterceptor";
    public static final String CALLABLE_INTERCEPT_CLASS = "io.skywalking.apm.plugin.jdbc.db2.v105.CreateCallableInterceptor";
    public static final String CREATE_STATEMENT_INTERCEPT_CLASS = "io.skywalking.apm.plugin.jdbc.db2.v105.CreateStatementInterceptor";

    @Override public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(Constants.PREPARE_STATEMENT_METHOD_NAME);
                }

                @Override public String getMethodsInterceptor() {
                    return PREPARED_STATEMENT_INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            },
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(Constants.PREPARE_CALL_METHOD_NAME).and(takesArguments(3));
                }

                @Override public String getMethodsInterceptor() {
                    return CALLABLE_INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            },
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(Constants.CREATE_STATEMENT_METHOD_NAME).and(takesArguments(2));
                }

                @Override public String getMethodsInterceptor() {
                    return CREATE_STATEMENT_INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            },
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(Constants.COMMIT_METHOD_NAME).or(named(Constants.ROLLBACK_METHOD_NAME)).or(named(Constants.CLOSE_METHOD_NAME)).or(named(Constants.RELEASE_SAVE_POINT_METHOD_NAME));
                }

                @Override public String getMethodsInterceptor() {
                    return Constants.SERVICE_METHOD_INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(
                CONNECTION_CLASS,
                T2ZOS_CONNECTION_CLASS,
                T2ZOS_REUSABLE_CONNECTION_CLASS,
                T4XA_CONNECTION_CLASS,
                DM_CONNECTION_CLASS,
                UW_CONNECTION_CLASS,
                B_CONNECTION_CLASS,
                U_CONNECTION_CLASS);
    }
}
