public class UnicodeTransDemo {
	private static String unicode2Cn(String unicode){
		String [] strs = unicode.split("\\\\u");
		String returnStr = "";
		// 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
		for (int i = 1; i < strs.length; i++) {
			returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
		}
		return returnStr;
	}

	private static String cnToUnicode(String cn) {
		char[] chars = cn.toCharArray();
		String returnStr = "";
		for (int i = 0; i < chars.length; i++) {
			returnStr += "\\u" + Integer.toString(chars[i], 16);
		}
		return returnStr;
	}

	public static void main(String[] args) {
		String cn = "这里填写备注信息";
		System.out.println(cnToUnicode(cn));
		String unicode = "\\u8fd9\\u91cc\\u586b\\u5199\\u5907\\u6ce8\\u4fe1\\u606f";
		System.out.println(unicode2Cn(unicode));
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
