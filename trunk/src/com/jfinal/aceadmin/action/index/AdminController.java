package com.jfinal.aceadmin.action.index;

import com.jfinal.core.Controller;

public class AdminController extends Controller {

	/**
	 * 加载主页
	 */
	public void index() {
		render("index.html");
	}

}
