package com.wufu.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wufu
 */
@Component
public class PreLogFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 指定过滤器类型：pre、routing、post、error 四种
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * true: 启动过滤器
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器具体逻辑
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String host = request.getRemoteHost();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        logger.info("host:{}, method:{}, the uri:{}", host, method, uri);
        return null;
    }
}
