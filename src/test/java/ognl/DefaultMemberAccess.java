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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * This class provides methods for setting up and restoring
 * access in a Field.  Java 2 provides access utilities for setting
 * and getting fields that are non-public.  This object provides
 * coarse-grained access controls to allow access to private, protected
 * and package protected members.  This will apply to all classes
 * and members.
 */
public class DefaultMemberAccess implements MemberAccess {
    /*
     * Assign an accessibility modification mechanism, based on Major Java Version.
     *   Note: Can be overridden using a Java option flag {@link OgnlRuntime#USE_PREJDK9_ACESS_HANDLER}.
     */
    private static final AccessibleObjectHandler _accessibleObjectHandler;

    static {
        _accessibleObjectHandler = AccessibleObjectHandlerJDK9Plus.createHandler();
    }

    public boolean allowPrivateAccess;
    public boolean allowProtectedAccess;
    public boolean allowPackageProtectedAccess;

    public DefaultMemberAccess(boolean allowAllAccess) {
        this(allowAllAccess, allowAllAccess, allowAllAccess);
    }

    public DefaultMemberAccess(boolean allowPrivateAccess, boolean allowProtectedAccess, boolean allowPackageProtectedAccess) {
        super();
        this.allowPrivateAccess = allowPrivateAccess;
        this.allowProtectedAccess = allowProtectedAccess;
        this.allowPackageProtectedAccess = allowPackageProtectedAccess;
    }

    public boolean getAllowPrivateAccess() {
        return allowPrivateAccess;
    }

    public void setAllowPrivateAccess(boolean value) {
        allowPrivateAccess = value;
    }

    public boolean getAllowProtectedAccess() {
        return allowProtectedAccess;
    }

    public void setAllowProtectedAccess(boolean value) {
        allowProtectedAccess = value;
    }

    public boolean getAllowPackageProtectedAccess() {
        return allowPackageProtectedAccess;
    }

    public void setAllowPackageProtectedAccess(boolean value) {
        allowPackageProtectedAccess = value;
    }

    public Object setup(OgnlContext context, Object target, Member member, String propertyName) {
        Object result = null;

        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject accessible = (AccessibleObject) member;

            if (!accessible.canAccess(target)) {
                result = Boolean.FALSE;
                _accessibleObjectHandler.setAccessible(accessible, true);
            }
        }
        return result;
    }

    public void restore(OgnlContext context, Object target, Member member, String propertyName, Object state) {
        if (state != null) {
            final AccessibleObject accessible = (AccessibleObject) member;
            final boolean stateboolean = ((Boolean) state).booleanValue();  // Using twice (avoid unboxing)
            if (!stateboolean) {
                _accessibleObjectHandler.setAccessible(accessible, stateboolean);
            } else {
                throw new IllegalArgumentException("Improper restore state [" + stateboolean + "] for target [" + target +
                        "], member [" + member + "], propertyName [" + propertyName + "]");
            }
        }
    }

    /**
     * Returns true if the given member is accessible or can be made accessible
     * by this object.
     *
     * @param context      the current execution context (not used).
     * @param target       the Object to test accessibility for (not used).
     * @param member       the Member to test accessibility for.
     * @param propertyName the property to test accessibility for (not used).
     * @return true if the member is accessible in the context, false otherwise.
     */
    public boolean isAccessible(OgnlContext context, Object target, Member member, String propertyName) {
        int modifiers = member.getModifiers();
        boolean result = Modifier.isPublic(modifiers);

        if (!result) {
            if (Modifier.isPrivate(modifiers)) {
                result = getAllowPrivateAccess();
            } else {
                if (Modifier.isProtected(modifiers)) {
                    result = getAllowProtectedAccess();
                } else {
                    result = getAllowPackageProtectedAccess();
                }
            }
        }
        return result;
    }
}
