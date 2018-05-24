package com.sf.qlinterface.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.sinojy.front.util.CommonParamUtils;
import cn.sinojy.front.util.DESUtil;
import cn.sinojy.front.util.FrontCrypt;
import cn.sinojy.front.util.PreCommonMethod;

import com.sf.db.domain.Ammeter;
import com.sf.db.domain.AmmeterAPDatas;
import com.sf.db.domain.ApCardDZ;
import com.sf.db.domain.ApCardErrorInfo;
import com.sf.db.domain.ApCardSaleInfo;
import com.sf.db.domain.ApSaleInfo;
import com.sf.db.domain.ZAmDatas;
import com.sf.qlinterface.dao.AmmeterAPDatasDAO;
import com.sf.qlinterface.dao.AmmeterDAO;
import com.sf.qlinterface.dao.ApCardDZDAO;
import com.sf.qlinterface.dao.ApCardErrorInfoDAO;
import com.sf.qlinterface.dao.ApCardSaleInfoDAO;
import com.sf.qlinterface.dao.ApSaleInfoDAO;
import com.sf.qlinterface.dao.PriceDAO;
import com.sf.qlinterface.dao.ZAmDatasDAO;
import com.sf.utils.FtpUtil;

/**
 * 充值编码查询相关service
 * @author tuzhaoliang
 * @date 2018年3月28日
 */
@Service("QLInterfaceServiceImpl")
public class QLInterfaceServiceImpl implements QLInterfaceService{
	
	private Logger logger = Logger.getLogger(QLInterfaceServiceImpl.class);

	@Resource(name="ammeterDAO")
	private AmmeterDAO ammeterDAO;
	@Resource(name="zAmDatasDAO")
	private ZAmDatasDAO zAmDatasDAO;
	@Resource(name="ammeterAPDatasDAO")
	private AmmeterAPDatasDAO ammeterAPDatasDAO;
	@Resource(name="apSaleInfoDAO")
	private ApSaleInfoDAO apSaleInfoDAO;
	@Resource(name="priceDAO")
	private PriceDAO priceDAO;
	@Resource(name="apCardDZDAO")
	private ApCardDZDAO apCardDZDAO;
	@Resource(name="apCardSaleInfoDAO")
	private ApCardSaleInfoDAO apCardSaleInfoDAO;
	@Resource(name="apCardErrorInfoDAO")
	private ApCardErrorInfoDAO apCardErrorInfoDAO;
	

	public Ammeter getAmmeterByConsumerNum(String comsumerNum) {
		return ammeterDAO.getAmmeterByConsumerNum(comsumerNum);
	}
	
	public ZAmDatas getLastZamDatas(String zAMTableName){
		return zAmDatasDAO.getLastZamDatas(zAMTableName);
	}
	
	/**
	 * 获取最新的apdatas 可以得到剩余金额
	 * @param ammeterId
	 * @return
	 */
	public AmmeterAPDatas getLastAmmeterAPDatasByAmmeterId(Integer ammeterId){
		return ammeterAPDatasDAO.getLastAmmeterAPDatasByAmmeterId(ammeterId);
	}
	
