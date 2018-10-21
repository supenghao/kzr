package com.dhk.service;
import java.util.List;

import com.dhk.entity.OrgRate;

   /**
    * t_s_org_rate service 接口<br/>
    * 2017-02-09 11:04:21 Gnaily
    */ 
public interface IOrgRateService {
	/**
	 * 根据二维码ID查询OrgRate
	 * @param id 二维码ID
	 * @return
	 */
	public List<OrgRate> findByQrcodeId(long id);
	
	/**
	 * 根据二维码ID和代理商ID查询
	 * @param qrcodeId
	 * @param orgId
	 * @return
	 */
	public List<OrgRate> findBy(long qrcodeId, long orgId);
	/**
	 * 更新OrgRate
	 * @param orgRate
	 * @return
	 */
	public int update(OrgRate orgRate);
	/**
	 * 插入代理商费率信息
	 * @param orgRate 代理商费率信息
	 * @return
	 */
	public long insert(OrgRate orgRate);


   /**
	* 用二维码ID查询id最小的记录
	* @param QRCODE_ID
	* @return
	* @throws Exception
	*/
   public OrgRate findByQrcodeIdAndMinId(Long QRCODE_ID) throws Exception;
}

