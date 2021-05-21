package com.example.shirojwttest.shiro;

import com.example.shirojwttest.Utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info(getClass().getName()+" ======> doGetAuthorizationInfo");
        return null;
    }

    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info(getClass().getName()+" ======> 执行了认证");
        //密码认证shiro自己做
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = jwtToken.getCredentials().toString();
        try {
            JwtUtil.verify(token);
        }catch (MalformedJwtException e) {
            throw new AccountException("token异常");
        } catch (SignatureException e) {
            throw new AccountException("签名异常");
        } catch (ExpiredJwtException e) {
            throw new AccountException("token过期");
        }
        /*
        principal:当前用户的认证
        credentials:传递密码的对象
        realmName:认证名
         */
        return new SimpleAuthenticationInfo(token, token, "token");
    }
}
