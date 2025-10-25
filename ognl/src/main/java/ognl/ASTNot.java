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

import java.io.Serial;

public class ASTNot<C extends OgnlContext<C>> extends BooleanExpression<C> {

    @Serial
    private static final long serialVersionUID = 1202881695483879532L;

    public ASTNot(int id) {
        super(id);
    }

    public ASTNot(OgnlParser p, int id) {
        super(p, id);
    }

    protected Object getValueBody(C context, Object source) throws OgnlException {
        return OgnlOps.booleanValue(children[0].getValue(context, source)) ? Boolean.FALSE : Boolean.TRUE;
    }

    public String getExpressionOperator(int index) {
        return "!";
    }

    public String toGetSourceString(C context, Object target) {
        try {

            String srcString = super.toGetSourceString(context, target);

            if (srcString == null || srcString.trim().isEmpty()) {
                srcString = "null";
            }

            context.setCurrentType(Boolean.TYPE);

            return "(! ognl.OgnlOps.booleanValue(" + srcString + ") )";

        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
