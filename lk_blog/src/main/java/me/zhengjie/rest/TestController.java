package me.zhengjie.rest;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.config.HttpConfig;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.*;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = "test")
@RequestMapping("/oauth")
public class TestController {

    @Log("test1")
    @RequestMapping("/render/{source}")
    @ResponseBody
    @AnonymousAccess
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @Log("test2")
    @RequestMapping("/callback/{source}")
    @ResponseBody
    @AnonymousAccess
    public Object login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request) {
        AuthRequest authRequest = getAuthRequest();
        System.out.println(JSONObject.toJSONString(callback));
        return authRequest.login(callback);
    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        System.out.println(source.toLowerCase());
        switch (source.toLowerCase()) {
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("2ef9e8961eaca3d86802")
                        .clientSecret("4f52aebde01730c64d1f1ade55eb8fd55993b929")
                        .redirectUri("http://192.168.123.211:8000/oauth/callback/github")
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.values()))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("2ef9e8961eaca3d86802")
                .clientSecret("4f52aebde01730c64d1f1ade55eb8fd55993b929")
                .redirectUri("http://localhost:8000/oauth/callback/github")
                .build());
    }
}
