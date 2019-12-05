package com.lz.framework.tools.constants;

public class Constants {
	
	public static final class CHARSET {
		
		public static final String UTF_8 = "UTF-8";
		
		public static final String GB2312 = "GB2312";
		
		public static final String GBK = "GBK";
		
		public static final String ISO8859_1 = "ISO8859-1";
		
	}
	
	public static final class DATE_FORMAT {
		
		public static final String DATE_FORMAT0 = "yyyy-MM-dd HH:mm:ss";
		
		public static final String DATE_FORMAT1 = "yyyy-MM-dd";
		
		public static final String DATE_FORMAT2 = "yyyy年MM月dd日";
		
		public static final String DATE_FORMAT3 = "yyMMdd";
		
		public static final String DATE_FORMAT4 = "yyyyMMdd";
	}

    public static final class CHAR {

        public static final String MASK_CHAR = "*";

        public static final String LIKE_CHAR = "%";

        public static final String SLANT_CHAR = "/";

        public static final String POINT_CHAR = ".";

        public static final String UNDERLINE_CHAR = "_";

        public static final String MIDDLELINE_CHAR = "-";

        public static final String AND_CHAR = "&";

        public static final String QUESTION_CHAR = "?";
        
        public static final String COMMA_CHAR = ",";
        
        public static final String WELL_CHAR = "#";
        
        public static final String EQUAL_CHAR = "=";
        
        public static final String SEMICOLON_CHAR = ";";

		public static final String SQL_PATTEN_KEY = "\\#(.*?)\\#";
		
		public static final String MASK = "[^ -()]";
		
		public static final String LEFT_PARENTHESE = "(";
		
		public static final String RIGHT_PARENTHESE = ")";
    }
	
    public static final class IS {
        // 是
        public static final String YES = "1";
        // 否
        public static final String NO = "0";
    }
    
    public static class MIME_TYPE {
        public static final String FORM = "application/x-www-form-urlencoded";
        public static final String JSON = "application/json";
        public static final String XML = "application/xml";
        public static final String PDF = "application/pdf";
        public static final String HTML = "text/html";
        public static final String PLAIN = "text/plain";
        public static final String IMAGE = "image/*";
        public static final String DOC = "application/msword";
        public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    public static final class RESPONSE {
        public static final String CODE = "code";
        public static final String MESSAGE = "message";
        public static final String DATA = "data";
        public static final String ITEMS = "items";
    }
    
}