package com.xdkj.admin.components.shiro;

import com.xdkj.admin.system.service.SecurityService;
import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.common.model.sysFunction.bean.SysFunction;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysRole.bean.SysRole;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    SysManagerService sysManagerService;

    @Autowired
    SecurityService securityService;

    /**
     * @Description 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * @auther: zhangkele
     * @UpadteDate: 2019/3/1 9:48
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String userName = token.getUsername();
        String password = String.valueOf(token.getPassword());
        SysManager sysManager = sysManagerService.getSysManagerByCode(userName);
        if (sysManager != null) {
            // 用户为禁用状态
            if ("1".equals(sysManager.getStatus())) {
                throw new DisabledAccountException();
            }
            // 用户被锁定
            if ("2".equals(sysManager.getStatus())) {
                throw new LockedAccountException();
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    sysManager,
                    sysManager.getPassword(),
                    getName()
            );
            return authenticationInfo;
        }
        throw new UnknownAccountException();
    }


    /**
     * @Description 授权
     * @auther: zhangkele
     * @UpadteDate: 2019/3/1 9:48
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object principal = principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (principal instanceof SysManager) {
            SysManager sysManager = (SysManager) principal;
            List<SysRole> roleList = securityService.listRoleByManagerId(sysManager.getId());
            List<String> roles = new ArrayList<String>();
            for (SysRole sysRole : roleList) {
                roles.add(sysRole.getName());
            }
            authorizationInfo.addRoles(roles);

            List<SysFunction> functionList = securityService.listSysFunctionByManagerId(sysManager.getId());
            List<String> permissions = new ArrayList<String>();
            for (SysFunction sysFunction : functionList) {
                permissions.add(sysFunction.getCode());
            }
            authorizationInfo.addStringPermissions(permissions);
        }
        return authorizationInfo;
    }

}