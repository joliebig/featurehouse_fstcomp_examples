package com.sleepycat.je.dbi; 
import java.util.HashSet; 
import java.util.Iterator; 
import java.util.Set; 
import java.util.SortedSet; 
import java.util.TreeSet; 
import com.sleepycat.je.DatabaseException; 
import com.sleepycat.je.tree.IN; 
import de.ovgu.cide.jakutil.*; 
public  class  INList {
	 private static final String DEBUG_NAME=INList.class.getName();

	 private SortedSet ins=null;

	 private EnvironmentImpl envImpl;

	 INList( EnvironmentImpl envImpl){ this.envImpl=envImpl; ins=new TreeSet(); this.hook338(envImpl); }

	 public INList( INList orig, EnvironmentImpl envImpl) throws DatabaseException { ins=new TreeSet(orig.getINs()); this.hook340(); this.envImpl=envImpl; this.hook339(envImpl); }

	 public SortedSet getINs(){ return ins; }

	 public int getSize(){ return ins.size(); }

	 public void add( IN in) throws DatabaseException { new INList_add(this,in).execute(); }

	 private void addAndSetMemory( Set set, IN in){ new INList_addAndSetMemory(this,set,in).execute(); }

	 public void removeLatchAlreadyHeld( IN in) throws DatabaseException { boolean removeDone=ins.remove(in); removeDone=this.hook341(in,removeDone); assert removeDone; this.hook346(in); }

	 public void remove( IN in) throws DatabaseException { removeLatchAlreadyHeld(in); }

	 public SortedSet tailSet( IN in) throws DatabaseException { return ins.tailSet(in); }

	 public IN first() throws DatabaseException { return (IN)ins.first(); }

	 public Iterator iterator(){ return ins.iterator(); }

	 public void clear() throws DatabaseException { ins.clear(); this.hook342(); }

	 public void dump(){ System.out.println("size=" + getSize()); Iterator iter=ins.iterator(); while (iter.hasNext()) { IN theIN=(IN)iter.next(); System.out.println("db=" + theIN.getDatabase().getId() + " nid=: "+ theIN.getNodeId()+ "/"+ theIN.getLevel()); } }

	
@MethodObject static  class  INList_add {
		 INList_add( INList _this, IN in){ this._this=_this; this.in=in; }

		 void execute() throws DatabaseException { this.hook344(); addToMajor=true; this.hook343(); }

		 protected INList _this;

		 protected IN in;

		 protected boolean enteredWithLatchHeld;

		 protected boolean addToMajor;

		 protected void hook343() throws DatabaseException { this.hook345(); }

		 protected void hook344() throws DatabaseException { }

		 protected void hook345() throws DatabaseException { _this.addAndSetMemory(_this.ins,in); }


	}

	
@MethodObject static  class  INList_addAndSetMemory {
		 INList_addAndSetMemory( INList _this, Set set, IN in){ this._this=_this; this.set=set; this.in=in; }

		 void execute(){ addOk=set.add(in); assert addOk : "failed adding in " + in.getNodeId(); }

		 protected INList _this;

		 protected Set set;

		 protected IN in;

		 protected boolean addOk;

		 protected MemoryBudget mb;


	}

	 protected void hook338( EnvironmentImpl envImpl){ }

	 protected void hook339( EnvironmentImpl envImpl) throws DatabaseException { }

	 protected void hook340() throws DatabaseException { }

	 protected boolean hook341( IN in, boolean removeDone) throws DatabaseException { return removeDone; }

	 protected void hook342() throws DatabaseException { }

	 protected void hook346( IN in) throws DatabaseException { }


}
