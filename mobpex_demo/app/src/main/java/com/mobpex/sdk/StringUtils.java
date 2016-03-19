package com.mobpex.sdk;

public class StringUtils {
	public static boolean isBlank(String str){
		if(str==null ||str.equals(""))return true;
		
		else return false;
	}
	public static String trimToEmpty(String str){
		return str==null?"":str.trim();
	}
	public static String getMimeType(String suffix){
		 
		String mimeType;

		if ("JPG".equals(suffix)) {
			mimeType = "image/jpeg";
		} else if ("GIF".equals(suffix)) {
			mimeType = "image/gif";
		} else if ("PNG".equals(suffix)) {
			mimeType = "image/png";
		} else if ("BMP".equals(suffix)) {
			mimeType = "image/bmp";
		}else {
			mimeType = "application/octet-stream";
		}

		return mimeType;
		
	}
	public static String substringBetween(String str,String open,String close){
		 if (str == null || open == null || close == null) {
	            return null;
	        }
	        int start = str.indexOf(open);
	        if (start != -1) {
	            int end = str.indexOf(close, start + open.length());
	            if (end != -1) {
	                return str.substring(start + open.length(), end);
	            }
	        }
	        return null;
		
	}
	
	public static String substringBeforeLast(String str,String separator){
		  if (isBlank(str) || isBlank(separator)) {
	            return str;
	        }
	        int pos = str.lastIndexOf(separator);
	        if (pos == -1) {
	            return str;
	        }
	        return str.substring(0, pos);
		
	}
	public static String substringAfter(String source,String fromStr){
		
		 if (isBlank(source)) {
	            return source;
	        }
	        if (fromStr == null) {
	            return fromStr;
	        }
	        int pos = source.indexOf(fromStr);
	        if (pos == -1) {
	            return "";
	        }
	        return source.substring(pos + fromStr.length());
	        
		  
		
	}
	
}
