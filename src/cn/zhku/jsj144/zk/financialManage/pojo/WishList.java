package cn.zhku.jsj144.zk.financialManage.pojo;

public class WishList {
	private Integer id;
	private String wid;
	private String wish;
	private Double wnum;
	private String wdate;
	private String state;
	private Integer user_id; // 对应数据库中的 user_id

	// Getter 和 Setter 方法
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public String getWid() { return wid; }
	public void setWid(String wid) { this.wid = wid; }

	public String getWish() { return wish; }
	public void setWish(String wish) { this.wish = wish; }

	public Double getWnum() { return wnum; }
	public void setWnum(Double wnum) { this.wnum = wnum; }

	public String getWdate() { return wdate; }
	public void setWdate(String wdate) { this.wdate = wdate; }

	public String getState() { return state; }
	public void setState(String state) { this.state = state; }

	public Integer getUser_id() { return user_id; }
	public void setUser_id(Integer user_id) { this.user_id = user_id; }
}