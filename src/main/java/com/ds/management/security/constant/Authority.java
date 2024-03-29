package com.ds.management.security.constant;

public class Authority {
    public static final String[] USER_AUTHORITIES = {"user:read", "device:read"};
    public static final String[] HR_AUTHORITIES = {"user:read", "user:update"};
    public static final String[] MANAGER_AUTHORITIES = {"user:read", "user:update"};
    public static final String[] ADMIN_AUTHORITIES = {"user:read", "user:update", "user:create"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {
            "user:read",
            "user:update",
            "user:create",
            "user:delete",
            "device:read",
            "device:update",
            "device:create",
            "device:delete"
    };
}
