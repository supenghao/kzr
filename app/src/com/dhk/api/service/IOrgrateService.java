package com.dhk.api.service;

/**
 * t_s_org_rate service 接口<br/>
 * 2017-02-11 06:36:13 qch
 */
public interface IOrgrateService {

	boolean updateDiffRateInfo(String orgid, String qrcodeid, Double maxrate);
}
