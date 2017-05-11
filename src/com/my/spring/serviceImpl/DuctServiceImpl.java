package com.my.spring.serviceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.my.spring.DAO.DuctDao;
import com.my.spring.DAO.DuctLogDao;
import com.my.spring.DAO.ProjectDao;
import com.my.spring.DAO.UserDao;
import com.my.spring.enums.ErrorCodeEnum;
import com.my.spring.enums.UserTypeEnum;
import com.my.spring.model.Duct;
import com.my.spring.model.DuctLog;
import com.my.spring.model.DuctPojo;
import com.my.spring.model.User;
import com.my.spring.service.DuctService;
import com.my.spring.service.FileService;
import com.my.spring.utils.DataWrapper;
import com.my.spring.utils.FileOperationsUtil;
import com.my.spring.utils.MD5Util;
import com.my.spring.utils.SessionManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/6/22.
 */
@Service("ductService")
public class DuctServiceImpl implements DuctService {
    @Autowired
    DuctDao DuctDao;
    @Autowired
    DuctLogDao ductLogDao;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    UserDao userDao;
    @Autowired
    FileService fileSerivce;
    @Override
    public DataWrapper<Void> addDuct(Duct Duct,String token) {
        DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
        User userInMemory = SessionManager.getSession(token);
        if (userInMemory != null) {
			if(userInMemory.getUserType()==UserTypeEnum.Admin.getType()){
				if(Duct!=null){
					if(!DuctDao.addDuct(Duct)) 
			            dataWrapper.setErrorCode(ErrorCodeEnum.Error);
					else
						return dataWrapper;
			        
				}else{
					dataWrapper.setErrorCode(ErrorCodeEnum.Empty_Inputs);
				}
			}else{
				dataWrapper.setErrorCode(ErrorCodeEnum.AUTH_Error);
			}
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> deleteDuct(Long id,String token) {
        DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
        User userInMemory = SessionManager.getSession(token);
        if (userInMemory != null) {
			if(userInMemory.getUserType()==UserTypeEnum.Admin.getType()){
				if(id!=null){
					DataWrapper<List<DuctLog>> ductlist=ductLogDao.getDuctLogByDuctId(id);
					if(ductlist.getData()!=null){
						for(int i=0;i<ductlist.getData().size();i++){
							ductLogDao.deleteDuctLog(ductlist.getData().get(i).getId());
						}
					}
					if(!DuctDao.deleteDuct(id)) 
			            dataWrapper.setErrorCode(ErrorCodeEnum.Error);
					else
						return dataWrapper;
			        
				}else{
					dataWrapper.setErrorCode(ErrorCodeEnum.Empty_Inputs);
				}
			}else{
				dataWrapper.setErrorCode(ErrorCodeEnum.AUTH_Error);
			}
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> updateDuct(Duct Duct,String token) {
        DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
        User userInMemory = SessionManager.getSession(token);
        if (userInMemory != null) {
			if(userInMemory.getUserType()==UserTypeEnum.Admin.getType()){
 				if(Duct!=null){
					if(!DuctDao.updateDuct(Duct)) 
			            dataWrapper.setErrorCode(ErrorCodeEnum.Error);
					else
						return dataWrapper;
			        
				}else{
					dataWrapper.setErrorCode(ErrorCodeEnum.Empty_Inputs);
				}
			}else{
				dataWrapper.setErrorCode(ErrorCodeEnum.AUTH_Error);
			}
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return dataWrapper;
    }

    @Override
    public DataWrapper<List<DuctPojo>> getDuctList(Integer pageIndex,Integer pageSize,Duct duct,String token,String content) {
    	List<Duct> ductList=new  ArrayList<Duct>();
    	List<DuctPojo> ductPojoList = new ArrayList<DuctPojo>();
    	DataWrapper<List<DuctPojo>> ductLists=new  DataWrapper<List<DuctPojo>>();
    	DataWrapper<List<Duct>> ductListst=new  DataWrapper<List<Duct>>();
    	User userInMemory = SessionManager.getSession(token);
    	if(userInMemory!=null){
    		ductListst=DuctDao.getDuctList(pageSize,pageIndex,duct,content);
    		ductList=ductListst.getData();
    		if(ductList!=null && ductList.size()>0){
    			String[] stateList=new String[]{"未定义","出库","安装","完成"};
    	    	for(int i=0;i<ductList.size();i++){
    	    		DuctPojo ductPojo=new DuctPojo();
    	    		ductPojo.setCodeUrl(ductList.get(i).getCodeUrl());
    	    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	    		String str=sdf.format(ductList.get(i).getDate()); 
    	    		ductPojo.setDate(str);
	    			ductPojo.setFamilyAndType(ductList.get(i).getFamilyAndType());
    	    		ductPojo.setId(ductList.get(i).getId().toString());
	    			ductPojo.setLevel(ductList.get(i).getLevel());
    	    		ductPojo.setName(ductList.get(i).getName());
    	    		ductPojo.setProjectId(ductList.get(i).getProjectId().toString());
    	    		ductPojo.setSelfId(ductList.get(i).getSelfId());
    	    		if(ductList.get(i).getState()!=null){
    	    			ductPojo.setState(stateList[ductList.get(i).getState()]);	
    	    		}else{
    	    			ductPojo.setState(stateList[0]);
    	    		}
    	    		ductPojo.setServiceType(ductList.get(i).getServiceType());
    	    		if(ductList.get(i).getUserId()!=null){
    	    			User user=userDao.getById(ductList.get(i).getUserId());
    	    			if(user.getUserName()!=null){
    	    				ductPojo.setUserName(user.getUserName());
    	    			}
    	    		}
    	    		ductPojoList.add(ductPojo);
    	    	}
    	    	ductLists.setData(ductPojoList);
        		ductLists.setData(ductPojoList);
        		ductLists.setTotalNumber(ductListst.getTotalNumber());
        		ductLists.setCurrentPage(ductListst.getCurrentPage());
        		ductLists.setTotalPage(ductListst.getTotalPage());
        		ductLists.setNumberPerPage(ductListst.getNumberPerPage());
    		}else{
    			ductLists.setErrorCode(ErrorCodeEnum.Error);
    		}
    	}else{
    		ductLists.setErrorCode(ErrorCodeEnum.User_Not_Logined);
    	}
        return ductLists;
    }

	@Override
	public DataWrapper<List<DuctPojo>> getDuctByProjectId(Long projectId,String token,Duct duct) {
		// TODO Auto-generated method stub
		DataWrapper<List<DuctPojo>> dataWrapper = new DataWrapper<List<DuctPojo>>();
		List<Duct> ductList = new ArrayList<Duct>();
		List<DuctPojo> ductPojoList = new ArrayList<DuctPojo>();
        User userInMemory = SessionManager.getSession(token);
        //String strs2="";
        if (userInMemory != null) {
        	ductList=DuctDao.getDuctByProjectId(projectId,duct).getData();
        	String[] stateList=new String[]{"未定义","出库","安装","完成"};
        	if(ductList!=null){
        		String projectName=projectDao.getById(ductList.get(0).getProjectId()).getName();
            	for(int i=0;i<ductList.size();i++){
            		///////////////////////
        			DuctPojo ductPojo=new DuctPojo();
        			ductPojo.setId(ductList.get(i).getId().toString());
        			ductPojo.setCodeUrl(ductList.get(i).getCodeUrl());
            		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            		String str=sdf.format(ductList.get(i).getDate()); 
            		ductPojo.setDate(str);
            		ductPojo.setSize(ductList.get(i).getSize());
            		ductPojo.setState(stateList[ductList.get(i).getState()]);
            		ductPojo.setName(ductList.get(i).getName());
            		ductPojo.setFamilyAndType(ductList.get(i).getFamilyAndType());
            		ductPojo.setLevel(ductList.get(i).getLevel());
            		ductPojo.setBuildingNum(ductList.get(i).getBuildingNum().toString());
            		ductPojo.setFloorNum(ductList.get(i).getFloorNum().toString());
            		ductPojo.setUnitNum(ductList.get(i).getUnitNum().toString());
            		ductPojo.setHouseholdNum(ductList.get(i).getHouseholdNum().toString());
            		ductPojo.setProjectId(ductList.get(i).getProjectId().toString());
            		ductPojo.setProjectName(projectName);
            		ductPojo.setSelfId(ductList.get(i).getSelfId());
            		ductPojo.setArea(ductList.get(i).getArea()+"");
            		ductPojo.setLength(ductList.get(i).getLength()+"");
            		ductPojo.setServiceType(ductList.get(i).getServiceType());
            		ductPojo.setSystemType(ductList.get(i).getSystemType());
            		ductPojo.setModelFlag(ductList.get(i).getModelFlag());
            		if(ductList.get(i).getUserId()!=null){
            			User user=userDao.getById(ductList.get(i).getUserId());
            			if(user.getUserName()!=null){
            				ductPojo.setUserName(user.getUserName());
            			}
            		}
            		ductPojoList.add(i,ductPojo);
            	}
            	dataWrapper.setData(ductPojoList);
        	}else{
        		dataWrapper.setErrorCode(ErrorCodeEnum.Target_Not_Existed);
        	}
		}else{
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return dataWrapper;
	}
	
	@Override
	public DataWrapper<DuctPojo> getDuctBySelfId(Long selfId) {
		// TODO Auto-generated method stub
		DataWrapper<DuctPojo> dataWrapper = new DataWrapper<DuctPojo>();
		Duct duct = new Duct();
    	duct=DuctDao.getDuctBySelfId(selfId).getData();
    	String[] stateList=new String[]{"未定义","出库","安装","完成"};
    	if(duct!=null){
    		String projectName=projectDao.getById(duct.getProjectId()).getName();
			DuctPojo ductPojo=new DuctPojo();
			ductPojo.setId(duct.getId().toString());
			ductPojo.setCodeUrl(duct.getCodeUrl());
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    		String str=sdf.format(duct.getDate()); 
    		ductPojo.setDate(str);
    		ductPojo.setSize(duct.getSize());
    		ductPojo.setState(stateList[duct.getState()]);
    		ductPojo.setName(duct.getName());
    		ductPojo.setFamilyAndType(duct.getFamilyAndType());
    		ductPojo.setLevel(duct.getLevel());
    		ductPojo.setBuildingNum(duct.getBuildingNum().toString());
    		ductPojo.setFloorNum(duct.getFloorNum().toString());
    		ductPojo.setUnitNum(duct.getUnitNum().toString());
    		ductPojo.setHouseholdNum(duct.getHouseholdNum().toString());
    		ductPojo.setProjectId(duct.getProjectId().toString());
    		ductPojo.setProjectName(projectName);
    		ductPojo.setSelfId(duct.getSelfId());
    		ductPojo.setArea(duct.getArea()+"");
    		ductPojo.setLength(duct.getLength()+"");
    		ductPojo.setServiceType(duct.getServiceType());
    		ductPojo.setSystemType(duct.getSystemType());
    		if(duct.getUserId()!=null){
    			User user=userDao.getById(duct.getUserId());
    			if(user.getUserName()!=null){
    				ductPojo.setUserName(user.getUserName());
    			}
    		}
        	dataWrapper.setData(ductPojo);
    	}else{
    		dataWrapper.setErrorCode(ErrorCodeEnum.Target_Not_Existed);
    	}
        return dataWrapper;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean batchImport(String name, MultipartFile file,String token,HttpServletRequest request,Long projectId) {
		if (file == null || name == null || name.equals("")) {
			return false;
		}
		int type=7;
		DataWrapper<Void> dataWrapper=new DataWrapper<Void>();
		boolean b = false;
		User userInMemory = SessionManager.getSession(token);
		if(userInMemory!=null){
			if(userInMemory.getUserType()==UserTypeEnum.Admin.getType()){
		        String newFileName = MD5Util.getMD5String(file.getOriginalFilename() + new Date() + UUID.randomUUID().toString()).replace(".","")
		                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		        //创建处理EXCEL
		        ReadExcelOfDuct readExcel=new ReadExcelOfDuct();
		        
		        //解析excel，构件信息集合。
		        List<Duct> DuctList = readExcel.getExcelInfo(newFileName ,file,projectId);
		        
		        if(DuctList.size()>0){
		        	for(int i=0;i<DuctList.size();i++){
		        		String codeInformation=null;
		        		
		        		codeInformation="http://139.224.59.3:8080/jasobim/page/ductInfo.html?selfId="+DuctList.get(i).getSelfId();
		        		SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMddHHmmssSSS" );
		        	   	Date d=new Date();
		        	   	String str=sdf.format(d);
		        	   	String rootPath = request.getSession().getServletContext().getRealPath("/");
		        	   	String filePath="/codeFiles";
		        	   	String imgpath=rootPath+filePath;
		        	   	String realPath=rootPath+filePath+"/"+str+".png";		        	   	
		        	   	DuctList.get(i).setDate(new Date());
						/////添加上传的用户id
						DuctList.get(i).setUserId(userInMemory.getId());

		        		try{
		        		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		        	        @SuppressWarnings("rawtypes")
		        			Map hints = new HashMap();  
		        	        //内容所使用编码  
		        	        hints.put(EncodeHintType.CHARACTER_SET, "utf8");  
		        	        BitMatrix bitMatrix = multiFormatWriter.encode(codeInformation,BarcodeFormat.QR_CODE, 200, 200, hints);  
		        	        //生成二维码  
		        	        File outputFile = new File(imgpath,str+".png"); 
		        	        
		        	        MatrixToImageWriter.writeToFile(bitMatrix, "png", outputFile);  
		        		} catch (Exception e) {
		        			e.printStackTrace();
		        		}
		        		DuctList.get(i).setCodeUrl(realPath);
		        	}
		        	
		        	b = true;
		        	fileSerivce.uploadFile(name, file, type, request);
		            //迭代添加构件信息
		        	DuctDao.addDuctList(DuctList);
		        }
		      
			}else{
				dataWrapper.setErrorCode(ErrorCodeEnum.AUTH_Error);
			}
			
		}else{
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return b;
	}
	@Override
	public DataWrapper<String> exportDuct(Long projectId, String token, HttpServletRequest request,String dateStart,String dateFinished) {
		// TODO Auto-generated method stub
		DataWrapper<String> dataWrapper = new DataWrapper<String>();
		if (projectId == null) {
			dataWrapper.setErrorCode(ErrorCodeEnum.Empty_Inputs);
		}
		User userInMemory = SessionManager.getSession(token);
		if(userInMemory!=null) {
			
				String filePath = "E:/JasoBim/BimAppDocument/apache-tomcat-8.0.39/webapps/jasobim" + "/out/" + projectId + "/duct/";
				File fileDir = new File(filePath);
		        if (!fileDir.exists()) {
		            fileDir.mkdirs();
		        }
		        String tempFile = filePath + "duct_temp.xls";
		        String file = filePath + "duct.xls";
		        String header = "序号\t"
		        		+ "名称\t"
		        		+ "个数\t"
		        		+ "长度\t"
		        		+ "面积\t"
		        		+ "项目id\t"
		        		+ "楼栋号\t"
		        		+ "familyAndType\t"
		        		+ "尺寸\t"
		        		+ "项目名称\t"
		        		+ "时间\n";
		        FileOperationsUtil.deleteFile(tempFile);
		        FileOperationsUtil.deleteFile(file);
		        if (DuctDao.exportDuct(tempFile, projectId,dateStart,dateFinished)) {
					String content = FileOperationsUtil.readFile(tempFile);
					String newContent = header + content;
					FileOperationsUtil.writeFile(file, newContent, false);
					dataWrapper.setData("out/" + projectId +"/duct" +"/duct.xls");
				} else {
					dataWrapper.setErrorCode(ErrorCodeEnum.Error);
				}
			
			
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
			
		return dataWrapper;
	}

	@Override
    public DataWrapper<Void> updateDuct(Duct duct,String token,HttpServletRequest request) {
        DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
        User userInMemory = SessionManager.getSession(token);
        if (userInMemory != null) {
			if(duct.getId()!=null){
				Duct ducts = new Duct();
				ducts=DuctDao.getDuctById(duct.getId());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    Date date;
				try {
					date = sdfs.parse(df.format(new Date()));
					ducts.setDate(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				ducts.setState(duct.getState());
				ducts.setUserId(userInMemory.getId());
				if(ducts!=null){
					if(!DuctDao.updateDuct(ducts)){
						dataWrapper.setErrorCode(ErrorCodeEnum.Error);
					}
				}
			}else{
				dataWrapper.setErrorCode(ErrorCodeEnum.Empty_Inputs);
			}
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.User_Not_Logined);
		}
        return dataWrapper;
    }

		
}