package com.mobpex.plug.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字体串操作相关工具类
 * 
 */
public final class Utils {

	static DecimalFormat df = null;

	/** 判断字符串是否为空 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str) /* || "null".equals(str) */;
	}

	public static boolean isNull(Object obj) {
		if (null == obj || obj == "" || obj.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(String str) {
		String reg = "\\d+(\\.\\d+)?";
		// java.util.regex.Pattern
		// pattern=java.util.regex.Pattern.compile("[0-9]*");
		Pattern pattern = Pattern.compile(reg);

		Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isInviteCode(String str) {
		if (str != null) {
			str = str.trim();
			Pattern p = Pattern.compile("[0-9a-zA-Z\u4e00-\u9fa5]+",
					Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		return false;

	}

	public static int ceil(float f) {
		int i = 0;
		if (f > 0 && f <= 1) {
			i = 1;
		} else if (f > 1 && f <= 2) {
			i = 2;
		} else if (f > 2 && f <= 3) {
			i = 3;
		} else if (f > 3 && f <= 4) {
			i = 4;
		} else if (f > 4 && f <= 5) {
			i = 5;
		}
		return i;
	}

	/** 判断字符串是否为邮箱 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher mc = pattern.matcher(str);
		return mc.matches();
	}

//	/**
//	 * 根据正则表达式 判断号码是否符合手机号码规范
//	 * 
//	 * @param mobiles
//	 *            手机号码
//	 * @return boolean 正规的手机格式返回true 不正规模式返回false
//	 */
//	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9])|(17[6-8]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();
//	}

	/**
	 * 判断字符串是否都是字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isStrAndLetter(String str) {
		boolean flag = true;
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fbf]+")
					|| str.substring(i, i + 1).matches("[a-zA-Z]")) {
			} else {
				return false;
			}
		}
		return flag;
	}

	/**
	 * <字符串截取方法　截取掉字符串最后一个字符>
	 * 
	 * @author christineRuan
	 * @date 2013-12-17 下午5:24:24
	 * @param intercept
	 * @return
	 * @returnType String
	 */
	public static String stringIntercept(String intercept) {
		return intercept.substring(0, intercept.length() - 1);
	}

	/**
	 * 
	 * <格式化double数据>
	 * 
	 * @author christineRuan
	 * @date 2014-1-20 下午8:06:10
	 * @param value
	 * @param doubleoFrmat
	 * @return
	 * @returnType String
	 */
	public static String doubleFormat(double value, String doubleoFrmat) {
		if (df == null) {
			df = new DecimalFormat(doubleoFrmat);
		}
		return df.format(value);
	}

	/**
	 * Float转String,获取2小数位字符串
	 * 
	 * @return
	 */
	public static String FloatToString(float value) {

		DecimalFormat fnum = new DecimalFormat("###,###,##0.00");
		String dd = fnum.format(value);

		return dd;

	}

	
	public static String formatNumber(String number, int fractionDigits) {
		double num = 0;
		try {
			num = Double.parseDouble(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return formatNumber(num, fractionDigits);
	}
	
	public static String formatNumber(double number, int fractionDigits) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(fractionDigits);
		format.setMinimumFractionDigits(fractionDigits);
		return format.format(number);
	}
	
	public static String toKM(String distance) {
		if (distance != null && !"".equals(distance)) {
			if (Double.valueOf(distance).doubleValue() >= 1000) {
				NumberFormat nf = new DecimalFormat("0.0");
				return Double.parseDouble(nf.format(Double.valueOf(distance)
						.doubleValue() / 1000)) + "公里";
			} else {
				return (int) Math.ceil(Double.valueOf(distance).doubleValue())
						+ "米";
			}
		} else {
			return "未设置";
		}
	}

	/**
	 * 
	 * TODO 判断是否为URL
	 * 
	 * @FileName AppUtil.java
	 * @author Simon.xin
	 */
	public static boolean isUrl(String url) {
		// ^((https|http|ftp|rtsp|mms)?://)
		// String regex =
		// "^(https|http|ftp|rtsp|mms|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		// String regex =
		// "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		// Pattern patt = Pattern.compile(regex);
		// Matcher matcher = patt.matcher(url);
		// boolean isMatch = matcher.matches();
		// if (!isMatch) {
		// return false;
		// } else {
		// return true;
		// }
		if (url.indexOf("http") > -1) {
			return true;
		}
		if (url.indexOf("www") > -1) {
			return true;
		}
		if (url.indexOf("com") > -1) {
			return true;
		}
		if (url.indexOf("cn") > -1) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> getParamFromUrl(String url) {
		ArrayList<String> list = null;
		if (isNull(url)) {
			return list;
		}
		try {
			String[] item = url.split("\\?");
			if (isNull(item)) {
				return list;
			}
			String[] params = item[1].split("&");
			if (isNull(params)) {
				return list;
			}
			list = new ArrayList<String>();
			for (int i = 0; i < params.length; i++) {
				list.add(params[i].split("=")[1]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	 
	
	
}
