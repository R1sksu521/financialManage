package cn.zhku.jsj144.zk.financialManage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.zhku.jsj144.zk.financialManage.pojo.Admin;
import cn.zhku.jsj144.zk.financialManage.pojo.User;
import cn.zhku.jsj144.zk.financialManage.service.AdminService;
import cn.zhku.jsj144.zk.financialManage.service.ShouzhiCategoryService;
import cn.zhku.jsj144.zk.financialManage.service.ShouzhiRecordService;
import cn.zhku.jsj144.zk.financialManage.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ShouzhiCategoryService shouzhiCategoryService;

	//用户登录
	@RequestMapping("login.action")
	public String login(User user,HttpServletRequest request){
		System.out.println("用户输入的用户名和密码："+user.getUsername()+"::"+user.getPassword());
		User findUser=userService.queryUserByUser(user);

		if(findUser!=null){
			System.out.println("查找到的用户名和密码："+findUser.getUsername()+"::"+findUser.getPassword());
		}
		System.out.println("");

		if(findUser!=null){
			HttpSession session = request.getSession();
			session.removeAttribute("isGuest");
				session.setAttribute("user", findUser);
			// check if user is admin
			Admin admin = new Admin();
			admin.setAdminname(findUser.getUsername());
			admin.setPassword(findUser.getPassword());
			Admin findAdmin = adminService.findAdmin(admin);
			if (findAdmin != null) {
				session.setAttribute("isAdmin", true);
				session.setAttribute("admin", findAdmin);
			}

			return "redirect:/shouzhiRecord/findShouzhiRecord.action";
		}

		//登录失败
		String msg="\u7528\u6237\u540D\u6216\u8005\u5BC6\u7801\u8F93\u5165\u9519\u8BEF\uFF0C\u8BF7\u91CD\u65B0\u8F93\u5165";
		request.setAttribute("msg", msg);
		return "/index.jsp";
	}

	//通过用户名判断用户是否存在
	@RequestMapping(value="findUserByNameAndAjax.action",method=RequestMethod.POST)
	public @ResponseBody String findUserByNameAndAjax(@RequestBody User user){
		User findUser=userService.queryUserByUsername(user.getUsername());
		if(findUser!=null){
			System.out.println("{\"name\":\"exit\"}");
			return "{\"name\":\"exit\"}";
		}
		else{
			System.out.println("{\"name\":\"notexit\"}");
			return "{\"name\":\"notexit\"}";
		}
	}

	//修改密码（忘记密码）
	@RequestMapping("updatePasswordByUsername.action")
	public  String updatePasswordByUsername(User user,HttpServletRequest request){
		userService.updatePasswordByUsername(user);
		request.setAttribute("msg", "\u5BC6\u7801\u4FEE\u6539\u6210\u529F\uFF0C\u8BF7\u767B\u5F55");
		return "/index.jsp";
	}

	//用户注册（普通用户）
	@RequestMapping("regist.action")
	public String regist(User user,String repassword,HttpServletRequest request){
		User findUser=userService.queryUserByUsername(user.getUsername());
		if(findUser!=null){
			request.setAttribute("msg", "\u5F53\u524D\u7528\u6237\u5DF2\u7ECF\u5B58\u5728\uFF0C\u8BF7\u91CD\u65B0\u8F93\u5165\u7528\u6237\u540D");
			request.setAttribute("user", user);
			request.setAttribute("repassword", repassword);
			return"/regist.jsp";
		}

		userService.insertUser(user);
		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		return "redirect:/shouzhiRecord/findShouzhiRecord.action";
	}


	//去用户信息修改页面
	@RequestMapping("/toUserSetting.action")
	public String toUserSetting(HttpServletRequest request){
		User user=(User) request.getSession().getAttribute("user");
		request.setAttribute("user", user);
		return "/jsp/userSetting.jsp";
	}


	//用户信息修改
	@RequestMapping("/editUser.action")
	public String editUser(HttpServletRequest request,User user,String oldusername){

		String username = user.getUsername();
		System.out.println("当前用户名"+username);
		System.out.println("之前的用户名"+oldusername);
		if(username!=null&&!"".equals(username)){
			System.out.println("用户名不为空");

			if(!username.equals(oldusername)){
				User findUser = userService.queryUserByUsername(username);
				if(findUser==null){
					System.out.println("用户名不存在，更新用户信息");
					userService.editUser(user);
					request.getSession().setAttribute("user", user);
					return  "redirect:/shouzhiRecord/findShouzhiRecord.action";
				}
				else{
					System.out.println("新用户名已存在");
					user.setUsername(findUser.getUsername());
					request.setAttribute("user", user);
					request.setAttribute("msg", "\u8BE5\u7528\u6237\u5DF2\u5B58\u5728\uFF0C\u8BF7\u4FEE\u6539\u5F53\u524D\u7528\u6237\u540D");
					return  "/jsp/userSetting.jsp";
				}
			}
			else{
				System.out.println("是当前用户");
				userService.editUser(user);
				request.getSession().setAttribute("user", user);
				return  "redirect:/shouzhiRecord/findShouzhiRecord.action";
			}
		}
		else{
			System.out.println("用户名为空");
			request.setAttribute("user", user);
			request.setAttribute("msg", "\u7528\u6237\u540D\u4E0D\u80FD\u4E3A\u7A7A");
			return  "/jsp/userSetting.jsp";
		}
	}

	//\u6E38\u5BA2访问登录
	@RequestMapping("guestLogin.action")
	public String guestLogin(HttpServletRequest request){
		HttpSession session = request.getSession();
		User guestUser = new User();
		guestUser.setUsername("\u6E38\u5BA2");
		guestUser.setUid(0);
		session.setAttribute("user", guestUser);
		session.setAttribute("isGuest", true);
		return "redirect:/news/findNewsList.action?currentPage=0";
	}

	//用户退出登录
	@RequestMapping("/logout.action")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("isGuest");
		request.getSession().removeAttribute("isAdmin");
		return "/index.jsp";
	}

}
