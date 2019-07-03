package com.example.funsdkdemo.entity;

public class ResolutionInfo {

	public static int GetIndex(String resolution) {
		final String D1 = "D1";
		final String HD1 = "HD1";
		final String BCIF = "BCIF";
		final String CIF = "CIF";
		final String QCIF = "QCIF";
		final String VGA = "VGA";
		final String QVGA = "QVGA";
		final String SVCD = "SVCD";
		final String QQVGA = "QQVGA";
		final String ND1 = "ND1";
		final String _650TVL = "650TVL";
		final String _720P = "720P";
		final String _1_3M = "1_3M";
		final String UXGA = "UXGA";
		final String _1080P = "1080P";
		final String WUXGA = "WUXGA";
		final String _2_5M = "2_5M";
		final String _3M = "3M";
		final String _4M = "4M";
		final String _5M = "5M";
		final String _1080N = "1080N";

		if (resolution.equals(D1)) {
			return 0;
		} else if (resolution.equals(HD1)) {
			return 1;
		} else if (resolution.equals(BCIF)) {
			return 2;
		} else if (resolution.equals(CIF)) {
			return 3;
		} else if (resolution.equals(QCIF)) {
			return 4;
		} else if (resolution.equals(VGA)) {
			return 5;
		} else if (resolution.equals(QVGA)) {
			return 6;
		} else if (resolution.equals(SVCD)) {
			return 7;
		} else if (resolution.equals(QQVGA)) {
			return 8;
		} else if (resolution.equals(ND1)) {
			return 9;
		} else if (resolution.equals(_650TVL)) {
			return 10;
		} else if (resolution.equals(_720P)) {
			return 11;
		} else if (resolution.equals(_1_3M)) {
			return 12;
		} else if (resolution.equals(UXGA)) {
			return 13;
		} else if (resolution.equals(_1080P)) {
			return 14;
		} else if (resolution.equals(WUXGA)) {
			return 15;
		} else if (resolution.equals(_2_5M)) {
			return 16;
		} else if (resolution.equals(_3M)) {
			return 17;
		}  else if (resolution.equals(_5M)) {
			return 18;
		}else if (resolution.equals(_1080N)) {
			return 19;
		}else if (resolution.equals(_4M)) {
			return 20;
		} else {
			return 0;
		}
	}

	public static String GetString(int i) {
		final String D1 = "D1";
		final String HD1 = "HD1";
		final String BCIF = "BCIF";
		final String CIF = "CIF";
		final String QCIF = "QCIF";
		final String VGA = "VGA";
		final String QVGA = "QVGA";
		final String SVCD = "SVCD";
		final String QQVGA = "QQVGA";
		final String ND1 = "ND1";
		final String _650TVL = "650TVL";
		final String _720P = "720P";
		final String _1_3M = "1_3M";
		final String UXGA = "UXGA";
		final String _1080P = "1080P";
		final String WUXGA = "WUXGA";
		final String _2_5M = "2_5M";
		final String _3M = "3M";
		final String _4M = "4M";
		final String _5M = "5M";
		final String _1080N = "1080N";

		switch (i) {
		case 0:
			return D1;
		case 1:
			return HD1;
		case 2:
			return BCIF;
		case 3:
			return CIF;
		case 4:
			return QCIF;
		case 5:
			return VGA;
		case 6:
			return QVGA;
		case 7:
			return SVCD;
		case 8:
			return QQVGA;
		case 9:
			return ND1;
		case 10:
			return _650TVL;
		case 11:
			return _720P;
		case 12:
			return _1_3M;
		case 13:
			return UXGA;
		case 14:
			return _1080P;
		case 15:
			return WUXGA;
		case 16:
			return _2_5M;
		case 17:
			return _3M;
		case 18:
			return _5M;
		case 19:
			return _1080N;
		case 20:
			return _4M;
		default:
			return D1;
		}
	}

}
