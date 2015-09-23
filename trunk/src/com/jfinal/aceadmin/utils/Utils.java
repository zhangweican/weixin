package com.jfinal.aceadmin.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.jfinal.aceadmin.bean.SearchPage;
import com.jfinal.log.Logger;
import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class.getName());
	public static String encryptPassword(String password,String key){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
        	if(StringUtils.isNotEmpty(key)){
        		password += key;
        	}
            byte[] btInput = password.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	/**
	 * 对象拷贝
	 * @param dest
	 * @param orgi
	 */
	public static void copyBean(Model dest,Model orgi){
		List<FGS> ofgs = orgi.fields();
		List<FGS> dfgs = dest.fields();
		for(FGS f : ofgs){
			int p = dfgs.indexOf(f);
			if(p != -1){
				Object value = f.getObject(orgi);
				if(value != null){
					try {
						String fieldType = f.getGetMethod().getReturnType().getName();
						FGS df = dfgs.get(p);
						df.getSetMethod().invoke(dest, value);
						
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				}
			}
		}
	}
	/**
	 * 获取查询对应的条件和page信息
	 * @param model
	 * @param request
	 * @return
	 */
	public static SearchPage getSearchPage(Model model,HttpServletRequest request){
		requestToBean(model, request);
		SearchPage sb = new SearchPage();
		String page = request.getParameter("page");// get the requested page
		String rows = request.getParameter("rows");// get how many rows we want to have into the grid
		String sidx = request.getParameter("sidx");// get index row - i.e. user click to sort
		String sord = request.getParameter("sord"); // get the direction
		String orderby = "";
		String where = "1 = 1";
		if(sidx != null && sord != null && sidx != "" && sord != ""){
			orderby = " order by " + sidx + " " + sord;
		}
		if(page == null || "".equals(page) || "0".equals(page)){
			page = "1";
		}
		if(rows == null || "".equals(rows)){
			rows = "10";
		}
		sb.setPage(Integer.parseInt(page));
		sb.setRows(Integer.parseInt(rows));
		sb.setSidx(sidx);
		sb.setSord(sord);
		sb.setT(model);
		return sb;
	}
	
	/**
	 * request参数赋值Model对象
	 */
	public static void requestToBean(Model bean, HttpServletRequest request){
		List<FGS> fgs = bean.fields();
		for(FGS f : fgs){
			String value = request.getParameter(f.getFieldName());
			if(StringUtils.isNotEmpty(value)){
				try {
					String fieldType = f.getGetMethod().getReturnType().getName();
					if(String.class.getName().equals(fieldType)){
						f.getSetMethod().invoke(bean, (String)value);
					}else if(Integer.class.getName().equals(fieldType) && NumberUtils.isDigits((String)value)){
						f.getSetMethod().invoke(bean, Integer.valueOf((String)value));
					}else if(Short.class.getName().equals(fieldType) && NumberUtils.isDigits((String)value)){
						f.getSetMethod().invoke(bean, Short.valueOf((String)value));
					}else if(Float.class.getName().equals(fieldType) && NumberUtils.isNumber((String)value)){
						f.getSetMethod().invoke(bean, Float.valueOf((String)value));
					}else if(Double.class.getName().equals(fieldType) && NumberUtils.isNumber((String)value)){
						f.getSetMethod().invoke(bean, Double.valueOf((String)value));
					}else if(Date.class.getName().equals(fieldType)){
						f.getSetMethod().invoke(bean, DateUtils.parseDate((String)value, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));
					}else{
						f.getSetMethod().invoke(bean, value);
					}
					//f.setAttribute(bean, v);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
	public static String tree2Json(List models,String parentFieldName){
		String json = "";
		if(models != null && models.size() > 0){
			int j = 1;
			for(Object o : models){
				if(o instanceof Model){
					Model m = (Model) o;
					String one = "{";
					boolean isLeaf = true;
					for(int i = 0;i < m.fields().size();i ++){
						FGS fgs = (FGS) m.fields().get(i);
						String name = fgs.getFieldName();
						Object value = "";
						try {
							value = fgs.getObject(o);
						} catch (Exception e) {
						} 
						if(name.equals(parentFieldName) && value == null){
							isLeaf = false;
						}
						//空值处理
						if(value == null){
							one += "\"" + name + "\":\"\",";
						}else{
							one += "\"" + name + "\":\"" + value + "\",";
						}
					}
					//追加isLeaf字段     追加expanded字段
					if(isLeaf){
						one += "\"isLeaf\":\"true\",";
						one += "\"expanded\":\"false\",";
					}
					else{
						one += "\"isLeaf\":\"false\",";
						one += "\"expanded\":\"true\",";
					}
					one += "\"level\":\"" + (j ++) + "\",";
					one += "\"loaded\":\"true\",";
					if(one.endsWith(",")){
						one = one.substring(0, one.length() - 1);
					}
					one += "},";
					json += one;
				}
			}
			if(json.endsWith(",")){
				json = json.substring(0, json.length() - 1);
			}
		}
		return "{\"data\":[" + json + "]}";
	}
	
	/**
	 * 后台Model转化为前台可以识别的json对象
	 * @param p
	 * @return
	 */
	public static String model2Json(List models){
		String json = "";
		if(models != null && models.size() > 0){
			for(Object o : models){
				if(o instanceof Model){
					Model m = (Model) o;
					String one = "{";
					for(int i = 0;i < m.fields().size();i ++){
						FGS fgs = (FGS) m.fields().get(i);
						String name = fgs.getFieldName();
						Object value = "";
						try {
							value = fgs.getObject(o);
						} catch (Exception e) {
						} 
						//空值处理
						if(value == null){
							one += "\"" + name + "\":\"\",";
						}else{
							one += "\"" + name + "\":\"" + value + "\",";
						}
					}
					if(one.endsWith(",")){
						one = one.substring(0, one.length() - 1);
					}
					one += "},";
					json += one;
				}
			}
			if(json.endsWith(",")){
				json = json.substring(0, json.length() - 1);
			}
		}
		return "[" + json + "]";
	}
	/**
	 * 后台Page转化为前台可以识别的json对象
	 * @param p
	 * @return
	 */
	public static String page2Json(Page p){
		String json = model2Json(p.getList());
		return "{\"records\":\"" + p.getTotalRow() + "\",\"page\":\"" + p.getPageNo() + "\",\"total\":\"" + p.getTotalPage() + "\",\"rows\":" + json + "}";
	}
	
	public static<T> void setClassValue(T o,Map<String,Object> params) {
		
        //获得该类的所有属性
        Field[] fields = o.getClass().getDeclaredFields();
        for(Field field:fields){
            try {
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), o.getClass());
				//获得set方法
				Method method = pd.getWriteMethod();
				String methodName = method.getName();
				if(methodName.startsWith("set")){
					methodName = methodName.substring(3);
				}else if(methodName.startsWith("is")){
					methodName = methodName.substring(2);
				}
				
				if(methodName.length() <= 0){
					continue;
				}
				methodName = methodName.substring(0,1).toLowerCase() + methodName.substring(1);
				if(!params.containsKey(methodName)){
					continue;
				}
				method.invoke(o, params.get(methodName));
            } catch (IntrospectionException e) {
            	//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			} 
        }
    }
	
	/**
	 * 通过Request获取客户端的IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    } 
	
	public static String map2String(Map<String,String[]> map){
		String str = "";
		for(Entry<String, String[]> e : map.entrySet()){
			str += e.getKey() + "=" ;
			for(String s : e.getValue()){
				str += s + ",";
			}
			str += ";";
		}
		return str;
	}
    public static String getFileToString(File f) throws IOException {  
        InputStream is = null;
        String ret = null;
        try {
            is =  new FileInputStream(f) ;
            long contentLength = f.length();
            byte[] ba = new byte[(int)contentLength];
            is.read(ba);
            ret = new String(ba);
        } finally {
            if(is!=null) {try{is.close();} catch(Exception e){} }
        }
        return ret;        
    } 
    
    /**
     * 发送请求
     * 
     * @param url
     *            请求地址
     * @param filePath
     *            文件在服务器保存路径（这里是为了自己测试方便而写，可以将该参数去掉）
     * @return
     * @throws IOException
     */
    public static String uploadFile(String url, File file) throws IOException {
 
        if (!file.exists() || !file.isFile()) {
            return "Error:file not exist";
        }
 
        /**
         * 第一部分
         */
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
 
        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
 
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
 
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);
 
        // 请求正文信息
 
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // ////////必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
 
        byte[] head = sb.toString().getBytes("utf-8");
 
        // 获得输出流
 
        OutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(head);
 
        // 文件正文部分
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
 
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
 
        out.write(foot);
 
        out.flush();
        out.close();
        
        StringBuffer s = new StringBuffer();
		try {
			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				s.append(line);
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
        return s.toString();
 
    }
    
    /**
     * 微信服务器素材上传
     * @param file  表单名称media
     * @param token access_token
     * @param type  type只支持四种类型素材(video/image/voice/thumb)
     */
    public static JSONObject uploadMedia(String url,File file, String token, String type) {
        if(file==null||token==null||type==null){
            return null;
        }

        if(!file.exists()){
            logger.info("上传文件不存在,请检查!");
            return null;
        }

        JSONObject jsonObject = null;
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Connection", "Keep-Alive");
        post.setRequestHeader("Cache-Control", "no-cache");
        FilePart media = null;
        HttpClient httpClient = new HttpClient();
        //信任任何类型的证书
        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443); 
        Protocol.registerProtocol("https", myhttps);

        try {
            media = new FilePart("media", file);
            Part[] parts = new Part[] { new StringPart("access_token", token),
                    new StringPart("type", type), media };
            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                    post.getParams());
            post.setRequestEntity(entity);
            int status = httpClient.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String text = post.getResponseBodyAsString();
                jsonObject = JSONObject.fromObject(text);
            } else {
                logger.info("upload Media failure status is:" + status);
            }
        } catch (FileNotFoundException execption) {
            logger.error("Error",execption);
        } catch (HttpException execption) {
        	logger.error("Error",execption);
        } catch (IOException execption) {
        	logger.error("Error",execption);
        }
        return jsonObject;
    }
    
}
