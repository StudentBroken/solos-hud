package com.ua.sdk.role;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.role.Role;
import com.ua.sdk.user.permission.UserPermission;
import com.ua.sdk.user.permission.UserPermissionManager;
import com.ua.sdk.user.role.UserRole;
import com.ua.sdk.user.role.UserRoleManager;

/* JADX INFO: loaded from: classes65.dex */
public class RoleSuperManagerImpl implements RoleSuperManager {
    private final RoleManager roleManager;
    private final UserPermissionManager userPermissionManager;
    private final UserRoleManager userRoleManager;

    public RoleSuperManagerImpl(RoleManager roleManager, UserPermissionManager userPermissionManager, UserRoleManager userRoleManager) {
        this.roleManager = roleManager;
        this.userPermissionManager = userPermissionManager;
        this.userRoleManager = userRoleManager;
    }

    @Override // com.ua.sdk.role.RoleManager
    public Role fetchRole(Role.Type name) {
        return this.roleManager.fetchRole(name);
    }

    @Override // com.ua.sdk.role.RoleManager
    public EntityList<Role> fetchRoleList() throws UaException {
        return this.roleManager.fetchRoleList();
    }

    @Override // com.ua.sdk.role.RoleManager
    public Request fetchRoleList(FetchCallback<EntityList<Role>> callback) {
        return this.roleManager.fetchRoleList(callback);
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public EntityList<UserPermission> fetchUserPermission(EntityRef ref) throws UaException {
        return this.userPermissionManager.fetchUserPermission(ref);
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public Request fetchUserPermission(EntityRef ref, FetchCallback<EntityList<UserPermission>> callback) {
        return this.userPermissionManager.fetchUserPermission(ref, callback);
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public EntityList<UserPermission> fetchUserPermissionList() throws UaException {
        return this.userPermissionManager.fetchUserPermissionList();
    }

    @Override // com.ua.sdk.user.permission.UserPermissionManager
    public Request fetchUserPermissionList(FetchCallback<EntityList<UserPermission>> callback) {
        return this.userPermissionManager.fetchUserPermissionList(callback);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public UserRole fetchUserRole(EntityRef ref) throws UaException {
        return this.userRoleManager.fetchUserRole(ref);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public Request fetchUserRole(EntityRef ref, FetchCallback<UserRole> callback) {
        return this.userRoleManager.fetchUserRole(ref, callback);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public UserRole createUserRole(UserRole entity) throws UaException {
        return this.userRoleManager.createUserRole(entity);
    }

    @Override // com.ua.sdk.user.role.UserRoleManager
    public Request createUserRole(UserRole entity, CreateCallback<UserRole> callback) {
        return this.userRoleManager.createUserRole(entity, callback);
    }
}
