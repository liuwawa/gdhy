package io.renren.modules.admin.controller;

import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.DataDictionariesEntity;
import io.renren.modules.admin.service.DataDictionariesService;
import io.renren.modules.oss.cloud.OSSFactory;
import io.renren.modules.oss.entity.SysOssEntity;
import io.renren.modules.oss.service.SysOssService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据字典
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-13 15:01:38
 */
@RestController
@RequestMapping("/admin/datadictionaries")
public class DataDictionariesController extends AbstractController {
	@Autowired
	private DataDictionariesService dataDictionariesService;

	@Autowired
	private SysOssService sysOssService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("admin:datadictionaries:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<DataDictionariesEntity> dataDictionariesList = dataDictionariesService.queryList(query);
		int total = dataDictionariesService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(dataDictionariesList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("admin:datadictionaries:info")
	public R info(@PathVariable("id") Long id){
		DataDictionariesEntity dataDictionaries = dataDictionariesService.queryObject(id);
		
		return R.ok().put("dataDictionaries", dataDictionaries);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("admin:datadictionaries:save")
	public R save(Integer type,String key , String value,String version){
		DataDictionariesEntity dataDictionaries=new DataDictionariesEntity();
		dataDictionaries.setValue(value);
		dataDictionaries.setType(type);
		dataDictionaries.setKey(key);
		dataDictionaries.setVersion(version);
		dataDictionaries.setCreateTime(new Date());
		dataDictionaries.setCreateUser(getUserId());
		dataDictionaries.setDelFlag(0);
		dataDictionariesService.save(dataDictionaries);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("admin:datadictionaries:update")
	public R update(Integer type,String key , String value,String version,Long id){
		DataDictionariesEntity dataDictionaries=new DataDictionariesEntity();
		dataDictionaries.setVersion(version);
		dataDictionaries.setType(type);
		dataDictionaries.setValue(value);
		dataDictionaries.setKey(key);
		dataDictionaries.setModifyTime(new Date());
		dataDictionaries.setModifyUser(getUserId());
		dataDictionaries.setId(id);
		dataDictionariesService.update(dataDictionaries);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("admin:datadictionaries:delete")
	public R delete(@RequestBody Long[] ids){
		dataDictionariesService.deleteBatch(ids);
		return R.ok();
	}

	@RequestMapping(value = "/ueConfig", produces="application/json;charset=UTF-8")
	public String ueConfig() throws FileNotFoundException {
		return "{" +
				"'imageActionName': 'fileUploadServlet'," +
				"'imageFieldName': 'file'," +
				"'imageMaxSize': 2048000," +
				"'imageAllowFiles': ['.png', '.jpg', '.jpeg']," +
				"'imageCompressEnable': true," +
				"'imageCompressBorder': 1600," +
				"'imageInsertAlign': 'none'," +
				"'imageUrlPrefix': ''," +
				"'imagePathFormat': ''," +
				"'imageManagerActionName': 'listimage'," +
				"'imageManagerListPath': '/ueditor/jsp/upload/image/'," +
				"'imageManagerListSize': 20," +
				"'imageManagerUrlPrefix': ''," +
				"'imageManagerInsertAlign': 'none'," +
				"'imageManagerAllowFiles': ['.png', '.jpg', '.jpeg', '.gif', '.bmp']" +
				"}";
	}

	@RequestMapping("/upload")
	public HashMap upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);

		HashMap<String, String> dataHashMap = new HashMap<>();
		dataHashMap.put("url", url);
		dataHashMap.put("state", "SUCCESS");
		dataHashMap.put("type", suffix.substring(1,suffix.length()));

		return  dataHashMap;
	}
}
