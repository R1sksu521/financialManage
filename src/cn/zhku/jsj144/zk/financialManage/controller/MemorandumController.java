package cn.zhku.jsj144.zk.financialManage.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.zhku.jsj144.zk.financialManage.pojo.Memorandum;
import cn.zhku.jsj144.zk.financialManage.pojo.PageBean;
import cn.zhku.jsj144.zk.financialManage.pojo.User;
import cn.zhku.jsj144.zk.financialManage.service.MemorandumService;

@Controller
@RequestMapping("/memorandum")
public class MemorandumController {

	@Autowired
	private MemorandumService memorandumService;

	// 添加备忘
	@RequestMapping("/addMemorandum.action")
	public String addMemorandum(HttpServletRequest request, String editvalue) {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		int uid = user.getUid();

		// 前80个字符作为列表摘要
		String topFont = null;
		if (editvalue.length() > 80) {
			topFont = editvalue.substring(0, 80);
		} else {
			topFont = editvalue;
		}

		// 富文本内容直接存数据库
		String content = request.getParameter("editorValue");

		Date dt = new Date();
		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String recordTime = dFormat.format(dt);

		Memorandum me = new Memorandum();
		me.setRecordTime(recordTime);
		me.setContent(content);
		me.setTopFont(topFont);
		me.setUser_id(uid);

		memorandumService.addMemorandum(me);

		return "redirect:/memorandum/listMemorandum.action";
	}

	// 备忘录列表（分页）
	@RequestMapping("/listMemorandum.action")
	public String listMemorandum(HttpServletRequest request, Integer currentPage, Model model) {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		int uid = user.getUid();

		PageBean<Memorandum> pageBean = memorandumService.listMemorandum(uid, currentPage);
		if (pageBean.getPageList().size() == 0) {
			pageBean.setPageList(null);
		}
		model.addAttribute("pageBean", pageBean);

		return "/jsp/memorandum/memorandum.jsp";
	}

	// 查看/编辑单条备忘
	@RequestMapping("/oneMemorandum.action")
	public String oneMemorandum(HttpServletRequest request, int mid, Model model, Integer currentPage) {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		int uid = user.getUid();

		Memorandum me = new Memorandum();
		me.setUser_id(uid);
		me.setMid(mid);

		Memorandum memorandum = memorandumService.oneMemorandum(me);
		String content = memorandum.getContent();

		model.addAttribute("content", content);
		model.addAttribute("memorandum", memorandum);
		model.addAttribute("currentPage", currentPage);

		return "/jsp/memorandum/editmemorandum.jsp";
	}

	// 编辑备忘
	@RequestMapping("/editMemorandum.action")
	public String editMemorandum(HttpServletRequest request, Memorandum memorandum, String editvalue,
			Model model, Integer currentPage) {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		int uid = user.getUid();

		// 更新前80个字符摘要
		String topFont = null;
		if (editvalue.length() > 80) {
			topFont = editvalue.substring(0, 80);
		} else {
			topFont = editvalue;
		}

		// 新的富文本内容直接更新到数据库
		String content = request.getParameter("editorValue");

		memorandum.setContent(content);
		memorandum.setTopFont(topFont);
		memorandum.setUser_id(uid);

		memorandumService.editMemorandum(memorandum);

		return "redirect:/memorandum/listMemorandum.action?currentPage=" + currentPage;
	}

	// 删除备忘
	@RequestMapping("/deleteMemorandum.action")
	public String deleteMemorandum(HttpServletRequest request, int mid, Integer currentPage) {
		memorandumService.deleteMemorandum(mid);

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "/index.jsp";
		}
		int uid = user.getUid();

		int pageRecord = 6;
		int allPage = 0;
		int allRecord = memorandumService.findMemorandumCount(uid);
		if (allRecord % pageRecord == 0) {
			allPage = allRecord / pageRecord;
		} else {
			allPage = allRecord / pageRecord + 1;
		}

		if (currentPage > allPage - 1) {
			currentPage = currentPage - 1;
		}
		return "redirect:/memorandum/listMemorandum.action?currentPage=" + currentPage;
	}

}