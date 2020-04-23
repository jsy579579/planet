package com.ysj.zuulplanet.filters.pre;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class AuthenticationHeaderFilter extends ZuulFilter {
	private static Logger log = LoggerFactory.getLogger(AuthenticationHeaderFilter.class);


	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		//return false 特定的路由可以不用验证;
		//思路：request.getRequestURI().toString().toLowerCase();可以通过请求url判断
		return true;
	}

	@Override
	public Object run() {
		log.info("AuthenticationHeaderFilter过滤器进入");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		//尝试获取请求头中的token
		String token = request.getHeader("token");
		//如果token参数值为空则进入此逻辑
		if (!StringUtils.isNotEmpty(token)) {
			//对该请求禁止路由，也就是禁止访问下游服务
			ctx.setSendZuulResponse(false);
			//设定responseBody供PostFilter使用
			ctx.setResponseBody("{\"status\":500,\"message\":\"token参数为空！\"}");
			//logic-is-success保存于上下文，作为同类型下游Filter的执行开关
			ctx.set("logic-is-success", false);
			//到这里此Filter逻辑结束
			return null;
		}
		//设置避免报空
		ctx.set("logic-is-success", true);
		return null;
	}
}
