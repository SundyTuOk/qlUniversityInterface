package com.sf.qlinterface.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sinojy.front.util.CommonParamUtils;
import cn.sinojy.front.util.FrontCrypt;
import cn.sinojy.front.util.PreCommonMethod;

import com.sf.db.domain.Ammeter;
import com.sf.db.domain.ZAmDatas;
import com.sf.qlinterface.dao.ZAmDatasDAO;
import com.sf.qlinterface.service.QLInterfaceServiceImpl;
import com.sf.utils.CommonUtils;
import com.sf.utils.Constant;
import com.sf.utils.SpringContextUtil;

@Controller  
@RequestMapping("/sf")
public class QLInterfaceController {
	
	private Logger logger = Logger.getLogger(QLInterfaceController.class);
	
	/**
	 * 盛帆提供给齐鲁大学接口分发器，根据request里面的intId(接口id)，来分发给不同的接口处理
	 * @param req
	 * @param rsp
	 * @throws ParseException 
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST) 
	@ResponseBody
	public String check(HttpServletRequest req,HttpServletResponse rsp) throws ParseException{
		logger.info("---------"+req.getServletContext().getRealPath("/")+"WEB-INF\\classes\\cert\\");
		String encryptionPath = req.getServletContext().getRealPath("/")+"WEB-INF\\classes\\cert\\1.pfx";
		String decryptionPath = req.getServletContext().getRealPath("/")+"WEB-INF\\classes\\cert\\1.crt";
		
		logger.info("加密证书路径->"+encryptionPath+"\n解密证书路径->"+decryptionPath);
		
		String jsonString = null;
		try {
			jsonString = CommonUtils.getStringFromStream(req.getInputStream());
		} catch (IOException e) {
			logger.error(e);
			logger.error("从HttpServletRequest里面获取输入数据流失败，请检查网络状况");
			return null;
		}
		JSONObject jsonObject =JSONObject.fromObject(jsonString);
		String interfaceId = jsonObject.optString(Constant.INTID);
		
		QLInterfaceServiceImpl qLInterfaceServiceImpl = SpringContextUtil.getBean("QLInterfaceServiceImpl");
		if(interfaceId.equals(Constant.CHECK_CODE)){
			// 充值编码查询
			String jsonStr = qLInterfaceServiceImpl.check(jsonObject,encryptionPath,decryptionPath);
			return jsonStr;
		}
		
		if(interfaceId.equals(Constant.RECHNOTIFY_CODE)){
			// 充值结果查询
			String jsonStr = qLInterfaceServiceImpl.rechNotify(jsonObject, encryptionPath, decryptionPath);
			return jsonStr;
		}
		
		if(interfaceId.equals(Constant.BALANCE_CODE)){
			// 对账接口
			String ftpFilePath = req.getServletContext().getRealPath("/")+"ftpFiles";
			String jsonStr = qLInterfaceServiceImpl.balance(jsonObject, encryptionPath, decryptionPath,ftpFilePath);
			return jsonStr;
		}
		
		String jsonStr = qLInterfaceServiceImpl.defalutMethod(jsonObject, encryptionPath, decryptionPath);
		
		return jsonStr;
	}
}
