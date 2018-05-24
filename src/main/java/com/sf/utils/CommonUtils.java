package com.sf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

public class CommonUtils {
	
	private static Logger logger = Logger.getLogger(CommonUtils.class);
	
	/**
	 * 从输入流中读取数据
	 * @param is
	 * @return
	 */
	public static String getStringFromStream(InputStream is){
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String temp = "";
		try {
			while((temp = br.readLine()) != null){
				sb.append(temp);
			}
			return sb.toString();
		} catch (IOException e) {
			logger.error(e);
			logger.error("从request输入流中读取数据失败，请检查网络连接");
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e);
				logger.error("关闭request输入流失败，请检查网络连接");
			}
		}
		return null;
	}
	
	/**
	 * //解密，解密后数据参见接口文档，参数说明：strDes=密文 signMsg=签名字符串 certPath=公钥证书路径  strKey=加密密钥 signKey=签名干扰串
	 * @return
	 */
	public static String decryption(String strDes,String signMsg,String certPath,String strKey,String signKey){
		return null;
	}
}
