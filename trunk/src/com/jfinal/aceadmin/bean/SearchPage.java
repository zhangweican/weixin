package com.jfinal.aceadmin.bean;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.tsc9526.monalisa.core.annotation.Column;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 接收查询的条件和页面参数
 * @author zhangweican
 *
 * @param <T>
 */
public class SearchPage<T> {
	// get the requested page
	private int page   = 1;	
	// get how many rows we want to have into the grid
	private int rows = 10;	
	// get index row - i.e. user click to sort
	private String sidx = null;
	// get the direction
	private String sord = null;
	//查询条件组合
	private T t;
	
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	
	public String getSidx4DB() {
		if(StringUtils.isEmpty(sidx)){
			return sidx;
		}
		List<FGS> fgs = ((Model) t).fields();
		for(FGS f : fgs){
			if(f.getFieldName().equals(sidx)){
				Column c = f.getField().getAnnotation(Column.class);
				return c.name();
			}
		}
		return sidx;
	}
	
	public String toWhere(){
		StringBuffer sb = new StringBuffer("1=1");
		if(t == null || !(t instanceof Model)){
			return sb.toString();
		}
		List<FGS> fgs = ((Model) t).fields();
		for(FGS f : fgs){
			Column c = f.getField().getAnnotation(Column.class);
			String key = c.name();
			Object value = f.getObject(t);
			if(value != null){
				//转义防注入
				String val = value.toString();
				value = StringEscapeUtils.escapeSql(val);
				sb.append(" and `").append(key).append("` like \"%").append(value).append("%\" ");
			}
		}
		//order by 
		if(StringUtils.isNotEmpty(sidx) && StringUtils.isNotEmpty(sord)){
			sb.append(" order by ").append(getSidx4DB()).append(" ").append(sord);
		}
		return sb.toString();
	}
	
}
