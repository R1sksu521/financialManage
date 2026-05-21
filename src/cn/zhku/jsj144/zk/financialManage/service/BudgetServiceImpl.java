package cn.zhku.jsj144.zk.financialManage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zhku.jsj144.zk.financialManage.mapper.BudgetMapper;
import cn.zhku.jsj144.zk.financialManage.pojo.Budget;

@Transactional
@Service
public class BudgetServiceImpl implements BudgetService{

	@Autowired
	private BudgetMapper budgetMapper;

	@Override
	public void addBudget(Budget budget) {//添加预算
		budgetMapper.addBudget(budget);
	}

	@Override
	public Budget findBudget(Budget budget) {//查找当前月份是否存在预算
		return budgetMapper.findBudget(budget);
	}

	@Override
	public void editBudget(Budget budget) {//编辑预算
		 budgetMapper.editBudget(budget);
	}

	@Override
	public void deleteBudget(int wid) {//删除预算
		 budgetMapper.deleteBudget(wid);
	}
}
