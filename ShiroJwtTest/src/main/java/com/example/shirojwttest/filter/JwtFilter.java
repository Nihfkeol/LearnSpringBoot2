package com.example.shirojwttest.filter;

import com.example.shirojwttest.model.ResultMap;
import com.example.shirojwttest.shiro.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    String message;

    /**
     * 过滤器拦截请求的入口方法
     * 返回 true 则允许访问
     * 返回false 则禁止访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info(getClass().getName()+" ======> isAccessAllowed");
        boolean allowed = false;
        if (isLoginAttempt(request,response)){
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                allowed = executeLogin(request,response);
            } catch (Exception e) {
                log.info(e.getMessage());
                message = e.getMessage();
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return allowed;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info(getClass().getName()+" ======> onAccessDenied");
        ResultMap map = new ResultMap();
        map.fail().code(401).message(message);
        ObjectMapper mapper = new ObjectMapper();
        String string = mapper.writeValueAsString(map);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(string);
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.info(getClass().getName()+" ======> isLoginAttempt");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info(getClass().getName()+" ======> executeLogin");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request,response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
}
