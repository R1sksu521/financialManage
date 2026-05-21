package cn.zhku.jsj144.zk.financialManage.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.zhku.jsj144.zk.financialManage.pojo.User;

//登录拦截器
public class LoginInterceptor implements HandlerInterceptor {

	private List<String> exceptUrls;//放行的资源列表
	public List<String> getExceptUrls() {
		return exceptUrls;
	}
	public void setExceptUrls(List<String> exceptUrls) {
		this.exceptUrls = exceptUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// 请求资源路径
		String requestUri = request.getRequestURI();

		if (requestUri.startsWith(request.getContextPath())) {
			requestUri = requestUri.substring(
					request.getContextPath().length(), requestUri.length());
		}

		// 放行exceptUrls中配置的url
		for (String url : exceptUrls) {
			if (url.endsWith("/**")) {
				if (requestUri.startsWith(url.substring(0, url.length() - 3))) {
					return true;
				}
			}
			else if (requestUri.startsWith(url)) {
				return true;
			}
		}

		// 1) 拦截用户请求，判断用户是否登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// 游客已登录：只允许访问新闻相关页面
		Boolean isGuest = (Boolean) session.getAttribute("isGuest");
		if (isGuest != null && isGuest) {
			if (requestUri.startsWith("/news/") || requestUri.startsWith("/user/")) {
				return true;
			}
			response.sendRedirect(request.getContextPath() + "/news/findNewsList.action?currentPage=0");
			return false;
		}

		// 2) 如果用户已经登录，放行
		if (user != null) {
			return true;
		}

		// 3) 如果用户未登录，跳转到登录页面
		response.sendRedirect(request.getContextPath() + "/index.jsp");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
