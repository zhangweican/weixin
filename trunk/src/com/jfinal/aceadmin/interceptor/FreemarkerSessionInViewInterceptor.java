package com.jfinal.aceadmin.interceptor;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.template.ObjectWrapper;

public class FreemarkerSessionInViewInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        ai.invoke();
 
        Controller c = ai.getController();
        HttpSession hs = c.getSession(false);
        if (hs != null) {
            c.setAttr("session", new HttpSessionHashModel(hs, ObjectWrapper.DEFAULT_WRAPPER));
        }
    }
}