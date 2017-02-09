package com.zenglb.framework.http.result;

import java.util.List;

/**
 * Created by zenglb on 2016/10/20.
 */

public class Modules {

	@Override
	public String toString() {
		return "Modules{" +
				"data=" + data +
				", is_changed=" + is_changed +
				", last_modified='" + last_modified + '\'' +
				'}';
	}

	/**
	 * is_changed : true
	 * data : [{"url":null,"sort":0,"code":"LBMD10001","name":"知识库","checkout_available":true},{"url":null,"sort":1,"code":"LBMD10002","name":"团队","checkout_available":true},{"url":null,"sort":2,"code":"LBMD10003","name":"消息","checkout_available":false},{"url":null,"sort":3,"code":"LBMD10004","name":"抢单","checkout_available":false},{"url":null,"sort":4,"code":"LBMD10005","name":"任务","checkout_available":false},{"url":null,"sort":5,"code":"LBMD10006","name":"报事","checkout_available":false},{"url":null,"sort":6,"code":"LBMD10009","name":"住户资料","checkout_available":false},{"url":null,"sort":7,"code":"LBMD10010","name":"查询","checkout_available":false},{"url":null,"sort":8,"code":"LBMD10012","name":"访客验证","checkout_available":false},{"url":"http://dev.fm.vankeservice.com:8080/login/index.do","sort":9,"code":"LBMD10013","name":"业务支持","checkout_available":false},{"url":null,"sort":10,"code":"LBMD10015","name":"网格报事","checkout_available":false},{"url":null,"sort":11,"code":"LBMD10016","name":"商业信息","checkout_available":false},{"url":null,"sort":12,"code":"LBMD10017","name":"活动推广","checkout_available":false}]
	 * last_modified : 1476866649
	 */

	private boolean is_changed;
	private String last_modified;
	/**
	 * url : null
	 * sort : 0
	 * code : LBMD10001
	 * name : 知识库
	 * checkout_available : true
	 */

	private List<DataBean> data;

	public boolean isIs_changed() {
		return is_changed;
	}

	public void setIs_changed(boolean is_changed) {
		this.is_changed = is_changed;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private String url;
		private int sort;
		private String code;
		private String name;
		private boolean checkout_available;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getSort() {
			return sort;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isCheckout_available() {
			return checkout_available;
		}

		public void setCheckout_available(boolean checkout_available) {
			this.checkout_available = checkout_available;
		}

		@Override
		public String toString() {
			return "DataBean{" +
					"checkout_available=" + checkout_available +
					", url='" + url + '\'' +
					", sort=" + sort +
					", code='" + code + '\'' +
					", name='" + name + '\'' +
					'}';
		}
	}
}
