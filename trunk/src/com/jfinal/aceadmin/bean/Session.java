package com.jfinal.aceadmin.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 会话
 * @author zhangweican
 *
 */
public class Session {
	//user info
	private String userId = "";
	private String name = "";
	private Set<Integer> groups = new HashSet<Integer>();
	private Set<String> ress = new HashSet<String>();
	private boolean isAdmin = false;

	//other
	//菜单树
	private String resTree = "";
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getResTree() {
		return resTree;
	}

	public void setResTree(String resTree) {
		this.resTree = resTree;
	}

	public Set<Integer> getGroups() {
		return groups;
	}

	public void setGroups(Set<Integer> groups) {
		this.groups = groups;
	}

	public Set<String> getRess() {
		return ress;
	}

	public void setRess(Set<String> ress) {
		this.ress = ress;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	
}
