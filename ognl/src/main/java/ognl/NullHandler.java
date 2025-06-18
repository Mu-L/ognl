/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ognl;

/**
 * Interface for handling null results from Chains.
 * Object has the opportunity to substitute an object for the
 * null and continue.
 */
public interface NullHandler {
    /**
     * Method called on target returned null.
     *
     * @param context    the current execution context.
     * @param target     the Object on which the method was called.
     * @param methodName the name of the method which was called.
     * @param args       the arguments to the method that was called.
     * @return the result Object containing the state of the method call that returned null.
     */
    Object nullMethodResult(OgnlContext context, Object target, String methodName, Object[] args);

    /**
     * Property in target evaluated to null.  Property can be a constant
     * String property name or a DynamicSubscript.
     *
     * @param context  the current execution context.
     * @param target   the Object to which the property belongs.
     * @param property the property whose value evaluated to null.
     * @return the result Object containing the state of the property that evaluated to null.
     */
    Object nullPropertyValue(OgnlContext context, Object target, Object property);
}

