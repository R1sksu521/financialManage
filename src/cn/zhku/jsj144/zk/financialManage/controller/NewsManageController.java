package cn.zhku.jsj144.zk.financialManage.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.zhku.jsj144.zk.financialManage.pojo.News;
import cn.zhku.jsj144.zk.financialManage.pojo.PageBean;
import cn.zhku.jsj144.zk.financialManage.pojo.News;
import cn.zhku.jsj144.zk.financialManage.pojo.User;
import cn.zhku.jsj144.zk.financialManage.service.NewsService;
@Controller
@RequestMapping("/newsManage")
public class NewsManageController {

	@Autowired
	private NewsService newsService;

	//新闻列表
	@RequestMapping("/findNewsList.action")
	public String findNewsList(News news,Integer currentPage,Model model){
		model.addAttribute("findNews", news);
		PageBean<News> pageBean=newsService.findNewsList(news,currentPage);
		if(pageBean.getPageList().size()==0){
			pageBean.setPageList(null);
		}
		model.addAttribute("pageBean", pageBean);
		return "/admin/newsManage.jsp";
	}

	//添加新闻
	@RequestMapping("/addNews.action")
	public String addNews(News news, MultipartFile file,String editvalue,HttpServletRequest request) throws IllegalStateException, IOException{
		String nContent=null;
		String realPath = request.getServletContext().getRealPath("/") + "upload/news";
		String uuidName = generateUUIDName();
		String savePath = generateSavePath(realPath, uuidName);
		if(file.getOriginalFilename()!=null&&!"".equals(file.getOriginalFilename())){
			System.out.println("方式二：上传文件");
			String oriName = file.getOriginalFilename();
			String extName = oriName.substring(oriName.lastIndexOf("."));
			nContent=savePath + uuidName + extName;
			file.transferTo(new File(nContent));
		}
		else{
			System.out.println("方式一：上传文件");
			String content = request.getParameter("editorValue");
			nContent = savePath + uuidName + ".txt";
			File fileText = new File(nContent);
			FileWriter fileWriter = new FileWriter(fileText);
			fileWriter.write(content);
			fileWriter.close();
		}
		news.setnContent(nContent);
		news.setVisitCount(0);
		newsService.addNews(news);
		return "redirect:/newsManage/findNewsList.action";
	}


	//编辑新闻页面
	@RequestMapping("/toEditPage.action")
	public String toEditPage(Integer nid,Model model,Integer currentPage){
		News news=newsService.queryNewsById(nid);
		String thingPath = news.getnContent();
		int len = 0;
		StringBuffer str = new StringBuffer("");
		if (thingPath != null && !thingPath.isEmpty()) {
			File file = new File(thingPath);
			if (file.exists()) {
				try {
					FileInputStream is = new FileInputStream(file);
					InputStreamReader isr = new InputStreamReader(is, "UTF-8");
					BufferedReader in = new BufferedReader(isr);
					String line = null;
					while ((line = in.readLine()) != null) {
						if (len != 0) str.append("\n");
						str.append(line);
						len++;
					}
					in.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String content=str.toString();
		model.addAttribute("content",content);
		model.addAttribute("news", news);
		model.addAttribute("currentPage", currentPage);
		return "/admin/news/editnews.jsp";
	}

	//编辑新闻信息
	@RequestMapping("/editNews.action")
	public String editNews(News news,String editvalue,HttpServletRequest request,Integer currentPage2) throws IOException{
		try {
		System.out.println("修改-------------------------");
		System.out.println(news.getAuthor()+"::"+news.getKeyword()+":::"+news.getnTitle()+":::"+news.getRecordTime());
		System.out.println("原路径:"+news.getnContent());

		String content = editvalue;
		System.out.println("编辑器内容长度："+ (content != null ? content.length() : 0) +"字符");

		if (content != null && !content.trim().isEmpty()) {
			String thingPath = news.getnContent();
			// 总是生成新路径（跨平台兼容：Windows路径在Linux上无效）
			String realPath = request.getServletContext().getRealPath("/") + "upload/news";
			String uuidName = generateUUIDName();
			String savePath = generateSavePath(realPath, uuidName);
			thingPath = savePath + uuidName + ".txt";
			news.setnContent(thingPath);
			System.out.println("新路径:"+thingPath);
			// 写入文件
			File fileText = new File(thingPath);
			fileText.getParentFile().mkdirs();
			FileWriter fileWriter = new FileWriter(fileText);
			fileWriter.write(content);
			fileWriter.close();
			System.out.println("文件写入成功");
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("编辑新闻异常: " + e.getClass().getName() + " - " + e.getMessage());
		}

		// 即使文件写入失败，也更新数据库
		newsService.editNews(news);
		int page = (currentPage2 != null) ? currentPage2 : 0;
		return "redirect:/newsManage/findNewsList.action?currentPage="+page;
	}

	//删除新闻信息
	@RequestMapping("/deleteNews.action")
	public String deleteNews(int  nid,Integer currentPage2){
		newsService.deleteNews(nid);
		int pageRecord=10;
		int count=newsService.countNews();
		int allPage=0;
		if(count%pageRecord==0){
			allPage=count/pageRecord;
		}
		else{
			allPage=count/pageRecord+1;
		}
		allPage=allPage-1;
		if(currentPage2>allPage){
			currentPage2=currentPage2-1;
		}
		return "redirect:/newsManage/findNewsList.action?currentPage="+currentPage2;
	}

	//新闻详情页

	// 生成唯一的文件名
	private static String generateUUIDName() {
		return UUID.randomUUID().toString();
	}

	// 生成随机文件夹
	private static String generateSavePath(String realPath, String filename) {
		int hashCode = filename.hashCode();
		int first = hashCode & (0xf);
		int second = (hashCode >> 4) & (0xf);
		String savePath = realPath + "/" + first + "/" + second + "/";
		File f = new File(savePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return savePath;
	}
}
