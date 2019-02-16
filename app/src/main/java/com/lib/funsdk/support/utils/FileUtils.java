package com.lib.funsdk.support.utils;

import com.lib.FunSDK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by Jeff on 16/4/20.
 */
public class FileUtils {

    /**
     * 复制单个文件
     *
     * @param oldPath 原文件路径 如：c:/fqf.txt
     * @param newPath 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cleanFolder(String path) {
        File folder = new File(path);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File f : files) {
                f.delete();
            }
            return true;
        } else {
            return false;
        }
    }
    
    
    public static String readFromFile(String path) {
    	try {
    		File file = new File(path);
    		
    		if ( !file.exists() ) {
    			return null;
    		}
    		
    		FileReader fr = new FileReader(file);
    		BufferedReader br = new BufferedReader(fr);
    		
    		String buffer = "";
    		String line = null;
    		while ((line = br.readLine()) != null) {
    			buffer += line + "\n";
    		}
    		
    		br.close();
    		fr.close();
    		
    		return buffer;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }

    public static void saveToFile(String path, String text) {
    	try {
    		File file = new File(path);
    		
    		if ( !file.exists() ) {
    			file.createNewFile();
    		}
    		
    		FileWriter fw = new FileWriter(file);
    		fw.write(text);
    		
    		fw.flush();
    		fw.close();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static String FormetFileSize(long fileS, int unit) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		
		if (unit == 0) {
			if (fileS == 0) {
				fileSizeString = "0.00B";
			} else if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "K";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "M";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "G";
			}
		}
		else if (unit == 1) {
			if (fileS == 0) {
				fileSizeString = "0.00K";
			} else if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "K";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "M";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "G";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "T";
			}
		}
		else if (unit == 2) {
			if (fileS == 0) {
				fileSizeString = "0.00M";
			} else if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "M";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "G";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "T";
			}
		}
		return fileSizeString;
	}
}
