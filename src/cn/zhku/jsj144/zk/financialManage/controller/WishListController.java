package cn.zhku.jsj144.zk.financialManage.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.zhku.jsj144.zk.financialManage.pojo.PageBean;
import cn.zhku.jsj144.zk.financialManage.pojo.User;
import cn.zhku.jsj144.zk.financialManage.pojo.WishList;
import cn.zhku.jsj144.zk.financialManage.service.WishListService;

@Controller
@RequestMapping("/wishlist")   // 访问路径是  /wishlist/xxx
public class WishListController {

	// 注入WishListService
	@Autowired
	private WishListService wishListService;


	// 查询所有\u5FC3\u613F\u5355
	@RequestMapping("/findAllWishList.action")
	public String findAllWishList(HttpServletRequest request,Integer currentPage,Model model){// 分页查询currentPage
		User user=(User) request.getSession().getAttribute("user");

		PageBean<WishList> pageBean=wishListService.findAllWishList(user.getUid(),currentPage);

		if(pageBean.getPageList().size()==0){// 确保为空
			pageBean.setPageList(null);
		}
		model.addAttribute("pageBean", pageBean);// 分页查询结果

		return "/jsp/wishlist.jsp";
	}

	// 添加\u5FC3\u613F\u5355
	@RequestMapping("/addWish.action")
	@ResponseBody
	public String addWish(HttpServletRequest request, WishList wishList){
		// 核心修复：获取当前登录用户并设置用户ID，使用正确的方法名 setUser_id
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			wishList.setUser_id(user.getUid());
		}

		// 将 datetime-local 格式转换为 yyyy-MM-dd HH:mm:ss
		String currentTime = formatDateTime(wishList.getWdate());
		String wid=null;// \u5FC3\u613F\u5355id
		// 获取某人，某天的\u5FC3\u613F\u5355个数
		wishList.setWdate(currentTime);
		int count=wishListService.CountWishByTimeAndId(wishList);
		count=count+1;
		if(count<10){
			wid="\u5FC3\u613F\u5355"+currentTime+"-0"+count;
		}
		else{
			wid="\u5FC3\u613F\u5355"+currentTime+"-"+count;
		}
		String state="\u672A\u5B8C\u6210";
		wishList.setWid(wid);
		wishList.setState(state);

		wishListService.addWish(wishList);// 添加\u5FC3\u613F\u5355
		return "OK";
	}

	// 去\u5FC3\u613F\u5355页面，根据id查询\u5FC3\u613F\u5355信息
	@RequestMapping("/toEdit.action")
	@ResponseBody
	public String toEdit(Long id){
		WishList findWishlist=wishListService.findWishById(id.intValue());
		String jsonString=JSON.toJSONString(findWishlist);
		System.out.println("jsonString:"+jsonString);
		return jsonString;
	}

	// 编辑\u5FC3\u613F\u5355
	@RequestMapping("/editWish.action")
	@ResponseBody
	public void editWish(WishList wishList){
		// 将 datetime-local 格式转换为 yyyy-MM-dd HH:mm:ss
		String currentTime = formatDateTime(wishList.getWdate());
		wishList.setWdate(currentTime);
		String wid=null;// \u5FC3\u613F\u5355id
		int count=wishListService.CountWishByTimeAndId(wishList);
		count=count+1;
		if(count<10){
			wid="\u5FC3\u613F\u5355"+currentTime+"-0"+count;
		}
		else{
			wid="\u5FC3\u613F\u5355"+currentTime+"-"+count;
		}
		wishList.setWid(wid);
		wishListService.editWish(wishList);
		return;
	}

	// 删除\u5FC3\u613F\u5355
	@RequestMapping("/deleteWish.action")
	@ResponseBody
	public void deleteWish(int id){
		wishListService.deleteWish(id);
		return;
	}

	// 将 HTML5 datetime-local 格式 (yyyy-MM-ddTHH:mm) 转换为标准格式 (yyyy-MM-dd HH:mm:ss)
	private String formatDateTime(String dateTimeStr) {
		if (dateTimeStr == null || dateTimeStr.isEmpty()) {
			return dateTimeStr;
		}
		try {
			// datetime-local 输入格式: yyyy-MM-ddTHH:mm 或 yyyy-MM-ddTHH:mm:ss
			String pattern = dateTimeStr.contains("T") ? "yyyy-MM-dd'T'HH:mm" : "yyyy-MM-dd HH:mm:ss";
			if (dateTimeStr.length() > 16) {
				pattern = "yyyy-MM-dd'T'HH:mm:ss";
			}
			DateFormat inputFormat = new SimpleDateFormat(pattern);
			Date date = inputFormat.parse(dateTimeStr);
			DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return outputFormat.format(date);
		} catch (Exception e) {
			return dateTimeStr;
		}
	}

}