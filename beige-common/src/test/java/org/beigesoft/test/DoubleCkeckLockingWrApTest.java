package org.beigesoft.test;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.Hashtable;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * <pre>This is about http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html.
 * It's very easy to understand that Java instruction:
 *   this.sharedBean = new SharedBean(); //create and assign reference in one
 * can be compiled into processor's (native) instructions:
 *   1. allocate memory for new instance SharedBean and create it
 *   2. assign reference of new instance to this.sharedBean
 *   3. call to SharedBean constructor code
 * On step #3 OS may switch to another thread that will be used
 * this.sharedBean whose constructor's code doesn't completed.
 * 
 * But it's very hard to understand why author didn't separate instruction in his code for creation object
 * and assigning reference to it into two instructions, even though lazy initialization of well designed bean
 * (that is usually POJO) means initialization it with setters rather than with constructor.
 * JVM must insure that step # 4 will be deal with completely initialized object:
 *   1. Bean lBean = new Bean();
 *   2. lBean.setServiceA(new ServiceA()); // POJO initialization
 *   3. this.bean = lBean;
 *   4. this.bean.do(); // this.bean is completely initialized
 * 
 * Even though putting reference to map is non-atomic (multi-instructions),
 * at the end assigning reference (pointer) is atomic CPU instruction e.g. for 64bit pointer:
 *    mov qword ptr [rbp], rax
 * 
 * This is test for wrong approach - creation and referencing in one instruction:
 *    this.longInitializedBean = new LongInitializedBean();
 * 
 * But test shows there is no problem for Open JDK 7 AMD64:
 *  this.countUsingNonInitialized=0
 *  this.countNulled=13
 *  this.countInitialized=14
 * i.e. JVM generate native code which assign reference after call constructor
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DoubleCkeckLockingWrApTest {

  //Shared Long Initialized Bean:
  private LongInitializedBean longInitializedBean = null;

  //Shared flags:  
  private Boolean isThread1End = false;

  private Boolean isThread2End = false;

  private Boolean isThread3End = false;

  //Shared data:
  private int index = 0;

  private int countUsingNonInitialized = 0;

  private int countInitialized = 0;

  private int countNulled = 0;

  @Test
  public void tstThreads() throws Exception {
    doThread1();
    doThread3();
    doThread2();
    for (this.index = 0; this.index < 40; this.index++) {
      Thread.sleep(5);
    }
    while (!(this.isThread1End
      && this.isThread2End
      && this.isThread3End)) {
      Thread.sleep(5);
    }
    System.out.println("this.countUsingNonInitialized=" + this.countUsingNonInitialized);
    System.out.println("this.countNulled=" + this.countNulled);
    System.out.println("this.countInitialized=" + this.countInitialized);
    // This assertion will fail for JVM that generate native code which
    // assign reference after call constructor
    //assertTrue(this.countUsingNonInitialized > 0);
  }

  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingWrApTest.this.index < 40) {
            if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingWrApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingWrApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingWrApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean == null) {
                  // wrong approach - creation and referencing in one instruction:
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingWrApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingWrApTest.this.isThread3End = true;
      }
    };
    thread3.start();
  }

  public void doThread2() throws Exception {
    Thread thread2 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingWrApTest.this.index < 40) {
            if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingWrApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingWrApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingWrApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean == null) {
                  // wrong approach - creation and referencing in one instruction:
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingWrApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingWrApTest.this.isThread2End = true;
      }
    };
    thread2.start();
  }

  public void doThread1() throws Exception {
    Thread thread1 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingWrApTest.this.index < 40) {
            if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingWrApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingWrApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingWrApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingWrApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingWrApTest.this.longInitializedBean == null) {
                  // wrong approach - creation and referencing in one instruction:
                  DoubleCkeckLockingWrApTest.this.longInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingWrApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingWrApTest.this.isThread1End = true;
      }
    };
    thread1.start();
  }
}
