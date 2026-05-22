package cn.zhku.jsj144.zk.financialManage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.zhku.jsj144.zk.financialManage.pojo.News;
import cn.zhku.jsj144.zk.financialManage.pojo.PageBean;
import cn.zhku.jsj144.zk.financialManage.pojo.ShouzhiCategory;
import cn.zhku.jsj144.zk.financialManage.pojo.ShouzhiRecord;
import cn.zhku.jsj144.zk.financialManage.pojo.User;
import cn.zhku.jsj144.zk.financialManage.service.NewsService;
import cn.zhku.jsj144.zk.financialManage.service.ShouzhiCategoryService;
import cn.zhku.jsj144.zk.financialManage.service.ShouzhiRecordService;

@Controller
@RequestMapping("/shouzhiRecord")
public class ShouzhiRecordController {

	@Autowired
	private ShouzhiRecordService shouzhiRecordService;

	@Autowired
	private ShouzhiCategoryService shouzhiCategoryService;

	@Autowired
	private NewsService newsService;

	// ====================== 修复点 1：全局预加载，所有页面都能用 ======================
	@ModelAttribute
	public void loadCategories(HttpSession session) {
		// 每次请求前自动加载，存入session，添加页面也能正常获取
		List<ShouzhiCategory> incomes = shouzhiCategoryService.findShouzhiCategoryByParent("收入");
		session.setAttribute("incomes", incomes);
		List<ShouzhiCategory> spends = shouzhiCategoryService.findShouzhiCategoryByParent("支出");
		session.setAttribute("spends", spends);
	}

	//账单明细 +分页+多条件
	@RequestMapping(value="findShouzhiRecord.action")
	public String findShouzhiRecord(ShouzhiRecord shouzhiRecord,HttpServletRequest request) throws UnsupportedEncodingException{

		int currentPage=0;
		if(request.getParameter("currentPage")!=null){
			currentPage=Integer.parseInt((String) request.getParameter("currentPage"));
		}
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			return "/index.jsp";
		}

		if(shouzhiRecord!=null){
			if(shouzhiRecord.getSzr_date()!=null){
				request.setAttribute("date_condition", shouzhiRecord.getSzr_date());
			}
			if(shouzhiRecord.getSzr_comment()!=null){
				String com=new String((shouzhiRecord.getSzr_comment()).getBytes("ISO-8859-1"),"utf-8");
				request.setAttribute("comment_condition", com);
				shouzhiRecord.setSzr_comment(com);
			}
		}

		PageBean<ShouzhiRecord> pageBean= shouzhiRecordService.findShouzhiRecord(currentPage,user,shouzhiRecord);

		// 直接从handler设置收支类型到session，不单靠@ModelAttribute
		List<ShouzhiCategory> incomes = shouzhiCategoryService.findShouzhiCategoryByParent("收入");
		List<ShouzhiCategory> spends = shouzhiCategoryService.findShouzhiCategoryByParent("支出");
		request.getSession().setAttribute("incomes", incomes);
		request.getSession().setAttribute("spends", spends);

		List<News> newsList=newsService.findNewsEightList();
		request.setAttribute("newsList", newsList);

		if(pageBean.getPageList().size()==0){
			pageBean.setPageList(null);
		}
		request.setAttribute("pageBean", pageBean);
		return "/jsp/main.jsp";
	}

	//ajax去编辑页面回显
	@RequestMapping("toEdit.action")
	@ResponseBody
	public String toEdit(Long id,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			return "/index.jsp";
		}
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("uid", user.getUid());
		map.put("szrid", id.intValue());
		ShouzhiRecord shouzhiRecord=shouzhiRecordService.findShouzhiRecordById(map);
		String parent_category = shouzhiRecord.getShouzhiCategory().getParent_category();
		List<String> son=shouzhiCategoryService.findSonCategoryByParent(parent_category);

		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("shouzhiRecord", shouzhiRecord);
		jsonMap.put("son", son);
		String jsonString = JSON.toJSONString(jsonMap);
		return jsonString;
	}

	//修改
	@RequestMapping("edit.action")
	@ResponseBody
	public String editShouzhiRecord(ShouzhiRecord shouzhiRecord,HttpServletRequest request){
		User user=(User)request.getSession().getAttribute("user");
		if(user==null){
			return "/index.jsp";
		}
		shouzhiRecord.setUser_id(user.getUid());
		ShouzhiCategory shouzhiCategory=shouzhiCategoryService.findCategoryBySonCategory(shouzhiRecord.getShouzhiCategory().getSon_category());
		shouzhiRecord.setShouzhiCategory(shouzhiCategory);

		if("收入".equals(shouzhiCategory.getParent_category())){
			double num=shouzhiRecord.getSzr_num();
			if(num<0){
				shouzhiRecord.setSzr_num(-num);
			}
		}else{
			double num=shouzhiRecord.getSzr_num();
			if(num>0){
				shouzhiRecord.setSzr_num(-num);
			}
		}

		shouzhiRecordService.editShouzhiRecord(shouzhiRecord);
		return "OK";
	}

	//删除一条
	@RequestMapping("deleteOne.action")
	@ResponseBody
	public String deleteOneShouzhiRecord(int id){
		shouzhiRecordService.deleteOneShouzhiRecord(id);
		return "OK";
	}

	//批量删除
	@RequestMapping("deleteBatch.action")
	@ResponseBody
	public String deleteBatchShouzhiRecord(String id){
		shouzhiRecordService.deleteBatchShouzhiRecord(id);
		return "OK";
	}

	// ====================== 修复点 2：添加前先加载数据 ======================
	@RequestMapping("toAddPage.action")
	public String toAddPage(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		// 跳转到添加页面时，强制把分类放入request，确保前端一定能拿到
		List<ShouzhiCategory> incomes = shouzhiCategoryService.findShouzhiCategoryByParent("收入");
		List<ShouzhiCategory> spends = shouzhiCategoryService.findShouzhiCategoryByParent("支出");
		request.setAttribute("incomes", incomes);
		request.setAttribute("spends", spends);
		return "/jsp/addShouzhi.jsp"; // 你自己的添加页面路径
	}

	//添加收支记录
	@RequestMapping("addShouzhiRecord.action")
	public String addShouzhiRecord(ShouzhiRecord shouzhiRecord,HttpServletRequest request) throws IOException{

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "redirect:/index.jsp";
		}
		shouzhiRecord.setUser_id(user.getUid());

		if (shouzhiRecord.getShouzhiCategory() == null) {
			request.getSession().setAttribute("msg", "请先选择收入/支出类型，若无可选项请先添加类型");
			return "redirect:/shouzhiRecord/findShouzhiRecord.action";
		}

		int szcid = shouzhiRecord.getShouzhiCategory().getSzcid();
		String cat=shouzhiRecordService.findParentCategoryById(szcid);

		if("支出".equals(cat)){
			double num=shouzhiRecord.getSzr_num();
			if(num>=0){
				shouzhiRecord.setSzr_num(-num);
			}
		}else{
			double num=shouzhiRecord.getSzr_num();
			if(num<=0){
				shouzhiRecord.setSzr_num(-num);
			}
		}

		shouzhiRecordService.addShouzhiRecord(shouzhiRecord);
		return "redirect:/shouzhiRecord/findShouzhiRecord.action";
	}
}