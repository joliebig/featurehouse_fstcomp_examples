package com.sleepycat.je.log; 
import java.util.HashSet; 
import java.util.Set; 
import com.sleepycat.je.DatabaseException; 
import com.sleepycat.je.log.entry.BINDeltaLogEntry; 
import com.sleepycat.je.log.entry.DeletedDupLNLogEntry; 
import com.sleepycat.je.log.entry.INLogEntry; 
import com.sleepycat.je.log.entry.LNLogEntry; 
import com.sleepycat.je.log.entry.LogEntry; 
import com.sleepycat.je.log.entry.SingleItemLogEntry; 
import de.ovgu.cide.jakutil.*; 
public  class  LogEntryType {
	 private static final int MAX_TYPE_NUM=27;

	 private static LogEntryType[] LOG_TYPES=new LogEntryType[MAX_TYPE_NUM];

	 public static final LogEntryType LOG_LN_TRANSACTIONAL=new LogEntryType((byte)1,(byte)0,"LN_TX",new LNLogEntry(com.sleepycat.je.tree.LN.class,true));

	 public static final LogEntryType LOG_LN=new LogEntryType((byte)2,(byte)0,"LN",new LNLogEntry(com.sleepycat.je.tree.LN.class,false));

	 public static final LogEntryType LOG_MAPLN_TRANSACTIONAL=new LogEntryType((byte)3,(byte)1,"MapLN_TX",new LNLogEntry(com.sleepycat.je.tree.MapLN.class,true));

	 public static final LogEntryType LOG_MAPLN=new LogEntryType((byte)4,(byte)1,"MapLN",new LNLogEntry(com.sleepycat.je.tree.MapLN.class,false));

	 public static final LogEntryType LOG_NAMELN_TRANSACTIONAL=new LogEntryType((byte)5,(byte)0,"NameLN_TX",new LNLogEntry(com.sleepycat.je.tree.NameLN.class,true));

	 public static final LogEntryType LOG_NAMELN=new LogEntryType((byte)6,(byte)0,"NameLN",new LNLogEntry(com.sleepycat.je.tree.NameLN.class,false));

	 public static final LogEntryType LOG_DEL_DUPLN_TRANSACTIONAL=new LogEntryType((byte)7,(byte)0,"DelDupLN_TX",new DeletedDupLNLogEntry(true));

	 public static final LogEntryType LOG_DEL_DUPLN=new LogEntryType((byte)8,(byte)0,"DelDupLN",new DeletedDupLNLogEntry(false));

	 public static final LogEntryType LOG_DUPCOUNTLN_TRANSACTIONAL=new LogEntryType((byte)9,(byte)0,"DupCountLN_TX",new LNLogEntry(com.sleepycat.je.tree.DupCountLN.class,true));

	 public static final LogEntryType LOG_DUPCOUNTLN=new LogEntryType((byte)10,(byte)0,"DupCountLN",new LNLogEntry(com.sleepycat.je.tree.DupCountLN.class,false));

	 public static final LogEntryType LOG_FILESUMMARYLN=new LogEntryType((byte)11,(byte)2,"FileSummaryLN",new LNLogEntry(com.sleepycat.je.tree.FileSummaryLN.class,false));

	 public static final LogEntryType LOG_IN=new LogEntryType((byte)12,(byte)2,"IN",new INLogEntry(com.sleepycat.je.tree.IN.class));

	 public static final LogEntryType LOG_BIN=new LogEntryType((byte)13,(byte)2,"BIN",new INLogEntry(com.sleepycat.je.tree.BIN.class));

	 public static final LogEntryType LOG_DIN=new LogEntryType((byte)14,(byte)2,"DIN",new INLogEntry(com.sleepycat.je.tree.DIN.class));

	 public static final LogEntryType LOG_DBIN=new LogEntryType((byte)15,(byte)2,"DBIN",new INLogEntry(com.sleepycat.je.tree.DBIN.class));

	 public static final LogEntryType[] IN_TYPES={LogEntryType.LOG_IN,LogEntryType.LOG_BIN,LogEntryType.LOG_DIN,LogEntryType.LOG_DBIN};

	 private static final int MAX_NODE_TYPE_NUM=15;

	 public static final LogEntryType LOG_ROOT=new LogEntryType((byte)16,(byte)1,"Root",new SingleItemLogEntry(com.sleepycat.je.dbi.DbTree.class));

	 public static final LogEntryType LOG_TXN_COMMIT=new LogEntryType((byte)17,(byte)0,"Commit",new SingleItemLogEntry(com.sleepycat.je.txn.TxnCommit.class));

	 public static final LogEntryType LOG_TXN_ABORT=new LogEntryType((byte)18,(byte)0,"Abort",new SingleItemLogEntry(com.sleepycat.je.txn.TxnAbort.class));

	 public static final LogEntryType LOG_CKPT_START=new LogEntryType((byte)19,(byte)0,"CkptStart",new SingleItemLogEntry(com.sleepycat.je.recovery.CheckpointStart.class));

	 public static final LogEntryType LOG_CKPT_END=new LogEntryType((byte)20,(byte)0,"CkptEnd",new SingleItemLogEntry(com.sleepycat.je.recovery.CheckpointEnd.class));

	 public static final LogEntryType LOG_IN_DELETE_INFO=new LogEntryType((byte)21,(byte)0,"INDelete",new SingleItemLogEntry(com.sleepycat.je.tree.INDeleteInfo.class));

