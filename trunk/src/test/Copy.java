package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {
    //排除复制的文件类型  
    public static String[] filterFile = {".svn"}; 
    public static String[] filterFolder = {".svn"}; 
	
	private static final String Dir_Source_Project = "E:\\Legame\\colee_oss\\trunk";
	private static final String Dir_Dest_Project_Channel = "E:\\Legame\\colee-channel\\trunk";
	private static final String Dir_Dest_Project_Cp = "E:\\Legame\\colee-cp\\trunk";
	
	public static void main(String[] args) {
		detailCopy(Dir_Source_Project, Dir_Dest_Project_Channel);
		detailCopy(Dir_Source_Project, Dir_Dest_Project_Cp);
	}
	
	public static void detailCopy(String source,String dest){
		try {
			//action.auth
			copyFolder(new File(source + "\\src\\com\\jfinal\\aceadmin\\action\\auth"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\action\\auth"));
			//action.index
			copyFolder(new File(source + "\\src\\com\\jfinal\\aceadmin\\action\\index"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\action\\index"));
			//action.AbstractController.java
			copyFile(new File(source + "\\src\\com\\jfinal\\aceadmin\\action\\AbstractController.java"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\action\\AbstractController.java"));
			//bean
			copyFolder(new File(source + "\\src\\com\\jfinal\\aceadmin\\bean"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\bean"));
			//interceptor
			copyFolder(new File(source + "\\src\\com\\jfinal\\aceadmin\\interceptor"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\interceptor"));
			//utils
			copyFolder(new File(source + "\\src\\com\\jfinal\\aceadmin\\utils"), new File(dest + "\\src\\com\\jfinal\\aceadmin\\utils"));
			//oss_config.cfg
			copyFile(new File(source + "\\appcfg\\oss_config.cfg"), new File(dest + "\\appcfg\\oss_config.cfg"));
			
			
			//webapp\admin\assets
			copyFolder(new File(source + "\\webapp\\admin\\assets"), new File(dest + "\\webapp\\admin\\assets"));
			//webapp\admin\auth
			copyFolder(new File(source + "\\webapp\\admin\\auth"), new File(dest + "\\webapp\\admin\\auth"));
			//webapp\admin\common
			copyFolder(new File(source + "\\webapp\\admin\\common"), new File(dest + "\\webapp\\admin\\common"));
			//webapp\admin\index
			copyFolder(new File(source + "\\webapp\\admin\\index"), new File(dest + "\\webapp\\admin\\index"));
			//webapp\changepassword.html
			copyFile(new File(source + "\\webapp\\changepassword.html"), new File(dest + "\\webapp\\changepassword.html"));
			//webapp\index.html
			copyFile(new File(source + "\\webapp\\index.html"), new File(dest + "\\webapp\\index.html"));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	

	public static void copyFolder(File srcFolder, File destFolder)throws Exception {
		File[] files = srcFolder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String pathname = destFolder + File.separator + file.getName();

				for (String suff : filterFile) {
					if (!pathname.endsWith(suff)) {
						File dest = new File(pathname);
						File destPar = dest.getParentFile();
						destPar.mkdirs();
						if (!dest.exists()) {
							dest.createNewFile();
						}
						copyFile(file, dest);
						break;
					}
				}
			} else {
				String fileName = file.getName();
				for (String suff : filterFile) {
					if (!fileName.equalsIgnoreCase(suff)) {
						copyFolder(file, new File(destFolder,file.getName()));
						break;
					}
				}
			}
		}
	}

	/***
	 * copy file
	 * 
	 * @param src
	 * @param dest
	 * @param status
	 * @throws IOException
	 */
	private static void copyFile(File src, File dest) throws Exception {
		BufferedInputStream reader = null;
		BufferedOutputStream writer = null;
		try {
			reader = new BufferedInputStream(new FileInputStream(src));
			writer = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] buff = new byte[reader.available()];
			while ((reader.read(buff)) != -1) {
				writer.write(buff);
			}
			String temp = "\ncopy:\n" + src + "\tsize:" + src.length()
					+ "\nto:\n" + dest + "\tsize:" + dest.length()
					+ "\n complate";
			System.out.println(temp);
			// status.append(temp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
			reader.close();

		}
	}
	

}
