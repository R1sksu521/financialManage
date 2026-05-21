package cn.zhku.jsj144.zk.financialManage.mapper;

import cn.zhku.jsj144.zk.financialManage.pojo.Budget;

public interface BudgetMapper {

	//添加预算
	void addBudget(Budget budget);

	//查找当前月份是否存在预算
	Budget findBudget(Budget budget);

	//编辑预算
	void editBudget(Budget budget);

	//删除预算
	void deleteBudget(int wid);

}
