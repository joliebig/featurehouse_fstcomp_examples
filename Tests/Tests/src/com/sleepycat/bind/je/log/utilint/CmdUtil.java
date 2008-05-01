package com.sleepycat.je.utilint; 
import java.io.File; 
import com.sleepycat.je.DatabaseException; 
import com.sleepycat.je.EnvironmentConfig; 
import com.sleepycat.je.config.EnvironmentParams; 
import com.sleepycat.je.dbi.EnvironmentImpl; 
import de.ovgu.cide.jakutil.*; 
public  class  CmdUtil {
	 public static String getArg( String[] argv, int whichArg) throws IllegalArgumentException { if (whichArg < argv.length) { return argv[whichArg]; } else { throw new IllegalArgumentException(); } }

	 public static long readLongNumber( String longVal){ if (longVal.startsWith("0x")) { return Long.parseLong(longVal.substring(2),16); } else { return Long.parseLong(longVal); } }

	 private static final String printableChars="!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	 public static void formatEntry( StringBuffer sb, byte[] entryData, boolean formatUsingPrintable){ for (int i=0; i < entryData.length; i++) { int b=entryData[i] & 0xff; if (formatUsingPrintable) { if (isPrint(b)) { if (b == 0134) { sb.append('\\'); } sb.append(printableChars.charAt(b - 33)); } else { sb.append('\\'); String hex=Integer.toHexString(b); if (b < 16) { sb.append('0'); } sb.append(hex); } } else { String hex=Integer.toHexString(b); if (b < 16) { sb.append('0'); } sb.append(hex); } } }

	 private static boolean isPrint( int b){ return (b < 0177) && (040 < b); }

	 public static EnvironmentImpl makeUtilityEnvironment( File envHome, boolean readOnly) throws DatabaseException { EnvironmentConfig config=new EnvironmentConfig(); config.setReadOnly(readOnly); hook853(config); hook854(config); hook855(config); config.setConfigParam(EnvironmentParams.ENV_RECOVERY.getName(),"false"); EnvironmentImpl envImpl=new EnvironmentImpl(envHome,config); return envImpl; }

	 public static String getJavaCommand( Class cls){ String clsName=cls.getName(); String lastName=clsName.substring(clsName.lastIndexOf('.') + 1); return "java { " + cls.getName() + " | -jar je.jar "+ lastName+ " }"; }

	 protected static void hook853( EnvironmentConfig config) throws DatabaseException { }

	 protected static void hook854( EnvironmentConfig config) throws DatabaseException { }

	 protected static void hook855( EnvironmentConfig config) throws DatabaseException { }


}
