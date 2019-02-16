package com.lib.funsdk.support.models;

public enum FunStreamType {

	STREAM_MAIN(0, -1),
	STREAM_SECONDARY(1, -1);
	
	private int mStreamTypeId;
	private int mStreamTypeNameResId;
	
	FunStreamType(int typeId, int nameResId) {
		mStreamTypeId = typeId;
		mStreamTypeNameResId = nameResId;
	}
	
	public static FunStreamType getStramType(int typeId) {
		for ( FunStreamType streamType : FunStreamType.values() ) {
			if ( streamType.getTypeId() == typeId ) {
				return streamType;
			}
		}
		return null;
	}
	
	public int getTypeId() {
		return mStreamTypeId;
	}
	
	public int getNameResId() {
		return mStreamTypeNameResId;
	}
}
