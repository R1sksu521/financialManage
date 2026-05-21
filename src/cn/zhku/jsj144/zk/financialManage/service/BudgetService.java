package cn.zhku.jsj144.zk.financialManage.service;

import cn.zhku.jsj144.zk.financialManage.pojo.Budget;

public interface BudgetService {

	void addBudget(Budget budget);//添加预算

	Budget findBudget(Budget budget);//查找当前月份是否存在预算

	void editBudget(Budget budget);//编辑预算

	void deleteBudget(int wid);//删除预算

}
