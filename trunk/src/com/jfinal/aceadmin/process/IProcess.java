package com.jfinal.aceadmin.process;

import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;

public interface IProcess {
	public WeixinOutput process(WeixinInput e);
}