	/**
	 * 充值编码查询
	 * 接口id：001
	 * @param jsonObject
	 * @param encryptionPath 加密证书路径
	 * @param decryptionPath 解密证书路径
	 * @return
	 */
	public String check(JSONObject jsonObject,String encryptionPath,String decryptionPath){
		logger.info("充值编码查询请求json->"+jsonObject.toString());
		
		Map<String,Object> strDesMap = null;//需要加密的数据
		String strDes = null;// 加密的报文
		String signMsg = null;// 请求的签名
		try{
			//得到加密后的数据 _报文
			strDes = jsonObject.optString("strDes");
			//得到证书签名
			signMsg = jsonObject.optString("signMsg");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json里面主数据缺失");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		
		logger.info("充值编码查询请求json:strDes->"+strDes);
		logger.info("充值编码查询请求json:signMsg->"+signMsg);
//		signMsg = "";
		
		//解密，解密后数据参见接口文档，参数说明：strDes=密文 signMsg=签名字符串 certPath=公钥证书路径  strKey=加密密钥 signKey=签名干扰串
		JSONObject reqData = null;
		try{
			reqData =  FrontCrypt.decryptWithKey(strDes,signMsg,decryptionPath,"12345678","");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json格式或者内容不正确");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		logger.info("解密后报文->"+reqData.toString());
		String deviceId = reqData.optString("deviceId");
		
		if(deviceId == null || deviceId.equals("")){
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:deviceId不正确");
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:deviceId不正确");
		}
		Ammeter ammeter = null;
		ZAmDatas lastZamDatas = null;
		AmmeterAPDatas ammeterAPDatas = null;
		try{
			ammeter = getAmmeterByConsumerNum(deviceId);
			lastZamDatas = getLastZamDatas("ZAmDatas"+ammeter.getAmmeterID());
			ammeterAPDatas = getLastAmmeterAPDatasByAmmeterId(ammeter.getAmmeterID());
		}catch(Exception e){
			logger.info("数据库连接或者根据所传参数查询异常");
		}
		
		if(ammeter == null || lastZamDatas == null || ammeterAPDatas == null){
			//失败
			strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:数据库连接或者根据所传参数查询异常");
			logger.error("返回错误码->000002,描述->系统错误，请联系管理员处理:数据库连接或者根据所传参数查询异常");
		}else{
			//成功
			strDesMap = new HashMap<String,Object>();
			strDesMap.put("resultCode", "000000"); // 返回码(000000为成功)
			strDesMap.put("resultMsg", "处理完成或接收成功"); // 返回信息
			
			logger.info("返回码->000000,处理完成或接收成功");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			strDesMap.put("tradeTime", sdf.format(new Date())); // 交易时间
			strDesMap.put("deviceId", deviceId);//充值编码
			
			/**要展示给用户的信息，可自定义 begin ******************************/
			Map<String,Object> deviceInfo = new HashMap<String,Object>();//缴费编码数据
			deviceInfo.put("nameTitle","电表ID");
			deviceInfo.put("nameValue",ammeter.getAmmeterID()+"");
			deviceInfo.put("unitTitle","房间号");
			deviceInfo.put("unitValue",ammeter.getConsumerNum());

			List<Object> infoList = new ArrayList<Object>();
			Map<String,String> infoMap1 = new HashMap<String, String>();
			infoMap1.put("key","ammeter485Address");
			infoMap1.put("keyName","电表485地址");
			infoMap1.put("keyValue",ammeter.getAmmeter485Address());
			infoList.add(infoMap1);

			Map<String,String> infoMap2 = new HashMap<String, String>();
			infoMap2.put("key","balance");
			infoMap2.put("keyName","余额");
			infoMap2.put("keyValue",ammeterAPDatas.getSyMoney().toString());
			infoList.add(infoMap2);
			
//			Map<String,String> infoMap3 = new HashMap<String, String>();
//			infoMap3.put("key","lastTime");
//			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			infoMap3.put("keyName","电表最后通讯时间");
//			infoMap3.put("keyValue",sdf.format(ammeter.getLastTime()));
//			infoList.add(infoMap3);
			
			Map<String,String> infoMap4 = new HashMap<String, String>();
			infoMap4.put("key","zValue");
			infoMap4.put("keyName","电表表码");
			infoMap4.put("keyValue",lastZamDatas.getzValueZY().toString());
			infoList.add(infoMap4);
			/**要展示给用户的信息，可自定义 end ******************************/
			
			deviceInfo.put("infos", infoList);
			strDesMap.put("deviceInfo", deviceInfo);
			strDesMap.put("billFlag", "0");// 0.无 1.有
		}
		
		// 加密后的strDesMap
		//加密，参数说明：strDes=密文 certPath=私钥证书路径  strKey=加密密钥 signKey=签名干扰串
		Map<String, String> strDesMapEncryption = FrontCrypt.encryptWithKey(strDesMap,encryptionPath,"12345678","12345678","");
		
		Map<String, Object> returnMap = PreCommonMethod.returnMap(jsonObject.optString(CommonParamUtils.version), 
				jsonObject.optString(CommonParamUtils.charset), 
				jsonObject.optString(CommonParamUtils.tradeNo), 
				jsonObject.optString(CommonParamUtils.merId), 
				jsonObject.optString(CommonParamUtils.rechId), 
				jsonObject.optString(CommonParamUtils.intId), 
				strDesMapEncryption.get(CommonParamUtils.strDes), strDesMapEncryption.get(CommonParamUtils.signMsg));
		//返回请求信息前先序列化map转为json串
		JSONObject returnObj = JSONObject.fromObject(returnMap);
		return returnObj.toString();
	}
	
	/**
	 * 返回失败desMap
	 * @param resultCode
	 * @param resultMsg
	 * @return
	 */
	public Map<String,Object> returnFailDesMap(String resultCode,String resultMsg){
		Map<String,Object> desMap = new HashMap<String,Object>();
		desMap.put("resultCode", resultCode); // 返回码(000002为失败)
		desMap.put("resultMsg", resultMsg); // 返回信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		desMap.put("tradeTime", sdf.format(new Date())); // 交易时间
		return desMap;
	}
	
	/**
	 * 充值结果查询
	 * 接口id：002
	 * @param jsonObject
	 * @param encryptionPath
	 * @param decryptionPath
	 * @return
	 */
	public String rechNotify(JSONObject jsonObject,String encryptionPath,String decryptionPath){
		
		Map<String,Object> strDesMap = null;//需要加密的数据
		String strDes = null;// 加密的报文
		String signMsg = null;// 请求的签名
		try{
			//得到加密后的数据 _报文
			strDes = jsonObject.optString("strDes");
			//得到证书签名
			signMsg = jsonObject.optString("signMsg");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json里面主数据缺失");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		logger.info("充值编码查询请求json:strDes->"+strDes);
		logger.info("充值编码查询请求json:signMsg->"+signMsg);
//		signMsg = "";
		
		//解密，解密后数据参见接口文档，参数说明：strDes=密文 signMsg=签名字符串 certPath=公钥证书路径  strKey=加密密钥 signKey=签名干扰串
		JSONObject reqData = null;
		try{
			reqData =  FrontCrypt.decryptWithKey(strDes,signMsg,decryptionPath,"12345678","");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json格式或者内容不正确");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		logger.debug("解密后报文->"+reqData.toString());
		
		String deviceId = reqData.optString("deviceId");
		String serioNo = reqData.optString("serioNo");
		String amount  = reqData.optString("amount");
		String tradeTime  = reqData.optString("tradeTime");
		Ammeter ammeter = null;
		try{
			ammeter = getAmmeterByConsumerNum(deviceId);
		}catch(Exception e){
			logger.error("数据库连接异常，或根据参数查询异常");
		}
		
		if(deviceId == null || deviceId.equals("") 
				|| serioNo == null || serioNo.equals("")
				|| amount == null || amount.equals("")
				|| tradeTime == null || tradeTime.equals("")){
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理");
		}
		
		Date tradeTimeDate = null;
		Float theMoney = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			tradeTimeDate = sdf.parse(tradeTime);
			theMoney = Float.parseFloat(amount);
		}catch(ParseException e){
			logger.info("tradeTime格式异常:"+tradeTime);
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:接口参数异常");
		}
		
		
		if(ammeter == null || ammeter.getAmmeterID() == null){
			// 失败
			strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:数据库连接或者根据所传参数查询异常");
		}
		
//		else{
//			// 成功
//			strDesMap = new HashMap<String,Object>();
//			strDesMap.put("resultCode", "000000"); // 返回码(000000为成功)
//			strDesMap.put("resultMsg", "处理完成或接收成功"); // 返回信息
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			strDesMap.put("tradeTime", sdf.format(new Date())); // 交易时间
//			strDesMap.put("deviceId", deviceId);//充值编码
//		}
		
		if(ammeter.getaPstate() == null || !ammeter.getaPstate().equals("开户")){
			// 没有开户，不允许充值
			strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:电表没有开户，请先开户");
		}
		try{
			if(apSaleInfoDAO.getApSaleInfoByOrderNo(serioNo) != null){
				// 本次充值在apsaleinfo表里面已经存在
				strDesMap = returnFailDesMap("000000", "处理完成或接收成功:本次充值编号在系统已经存在");
			}
		}catch(Exception e){
			strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:数据库连接异常或者根据serioNo查询异常");
		}
		
		String saleInfoNum = apSaleInfoDAO.getLastApSaleInfo().getSaleInfoNum();
		if(saleInfoNum == null || saleInfoNum.equals("")){
			// saleInfoNum获取失败
			strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:系统ApSaleInfo表最新的saleInfoNum获取失败");
		}
		
		if(strDesMap == null){
			try{
				Long saleInfoNumber = Long.valueOf(saleInfoNum)+1;
				ApSaleInfo apSaleInfo = new ApSaleInfo();
				apSaleInfo.setAmmeterID(ammeter.getAmmeterID());
				apSaleInfo.setAmmeterName(ammeter.getAmmeterName());
				apSaleInfo.setAmmeter_485Address(ammeter.getAmmeter485Address());
				apSaleInfo.setKind(1);
				apSaleInfo.setBuyTime(tradeTimeDate);
				apSaleInfo.setStatus(0);
				apSaleInfo.setOrderNo(serioNo);
				
				Float priceValue = priceDAO.getPriceValueByPriceID(ammeter.getPriceID());
				apSaleInfo.setPrice(priceValue);
				Float thegross = div(theMoney,priceValue,1);
				apSaleInfo.setThegross(thegross);
				apSaleInfo.setThemoney(theMoney);
				apSaleInfo.setUsersName("齐鲁大学接口售电");
				while(true){
					apSaleInfo.setSaleInfoNum(saleInfoNumber.toString());
					int line = apSaleInfoDAO.addApSaleInfo(apSaleInfo);
					if(line == 1){
						// 插入成功
						break;
					}
					saleInfoNumber++;
				}
				// 这里借用失败map，只需要返回这么几个字段就行,虽然不够规范
				strDesMap = returnFailDesMap("000000", "处理完成或接收成功");
			}catch(Exception e){
				logger.info(e);
				logger.info("生成购电单失败，请检查数据库连接状况");
				strDesMap = returnFailDesMap("000004", "数据保存失败，请联系管理员处理:插入售电单到数据库失败，请先检查数据库状况");
			}
		}
		
		//加密，参数说明：strDes=密文 certPath=私钥证书路径  strKey=加密密钥 signKey=签名干扰串
		// 加密后的map
		Map<String, String> strDesMapEncryption = FrontCrypt.encryptWithKey(strDesMap,encryptionPath,
				"12345678","12345678","");
		Map<String, Object> returnMap = PreCommonMethod.returnMap(jsonObject.optString(CommonParamUtils.version)
				, jsonObject.optString(CommonParamUtils.charset)
				, jsonObject.optString(CommonParamUtils.tradeNo)
				, jsonObject.optString(CommonParamUtils.merId)
				, jsonObject.optString(CommonParamUtils.rechId)
				, jsonObject.optString(CommonParamUtils.intId)
				, strDesMapEncryption.get("strDes"), strDesMapEncryption.get("signMsg"));
		
		//返回请求信息前先序列化map转为json串
		JSONObject returnObj = JSONObject.fromObject(returnMap);
		return returnObj.toString();
	}
	
//	@Value("#{propertyConfigurer1['ip']}")
//	@Value("${ip}")
	@Value("#{projectSettings['ftp.ip']}")
	private String ftpIP;
	@Value("#{projectSettings['ftp.port']}")
	private String ftpPort;
	@Value("#{projectSettings['ftp.user']}")
	private String ftpUsername;
	@Value("#{projectSettings['ftp.password']}")
	private String ftpPassword;
	
	public String balance(JSONObject jsonObject,String encryptionPath,String decryptionPath,String ftpFilePath){
		logger.info("对账接口查询请求json->"+jsonObject.toString());
		String localFile = null;//ftp文件下载路径+文件名
		Map<String,Object> strDesMap = null;//需要加密的数据
		String strDes = null;// 加密的报文
		String signMsg = null;// 请求的签名
		try{
			//得到加密后的数据 _报文
			strDes = jsonObject.optString("strDes");
			//得到证书签名
			signMsg = jsonObject.optString("signMsg");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json里面主数据缺失");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		
		logger.info("对账接口查询请求json:strDes->"+strDes);
		logger.info("对账接口查询请求json:signMsg->"+signMsg);
//		signMsg = "";
		
		//解密，解密后数据参见接口文档，参数说明：strDes=密文 signMsg=签名字符串 certPath=公钥证书路径  strKey=加密密钥 signKey=签名干扰串
		JSONObject reqData = null;
		try{
			reqData =  FrontCrypt.decryptWithKey(strDes,signMsg,decryptionPath,"12345678","");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json格式或者内容不正确");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		logger.info("解密后报文->"+reqData.toString());
		
//		strDesMap = new HashMap<String,Object>();
//		strDesMap.put("resultCode", "000002"); // 返回码(000000为成功)
//		strDesMap.put("resultMsg", "系统错误，请联系管理员处理:本接口未开发完成"); // 返回信息
//		logger.info("这里暂时返回码->000002,系统错误，请联系管理员处理:本接口未开发完成");
//		strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:本接口未开发完成");
//		strDesMap.put("detail", "本接口暂时未开发完成，先预留接口");
		String balanceDate = reqData.optString("balanceDate");
		String filePath = reqData.optString("filePath");
		String fileName  = reqData.optString("fileName");
		
		if(balanceDate == null || balanceDate.equals("") 
				|| filePath == null || filePath.equals("")
				|| fileName == null || fileName.equals("")){
			logger.info("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json内容不全");
			returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json内容不全");
		}
		
		if(strDesMap == null){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date balanceDateDate = sdf.parse(balanceDate);
				Date now = new Date();
				// 如果对账日期比现在还要晚，返回错误
				if(!now.after(balanceDateDate)){
					logger.info("返回错误码->000001,描述->接口参数不足，拒绝受理:对账日期晚于当前时间，错误");
					strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:对账日期晚于当前时间，错误");
				}
			}catch(ParseException e){
				logger.info("返回错误码->000001,描述->接口参数不足，拒绝受理:对账日期格式可能不正确");
				strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:对账日期格式可能不正确");
			}
		}
		
		if(strDesMap == null){
			// 获取ftp服务器信息
			logger.info("获取ftp服务器信息");
			logger.debug("ftpIP->"+ftpIP+",ftpPort->"+ftpPort+",ftpUsername->"+ftpUsername+",ftpPassword->"+ftpPassword);
			if(ftpIP == null || ftpIP.equals("")
					|| ftpPort == null || ftpPort.equals("")
					|| ftpUsername == null || ftpUsername.equals("")
					|| ftpPassword == null || ftpPassword.equals("")){
				logger.info("获取ftp服务器信息失败");
				logger.info("返回错误码->000002,描述->系统错误，请联系管理员处理:ftp地址或者用户名密码配置不正确");
				strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:ftp地址或者用户名密码配置不正确");
			}
		}
		
		if(strDesMap == null){
			// 准备从ftp服务器下载文件
			logger.info("准备从ftp服务器下载文件");
			localFile = ftpFilePath + filePath + File.separator;
			File ftpLocalFile = new File(localFile);
			if(!ftpLocalFile.exists()){
				ftpLocalFile.mkdirs();
			}
			boolean downloadSuccess = false;
			try{
				downloadSuccess = FtpUtil.downloadFtpFile(ftpIP, ftpUsername, ftpPassword, Integer.valueOf(ftpPort), filePath, localFile, fileName);
			}catch(Exception e){
				logger.info("从ftp服务器下载文件失败");
				logger.error(e);
			}
			
			logger.debug("ftpIP->"+ftpIP+",ftpPort->"+ftpPort+",ftpUsername->"+ftpUsername+",ftpPassword->"+ftpPassword);
			logger.debug("filePath->"+filePath+",fileName->"+fileName+"下载到本地path->"+localFile+fileName);
			
			if(!downloadSuccess){
				// 下载失败
				logger.info("返回错误码->000005,描述->第三方未返回数据，请联系管理员处理:ftp下载文件失败");
				strDesMap = returnFailDesMap("000005", "第三方未返回数据，请联系管理员处理:ftp下载文件失败");
			}
			logger.info("从ftp服务器下载文件成功");
		}
		
		// 对账文件解密后内容
		String decryptionContent = null;
		if(strDesMap == null){
			// 对下载的对账文件进行解密
			logger.info("对下载的对账文件进行解密");
			StringBuffer ftpFileContent = new StringBuffer();
			String temp = null;
			File ftpFile = new File(localFile + fileName);
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(ftpFile));
				while((temp = br.readLine()) != null){
					ftpFileContent.append(temp);
				}
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}finally{
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			
			logger.debug("读取到ftp对账文件内容->" + ftpFileContent.toString());
			try{
				decryptionContent = DESUtil.decryptDES(ftpFileContent.toString(), "12345678");
				logger.debug("解密后ftp对账文件内容->" + decryptionContent);
			}catch(Exception e){
				logger.error(e);
				logger.error("对账文件解密失败");
				logger.info("返回错误码->000002,描述->系统错误，请联系管理员处理:对账文件解密失败");
				strDesMap = returnFailDesMap("000002", "系统错误，请联系管理员处理:对账文件解密失败");
			}
		}
		
//		Map<String,Object> strDesMap = null;//需要加密的数据
//		String decryptionContent = "3|97.1\nR3030120180411145449000000000022|201804111455011|111002|null|0.10|S\nR3030120180411151424000000000026|201804111514053|111002|null|87.00|S\nR3030120180411154307000000000030|201804111543048|111002|null|10.00|S";
//		
//		String encryptDES = "C6sRKflEghdOGZyzjhU5ndmLFqpw9dcLfvaZjAV4mrsQ6niURS0GT0uokYPxFaDdtzf0m71HUZjlIsDp7vYWFfzHyNImy5LP+8s+5yjrb5Iir7Nr/CD56x43ZxC2vz7hup3J/MqD5slfs+VcnnK7ij6LAHqlDoaWVMQm2yw70LgFchAB7649DZ7gsu4P4hmjXUgLSzRWY6fR+r6hNUrtl+lBqdizfeafX2p9Uo45TzkqKSsmdYI6rRA9b3ivTLBzlA+waaxvtnyyTwcncuxojedYhzdRKlbd/rlZt9RkL8s=";
//		String decryptionContent = DESUtil.decryptDES(encryptDES, "12345678");
//		logger.debug("解密后ftp对账文件内容->" + decryptionContent);
//		
//		decryptionContent = "3|96.1\\nR3030120180411145449000000000444|201804111455011|111002|null|0.10|S\\nR3030120180411151424000000000026|201804111514053|111002|null|86.00|S\\nR3030120180411154307000000000030|201804111543048|111002|null|10.00|S";
		
		String[] decryptionLines = null;// 对账文件每行
		String[] header = null;// 对账文件头 两列
		Integer totalNumber = null;
		Float totalMoney = null;
		if(strDesMap == null){
			// 对账文件总笔数比对
			decryptionLines = decryptionContent.split("\\\\n");
			logger.debug("格式化明文对账文件之后：");
			for(String line : decryptionLines){
				logger.debug(line);
			}
			header = decryptionLines[0].split("\\|");
			totalNumber = Integer.valueOf(header[0]);// 总笔数
			totalMoney = Float.valueOf(header[1]);
			if((decryptionLines.length-1) != totalNumber){
				//总笔数不对
				logger.info("对账文件总笔数不对");
				logger.info("返回错误码->000001,描述->接口参数不足，拒绝受理:对账文件总笔数不对");
				strDesMap = returnFailDesMap("接口参数不足，拒绝受理", "接口参数不足，拒绝受理:对账文件总笔数不对");
			}
		}
		
		Float sumMoney = 0f;
		if(strDesMap == null){
			// 对账文件总金额比对
			for(int i = 1;i<decryptionLines.length;i++){
				sumMoney += Float.valueOf(decryptionLines[i].split("\\|")[4]);
			}
			
			if(sumMoney.floatValue() != totalMoney.floatValue()){
				//总笔数不对
				logger.info("对账文件总金额不对");
				logger.info("返回错误码->000001,描述->接口参数不足，拒绝受理:对账文件总金额不对");
				strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:对账文件总金额不对");
			}
		}
		
		if(strDesMap == null){
			//比对每笔账单的金额 与 数据库中的数据进行比对
			StringBuffer rtnStr = new StringBuffer();
			try{
				// 往ApCardDZ表里面插入本次对账记录
				ApCardDZ apCardDZ = new ApCardDZ();
				// 这里暂时只设置对账文件里面的总金额cardMoney，没有设置系统里面的总金额apMoney
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String DZTime = sdf.format(new Date());
				sdf = null;
				apCardDZ.setaPCardDZTime(DZTime);// 对账时间
				apCardDZ.setCardMoney(sumMoney);// 对账文件总金额
//				apCardDZ.setApMoney();// 设置系统里面的总金额
				apCardDZ.setCardCountTimes(decryptionLines.length-1);// 对账总笔数
				apCardDZ.setCardTimes(decryptionLines.length-1); // 对账文件里面状态为正常的笔数
//				apCardDZ.setaPTimes();// 设置系统里面账单的总笔数
//				apCardDZ.setEroorTimes();// 设置出错的账单笔数
//				apCardDZ.setExecTimes();// 设置已经处理了的错误账单数
				apCardDZ.setUsersID(1);
				apCardDZ.setInsertTime(new Date());
				apCardDZDAO.addApCardDZ(apCardDZ);
				Integer apCardDZ_ID = apCardDZ.getaPCardDZID();
				logger.info("向apCardDZ插入一条id->"+apCardDZ_ID+"记录:"+apCardDZ.toString());
				
				// 开始遍历对账单
				Float apMoney = 0f;
				int apTimes = 0;
				int execTimes = 0;
				for(int i = 1;i<decryptionLines.length;i++){
					apTimes++;
					
					String[] line = decryptionLines[i].split("\\|");
					String orderNo = line[0];// 交易流水号
					String tradeTime = line[1];// 交易时间
					String consumerNum = line[2];// 户号
					Float theMoney = Float.parseFloat(line[4]);
					
					// 将一卡通账单插入系统ApCardSaleInfo表里面
					ApCardSaleInfo apCardSaleInfo = new ApCardSaleInfo();
					apCardSaleInfo.setApCardDZID(apCardDZ_ID);
					sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date buyTime = sdf.parse(tradeTime.substring(0, tradeTime.length()-1));// tradeTime = "201804111543048";后面那个数字不要了
					sdf = null;
					apCardSaleInfo.setBuyTime(buyTime);
					apCardSaleInfo.setCardManName("齐鲁大学接口生成单");
					apCardSaleInfo.setState("正常");
					apCardSaleInfo.setTheMoney(theMoney);
					
					ApSaleInfo apSaleInfo = apSaleInfoDAO.getApSaleInfoByOrderNo(orderNo);// 查出本系统这条账单
					
					if(apSaleInfo != null){
						// 如果这条账单在系统里面存在，就顺便填上SaleInfoNum
						apCardSaleInfo.setSaleInfoNum(apSaleInfo.getSaleInfoNum());
					}
					logger.debug("向数据库ApCardSaleInfo插入数据->"+apCardSaleInfo.toString());
					apCardSaleInfoDAO.addApCardSaleInfo(apCardSaleInfo);
					Integer apCardSaleInfoID = apCardSaleInfo.getApCardSaleInfoID();
					
					logger.debug("向数据库ApCardSaleInfo插入数据->"+apCardSaleInfo.toString());
					apCardSaleInfo = null;
					
					// 漏充 情况
					if(apSaleInfo == null){
						logger.info("对账文件第"+i+"笔账单在系统中查不到，orderNo为->"+orderNo);
						apTimes--;
						// 数据库查不到该笔订单，需要向系统APCardErrorInfo插入一条对账修正记录
						String comsumerNum = line[2];
						Ammeter ammeter = ammeterDAO.getAmmeterByConsumerNum(comsumerNum);
						if(ammeter == null){
							String rtn = "第"+i+"对账文件的缴费编码->"+comsumerNum+"在系统中不存在，请检查";
							logger.debug(rtn);
							returnFailDesMap("000001", "接口参数不足，拒绝受理:"+rtn);
							break;
						}
						
						ApCardErrorInfo apCardErrorInfo = new ApCardErrorInfo();
						apCardErrorInfo.setApCardSaleInfoID(apCardSaleInfoID);
						apCardErrorInfo.setApCardDZID(apCardDZ_ID);
//						apCardErrorInfo.setSaleInfoNum(saleInfoNum);
						apCardErrorInfo.setErrorType("漏充");
						apCardErrorInfo.setBuyTime(buyTime);
						apCardErrorInfo.setExecState(0);// 0表示现在只是记录还未处理
						apCardErrorInfo.setAmMeterName(ammeter.getAmmeterName());
						apCardErrorInfoDAO.addApCardErrorInfo(apCardErrorInfo);
						Integer apCardErrorInfoID = apCardErrorInfo.getApCardErrorInfoID();
						
						logger.debug("向数据库ApCardErrorInfo插入数据->"+apCardErrorInfo.toString());
						apCardErrorInfo = null;
						
						// 准备插入漏掉的售电单
						ApSaleInfo missingApSaleInfo = new ApSaleInfo();
						long saleInfoNum = Long.parseLong(apSaleInfoDAO.getLastApSaleInfo().getSaleInfoNum())+1;
						missingApSaleInfo.setSaleInfoNum(saleInfoNum + "");
						missingApSaleInfo.setAmmeterID(ammeter.getAmmeterID());
						missingApSaleInfo.setAmmeterName(ammeter.getAmmeterName());
						missingApSaleInfo.setAmmeter_485Address(ammeter.getAmmeter485Address());
						missingApSaleInfo.setKind(1);
//						apSaleInfo.setBuyTime(tradeTimeDate);
						missingApSaleInfo.setStatus(0);
						missingApSaleInfo.setOrderNo(orderNo);
						missingApSaleInfo.setBuyTime(buyTime);
						
						Float priceValue = priceDAO.getPriceValueByPriceID(ammeter.getPriceID());
						missingApSaleInfo.setPrice(priceValue);
						Float thegross = div(theMoney,priceValue,1);
						missingApSaleInfo.setThegross(thegross);
						missingApSaleInfo.setThemoney(theMoney);
						missingApSaleInfo.setUsersName("齐鲁大学接口售电漏充");
						
						apSaleInfoDAO.addApSaleInfo(missingApSaleInfo);
						logger.debug("向数据库补发ApSaleInfo->"+missingApSaleInfo.toString());
						
						apCardErrorInfoDAO.updateExecState(1,apCardErrorInfoID);//现在更新这条对账有出入的单子，改为已经处理状态
						logger.debug("更新APCardErrorInfo表ID为"+apCardErrorInfoID+"的记录为ExecState=1");
						execTimes++;// 每处理完一个对账异常记录增加一次
						
						logger.info("对账文件第"+i+"笔账单在系统中查不到，插入售电单orderNo为->"+orderNo+",售电单号saleInfoNum->"+saleInfoNum);
//						strDesMap = returnFailDesMap("000002", "接口参数不足，拒绝受理:"+"对账文件第"+i+"笔账单在系统中查不到，orderNo为->"+orderNo);
						
						apMoney = add(apMoney,theMoney);
						rtnStr.append("第"+i+"账单系统漏单，现在补上售电单->").append(orderNo).append(",");
						
						apCardSaleInfoDAO.updateState("被冲正", apCardSaleInfoID);
						continue;
					}
					
					if(apSaleInfo.getThemoney() == null){
						// 数据库该笔订单的金额获取失败
						ApCardErrorInfo apCardErrorInfo = new ApCardErrorInfo();
						apCardErrorInfo.setApCardSaleInfoID(apCardSaleInfoID);
						apCardErrorInfo.setApCardDZID(apCardDZ_ID);
						apCardErrorInfo.setSaleInfoNum(apSaleInfo.getSaleInfoNum());
						apCardErrorInfo.setErrorType("系统缺失金额");
						apCardErrorInfo.setBuyTime(buyTime);
						apCardErrorInfo.setExecState(0);// 0表示现在只是记录还未处理
						apCardErrorInfoDAO.addApCardErrorInfo(apCardErrorInfo);
						
						logger.debug("向数据库ApCardErrorInfo插入数据->"+apCardErrorInfo.toString());
						apCardErrorInfo = null;
						
						apCardSaleInfoDAO.updateState("冲正多充", apCardSaleInfoID);
						
						logger.info("对账文件第"+i+"笔账单在系统中金额查询失败，orderNo为->"+orderNo);
						logger.info("返回错误码->000002,描述->接口参数不足，拒绝受理:"+"对账文件第"+i+"笔账单在系统中金额查询失败，orderNo为->"+orderNo);
//						strDesMap = returnFailDesMap("000002", "接口参数不足，拒绝受理:"+"对账文件第"+i+"笔账单在系统中金额查询失败，orderNo为->"+orderNo);
						rtnStr.append("对账文件第").append(i).append("笔账单在系统中金额查询失败").append(",");
						continue;
					}
					
					if(apSaleInfo.getThemoney().floatValue() != theMoney.floatValue()){
						// 数据库该笔订单金额获取成功，但是与对账文件比对不一致
						apMoney = add(apMoney,apSaleInfo.getThemoney());
						ApCardErrorInfo apCardErrorInfo = new ApCardErrorInfo();
						apCardErrorInfo.setApCardSaleInfoID(apCardSaleInfoID);
						apCardErrorInfo.setApCardDZID(apCardDZ_ID);
						apCardErrorInfo.setSaleInfoNum(apSaleInfo.getSaleInfoNum());
						apCardErrorInfo.setErrorType("冲正多充");
						apCardErrorInfo.setBuyTime(buyTime);
						apCardErrorInfo.setExecState(0);// 0表示现在只是记录还未处理
						apCardErrorInfoDAO.addApCardErrorInfo(apCardErrorInfo);
						
						logger.debug("向数据库ApCardErrorInfo插入数据->"+apCardErrorInfo.toString());
						apCardErrorInfo = null;
						
						apCardSaleInfoDAO.updateState("冲正多充", apCardSaleInfoID);
						
						logger.info("对账文件第"+i+"笔账单不正确");
						logger.info("返回错误码->000002,描述->接口参数不足，拒绝受理:对账文件第"+i+"笔金额不对");
//						strDesMap = returnFailDesMap("000002", "接口参数不足，拒绝受理:"+"对账文件第"+i+"笔账单不正确");
						rtnStr.append("对账文件第").append(i).append("笔账单不正确").append(",");
						continue;
					}
					
				}
				
				apCardDZDAO.updateApMoney(apMoney, apCardDZ_ID);
				apCardDZDAO.updateCMoney(apCardDZ_ID);
				apCardDZDAO.updateApTimes(apTimes,apCardDZ_ID);
				apCardDZDAO.updateErrorTimes(apCardDZ_ID);
				
				
				if(rtnStr.length() != 0){
					// 对账有不正常正常
					logger.info("返回错误码->000002,描述->对账有出入："+rtnStr.toString());
					strDesMap = returnFailDesMap("000002", "对账有出入："+rtnStr.toString());
				}
			}catch(Exception e){
				logger.error(e);
				logger.info("对账过程出错：可能数据库连接有问题");
				logger.info("返回错误码->000002,描述->接口参数不足，拒绝受理:对账过程出错：可能数据库连接有问题");
				strDesMap = returnFailDesMap("000002", "接口参数不足，拒绝受理:对账过程出错：可能数据库连接有问题");
			}
			
			
		}
		
		if(strDesMap == null){
			logger.info("对账成功");
			logger.info("返回错误码->000000,描述->处理完成或接收成功");
			strDesMap = returnFailDesMap("000000", "处理完成或接收成功");
		}
		
		// 加密后的strDesMap
		//加密，参数说明：strDes=密文 certPath=私钥证书路径  strKey=加密密钥 signKey=签名干扰串
		Map<String, String> strDesMapEncryption = FrontCrypt.encryptWithKey(strDesMap,encryptionPath,"12345678","12345678","");
		
		Map<String, Object> returnMap = PreCommonMethod.returnMap(jsonObject.optString(CommonParamUtils.version), 
				jsonObject.optString(CommonParamUtils.charset), 
				jsonObject.optString(CommonParamUtils.tradeNo), 
				jsonObject.optString(CommonParamUtils.merId), 
				jsonObject.optString(CommonParamUtils.rechId), 
				jsonObject.optString(CommonParamUtils.intId), 
				strDesMapEncryption.get(CommonParamUtils.strDes), strDesMapEncryption.get(CommonParamUtils.signMsg));
		//返回请求信息前先序列化map转为json串
		JSONObject returnObj = JSONObject.fromObject(returnMap);
		return returnObj.toString();
	}
	
	/**
	 * 精确除法
	 * @param v1
	 * @param v2
	 * @param scale 精确到小数点后几位
	 * @return
	 */
	public static Float div(Float v1, Float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or ");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
	
	/**
	 * 精确相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	 public static Float add(Float d1,Float d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).floatValue();
	}

	public String defalutMethod(JSONObject jsonObject, String encryptionPath,
			String decryptionPath) {
		logger.info("json请求的intId没有匹配 json->"+jsonObject.toString());
		
		Map<String,Object> strDesMap = null;//需要加密的数据
		String strDes = null;// 加密的报文
		String signMsg = null;// 请求的签名
		try{
			//得到加密后的数据 _报文
			strDes = jsonObject.optString("strDes");
			//得到证书签名
			signMsg = jsonObject.optString("signMsg");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json里面主数据缺失");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		
		logger.info("充值编码查询请求json:strDes->"+strDes);
		logger.info("充值编码查询请求json:signMsg->"+signMsg);
//		signMsg = "";
		
		//解密，解密后数据参见接口文档，参数说明：strDes=密文 signMsg=签名字符串 certPath=公钥证书路径  strKey=加密密钥 signKey=签名干扰串
		JSONObject reqData = null;
		try{
			reqData =  FrontCrypt.decryptWithKey(strDes,signMsg,decryptionPath,"12345678","");
		}catch(Exception e){
			logger.error(e);
			logger.error("返回错误码->000001,描述->接口参数不足，拒绝受理:请求的json格式或者内容不正确");
			strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:请求的json格式或者内容不正确");
		}
		logger.info("解密后报文->"+reqData.toString());
		
//		strDesMap = new HashMap<String,Object>();
//		strDesMap.put("resultCode", "000002"); // 返回码(000000为成功)
//		strDesMap.put("resultMsg", "系统错误，请联系管理员处理:本接口未开发完成"); // 返回信息
		
		logger.info("这里暂时返回码->000001,接口参数不足，拒绝受理:intId有问题");
		strDesMap = returnFailDesMap("000001", "接口参数不足，拒绝受理:intId有问题");
		strDesMap.put("detail", "接口参数不足，拒绝受理:intId有问题");
		
		// 加密后的strDesMap
		//加密，参数说明：strDes=密文 certPath=私钥证书路径  strKey=加密密钥 signKey=签名干扰串
		Map<String, String> strDesMapEncryption = FrontCrypt.encryptWithKey(strDesMap,encryptionPath,"12345678","12345678","");
		
		Map<String, Object> returnMap = PreCommonMethod.returnMap(jsonObject.optString(CommonParamUtils.version), 
				jsonObject.optString(CommonParamUtils.charset), 
				jsonObject.optString(CommonParamUtils.tradeNo), 
				jsonObject.optString(CommonParamUtils.merId), 
				jsonObject.optString(CommonParamUtils.rechId), 
				jsonObject.optString(CommonParamUtils.intId), 
				strDesMapEncryption.get(CommonParamUtils.strDes), strDesMapEncryption.get(CommonParamUtils.signMsg));
		//返回请求信息前先序列化map转为json串
		JSONObject returnObj = JSONObject.fromObject(returnMap);
		return returnObj.toString();
	}
	
}
