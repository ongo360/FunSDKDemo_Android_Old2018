package com.lib.sdk.bean;

public class BackUp {
	public String SessionID;
	public String Name;
	public OPBackupQuery OPBackupQuery;

	public class OPBackupQuery {
		public String FileFmt;
		public String StreamType;
		public int Action;
		public String Type;
		public String EndTime;
		public int Channel;
		public String Event;
		public String BeginTime;
		public String PathName;
	}
}
