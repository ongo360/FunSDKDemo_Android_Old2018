package com.lib.funsdk.support.config;

public class OPTimeQuery extends DevCmdGeneral{

	public static final String CONFIG_NAME = JsonConfig.OPTIME_QUERY;
	
	@Override
	public String getConfigName() {
		// TODO Auto-generated method stub
		return CONFIG_NAME;
	}
	
	@Override
	public int getJsonID() {
		return 1452;
	}

	public String OPTimeQuery;
	
	@Override
	public boolean onParse(String json) {
		if ( !super.onParse(json) ) {
			return false;
		}
		
		try {
			OPTimeQuery = mJsonObj.optString("OPTimeQuery");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public String getSendMsg() {
		return null;
	}
	
	public String getOPTimeQuery() {
		return OPTimeQuery;
	}
	public void setOPTimeQuery(String OPTimeQuery) {
		this.OPTimeQuery = OPTimeQuery;
	}
	
}
