package cn.zhku.jsj144.zk.financialManage.controller;

import java.io.IOException;
import java.io.PrintWriter;
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

	@ModelAttribute
	public void loadCategories(HttpSession session) {
		List<ShouzhiCategory> incomes = shouzhiCategoryService.findShouzhiCategoryByParent("收入");
		session.setAttribute("incomes", incomes);
		List<ShouzhiCategory> spends = shouzhiCategoryService.findShouzhiCategoryByParent("支出");
		session.setAttribute("spends", spends);
	}

	//账单明细 +分页+多条件
	@RequestMapping(value="findShouzhiRecord.action")
	public String findShouzhiRecord(ShouzhiRecord shouzhiRecord,HttpServletRequest request){

		int currentPage=0;
		if(request.getParameter("currentPage")!=null){
			currentPage=Integer.parseInt((String) request.getParameter("currentPage"));
		}
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			return "/index.jsp";
		}

		if(shouzhiRecord!=null){
			if(shouzhiRecord.getSzr_date()!=null && !"".equals(shouzhiRecord.getSzr_date())){
				request.setAttribute("date_condition", shouzhiRecord.getSzr_date());
			}
		}

		PageBean<ShouzhiRecord> pageBean= shouzhiRecordService.findShouzhiRecord(currentPage,user,shouzhiRecord);

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

	@RequestMapping("toAddPage.action")
	public String toAddPage(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		List<ShouzhiCategory> incomes = shouzhiCategoryService.findShouzhiCategoryByParent("收入");
		List<ShouzhiCategory> spends = shouzhiCategoryService.findShouzhiCategoryByParent("支出");
		request.setAttribute("incomes", incomes);
		request.setAttribute("spends", spends);
		return "/jsp/addShouzhi.jsp";
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
