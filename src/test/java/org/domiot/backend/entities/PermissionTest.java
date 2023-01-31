package org.domiot.backend.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.domiot.backend.entities.Permission;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class PermissionTest
{

    @Test
    public void test() {
        Permission permission = new Permission("admin");
        assertThat(permission.getPermissionFlags(), is(0x0000));
        assertThat(permission.isControlPermissionSet(), is(false));
        permission.setControlPermission(true);
        assertThat(permission.getPermissionFlags(), CoreMatchers.is(Permission.ROLE_PERMISSION_CONTROL_FLAG));
        assertThat(permission.isDeletePermissionSet(), is(false));
        assertThat(permission.isControlPermissionSet(), is(true));
        assertThat(permission.isModifyPermissionSet(), is(false));
        assertThat(permission.isReadPermissionSet(), is(false));
        assertThat(permission.isWritePermissionSet(), is(false));
        permission.setControlPermission(false);
        assertThat(permission.getPermissionFlags(), is(0x0000));
        permission.setDeletePermission(true);
        permission.setWritePermission(true);
        assertThat(permission.isDeletePermissionSet(), is(true));
        assertThat(permission.isControlPermissionSet(), is(false));
        assertThat(permission.isModifyPermissionSet(), is(false));
        assertThat(permission.isReadPermissionSet(), is(false));
        assertThat(permission.isWritePermissionSet(), is(true));
        assertThat(permission.getPermissionFlags(), CoreMatchers.is(Permission.ROLE_PERMISSION_DELETE_FLAG | Permission.ROLE_PERMISSION_WRITE_FLAG));
    }

}