	 public static final LogEntryType LOG_BIN_DELTA=new LogEntryType((byte)22,(byte)0,"BINDelta",new BINDeltaLogEntry(com.sleepycat.je.tree.BINDelta.class));

	 public static final LogEntryType LOG_DUP_BIN_DELTA=new LogEntryType((byte)23,(byte)0,"DupBINDelta",new BINDeltaLogEntry(com.sleepycat.je.tree.BINDelta.class));

	 public static final LogEntryType LOG_TRACE=new LogEntryType((byte)24,(byte)0,"Trace",new SingleItemLogEntry(com.sleepycat.je.utilint.Tracer.class));

	 public static final LogEntryType LOG_FILE_HEADER=new LogEntryType((byte)25,(byte)0,"FileHeader",new SingleItemLogEntry(com.sleepycat.je.log.FileHeader.class));

	 public static final LogEntryType LOG_IN_DUPDELETE_INFO=new LogEntryType((byte)26,(byte)0,"INDupDelete",new SingleItemLogEntry(com.sleepycat.je.tree.INDupDeleteInfo.class));

	 public static final LogEntryType LOG_TXN_PREPARE=new LogEntryType((byte)27,(byte)0,"Prepare",new SingleItemLogEntry(com.sleepycat.je.txn.TxnPrepare.class));

	 private static final byte PROVISIONAL_MASK=(byte)0x80;

	 private static final byte IGNORE_PROVISIONAL=~PROVISIONAL_MASK;

	 private byte typeNum;

	 private byte version;

	 private String displayName;

	 private LogEntry logEntry;

	 LogEntryType( byte typeNum, byte version){ this.typeNum=typeNum; this.version=version; }

	 private LogEntryType( byte typeNum, byte version, String displayName, LogEntry logEntry){ this.typeNum=typeNum; this.version=version; this.logEntry=logEntry; this.displayName=displayName; LOG_TYPES[typeNum - 1]=this; }

	 public static boolean isNodeType__wrappee__base( byte typeNum, byte version){ return (typeNum <= MAX_NODE_TYPE_NUM); }

	 public static boolean isNodeType( byte typeNum, byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	isNodeType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public boolean isNodeType__wrappee__base(){ return (typeNum <= MAX_NODE_TYPE_NUM); }

	 public boolean isNodeType(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	isNodeType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public static LogEntryType findType__wrappee__base( byte typeNum, byte version){ if (typeNum <= 0 || typeNum > MAX_TYPE_NUM) { return null; } return (LogEntryType)LOG_TYPES[typeNum - 1]; }

	 public static LogEntryType findType( byte typeNum, byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	findType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public static Set getAllTypes__wrappee__base(){ HashSet ret=new HashSet(); for (int i=0; i < MAX_TYPE_NUM; i++) { ret.add(LOG_TYPES[i]); } return ret; }

	 public static Set getAllTypes(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	getAllTypes__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public LogEntry getSharedLogEntry__wrappee__base(){ return logEntry; }

	 public LogEntry getSharedLogEntry(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	getSharedLogEntry__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 LogEntry getNewLogEntry__wrappee__base() throws DatabaseException { try { return (LogEntry)logEntry.clone(); } catch ( CloneNotSupportedException e) { throw new DatabaseException(e); } }

	 LogEntry getNewLogEntry() throws DatabaseException { t.in(Thread.currentThread().getStackTrace()[1].toString());	getNewLogEntry__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 static byte setProvisional__wrappee__base( byte version){ return (byte)(version | PROVISIONAL_MASK); }

	 static byte setProvisional( byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	setProvisional__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public static byte clearProvisional__wrappee__base( byte version){ return (byte)(version & IGNORE_PROVISIONAL); }

	 public static byte clearProvisional( byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	clearProvisional__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 static boolean isProvisional__wrappee__base( byte version){ return ((version & PROVISIONAL_MASK) != 0); }

	 static boolean isProvisional( byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	isProvisional__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 byte getTypeNum__wrappee__base(){ return typeNum; }

	 byte getTypeNum(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	getTypeNum__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 byte getVersion__wrappee__base(){ return version; }

	 byte getVersion(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	getVersion__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 static boolean isValidType__wrappee__base( byte typeNum){ return typeNum > 0 && typeNum <= MAX_TYPE_NUM; }

	 static boolean isValidType( byte typeNum){ t.in(Thread.currentThread().getStackTrace()[1].toString());	isValidType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public String toString__wrappee__base(){ return displayName + "/" + version; }

	 public String toString(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	toString__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 boolean equalsType__wrappee__base( byte typeNum, byte version){ return (this.typeNum == typeNum); }

	 boolean equalsType( byte typeNum, byte version){ t.in(Thread.currentThread().getStackTrace()[1].toString());	equalsType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public boolean equalsType__wrappee__base( byte typeNum){ return (this.typeNum == typeNum); }

	 public boolean equalsType( byte typeNum){ t.in(Thread.currentThread().getStackTrace()[1].toString());	equalsType__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public boolean equals__wrappee__base( Object obj){ if (this == obj) { return true; } if (!(obj instanceof LogEntryType)) { return false; } return typeNum == ((LogEntryType)obj).typeNum; }

	 public boolean equals( Object obj){ t.in(Thread.currentThread().getStackTrace()[1].toString());	equals__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	 public int hashCode__wrappee__base(){ return typeNum; }

	 public int hashCode(){ t.in(Thread.currentThread().getStackTrace()[1].toString());	hashCode__wrappee__base(); t.out(Thread.currentThread().getStackTrace()[1].toString()); }

	private Tracer t = new Tracer();

	public Tracer getTracer(){return t;}


}
