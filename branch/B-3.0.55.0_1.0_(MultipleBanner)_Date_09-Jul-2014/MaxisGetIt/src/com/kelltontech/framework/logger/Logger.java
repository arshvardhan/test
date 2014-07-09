package com.kelltontech.framework.logger;

import android.util.Log;

public class Logger {

	public static final short LEVEL_VERBOSE = 0;
	public static final short LEVEL_DEBUG = 1;
	public static final short LEVEL_INFO = 2;
	public static final short LEVEL_WARNNING = 3;
	public static final short LEVEL_ERROR = 4;

	public static void easyLog(short level, String fromClass, String message) {

		System.out.println("--From Class " + fromClass + "Message  " + message);
		switch (level) {
		case LEVEL_VERBOSE:
			Log.v(fromClass, message);
			break;
		case LEVEL_DEBUG:
			Log.d(fromClass, message);
			break;
		case LEVEL_INFO:
			Log.i(fromClass, message);
			break;
		case LEVEL_WARNNING:
			Log.w(fromClass, message);
			break;
		case LEVEL_ERROR:
			Log.e(fromClass, message);
		default:
			Log.v(fromClass, message);

		}
	}

	public static void easyLog(String message, Throwable e) {
		StringBuffer methodCreatedException = new StringBuffer();

		if (null != e) {
			// Get the stack trace
			StackTraceElement stack[] = e.getStackTrace();
			// stack[0] contains the method that created the exception.
			// stack[stack.length-1] contains the oldest method call.
			// Enumerate each stack element.
			if (null != stack && stack.length > 0)
				methodCreatedException.append(" >> Error Occured in File ");
			methodCreatedException.append(stack[0].getFileName());
			methodCreatedException.append("  having class name : ");
			methodCreatedException.append(stack[0].getClassName());
			methodCreatedException.append("  in Line : ");
			methodCreatedException.append(stack[0].getLineNumber());
			methodCreatedException.append("  in metod : ");
			methodCreatedException.append(stack[0].getMethodName());
		}

		// if(null!=mContext)
		// Toast.makeText(mContext, methodCreatedException, Toast.LENGTH_LONG);

		Log.e(message, methodCreatedException.toString(), e);

	}
}