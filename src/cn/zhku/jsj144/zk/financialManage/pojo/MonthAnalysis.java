package cn.zhku.jsj144.zk.financialManage.pojo;

public class MonthAnalysis {/*月份分析*/

	private int incomeRecordCount;//收入记录条数
	private int spendsRecordCount;//支出记录条数
	private double incomeMoney;//收入金额
	private double spendsMoney;//支出金额
	private double allMoney;//总金额
	public int getIncomeRecordCount() {
		return incomeRecordCount;
	}
	public void setIncomeRecordCount(int incomeRecordCount) {
		this.incomeRecordCount = incomeRecordCount;
	}
	public int getSpendsRecordCount() {
		return spendsRecordCount;
	}
	public void setSpendsRecordCount(int spendsRecordCount) {
		this.spendsRecordCount = spendsRecordCount;
	}
	public double getIncomeMoney() {
		return incomeMoney;
	}
	public void setIncomeMoney(double incomeMoney) {
		this.incomeMoney = incomeMoney;
	}
	public double getSpendsMoney() {
		return spendsMoney;
	}
	public void setSpendsMoney(double spendsMoney) {
		this.spendsMoney = spendsMoney;
	}
	public double getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(double allMoney) {
		this.allMoney = allMoney;
	}


}
