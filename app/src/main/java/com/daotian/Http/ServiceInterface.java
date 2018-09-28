package com.daotian.Http;

public enum ServiceInterface {
	
	getHomeList("Cp","getLotteryList","","首页列表",""),
	getOpenList("Cp","getLotteryListTwo","","开奖列表",""),
	getOpenListDetail("Cp","get_prize_list","","开奖列表",""),
	getInit("Cp","buyInit","","选号页面",""),
	getOrderInit("Cp","orderInit","","订单确认初始化",""),
	getCenter("Merchant","center","","个人中心初始化",""),
	getSetting("Cp","getSetting","","获取配置说明",""),
	getPersonalInit("Cp","orderInit","","数据初始化",""),
	myOrder("Cp","myOrder","","我的订单",""),
	myOrderDetail("Cp","orderDetail","","普通订单详情",""),
	myChildOrderDetail("Cp","childOrderDetail","","子订单详情",""),
	myZqOrderDetail("Cp","orderDetailZq","","我的订单详情（追期）",""),
	buyOrder("Cp","buy","","购买",""),
	Login("Merchant","login","","登录",""),
	Register("Merchant","register","","注册",""),
	SaveInfo("Merchant","SaveInfo","","修改密码",""),
	sendMsg("Merchant","sendMsg","","发送验证码",""),
	Out("Merchant","Out","","注销",""),
	getChildList("Merchant","getChildList","","获取子账号",""),
	giveChildMoney("Merchant","giveMoney","","给子账户打钱",""),
	getMemberFlow("Merchant","get_member_flow","","获取个人流水",""),
	cancleOrder("Cp","cancle_order","","撤单",""),
	;

	
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	private String model;
	private String action;
	private String version;
	private String desc;
	private String param;

	/**
	 * ��Ĺ��췽��
	 */
	private ServiceInterface(String model, String action, String version,String desc,String param) {
		this.model = model;
		this.action = action;
		this.version = version;
		this.desc = desc;
		this.param = param;
	}

	
}
