package com.daotian.Http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5����
 * @author ssp
 * 2014.12.15
 */
public class MD5Util {

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * �õ��������ܺ��MD5ֵ
	 * @param inStr
	 * @return 32byte MD5 Value
	 */
	public static String getMD5(String inStr) {
		byte[] inStrBytes = inStr.getBytes();
		try {
			MessageDigest MD = MessageDigest.getInstance("MD5");
			MD.update(inStrBytes);
			byte[] mdByte = MD.digest();
			char[] str = new char[mdByte.length * 2];
			int k = 0;
			for (int i = 0; i < mdByte.length; i++) {
				byte temp = mdByte[i];
				str[k++] = hexDigits[temp >>> 4 & 0xf];
				str[k++] = hexDigits[temp & 0xf];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param inStr
	 * @param salt
	 * @param inputJson
	 * @param behaviorJson 
	 * @return 32byte MD5 Value
	 * @description ��μ���
	 * @author zhaosl
	 * @update 2012-12-28 ����6:00:46
	 */
	public static String getMD5WithSalt(String salt,String inputJson,String behaviorJson){
		System.out.println(salt+inputJson+behaviorJson);
		return getMD5(inputJson+behaviorJson).toLowerCase();
	}
}

