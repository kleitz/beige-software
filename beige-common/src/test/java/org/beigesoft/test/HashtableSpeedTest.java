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
 * <p>Test Map multithreading.</p>
 *
 * @author Yury Demidenko
 */
public class HashtableSpeedTest {

  private final Hashtable<Integer, Integer> testMap =
    new Hashtable<Integer, Integer>(); 

  //Shared flags:
  private Boolean thread1WasException = false;

  private Boolean thread2WasException = false;

  private Boolean thread3WasException = false;
  
  private Boolean isThread1End = false;

  private Boolean isThread2End = false;

  private Boolean isThread3End = false;

  //@Test
  public void tstThreads() throws Exception {
    doThread1();
    //doThread2();
    doThread3();
    while (!(this.isThread1End
      //&& this.isThread2End
      && this.isThread3End)) {
      Thread.sleep(3);
    }
    assertTrue(!thread1WasException);
    //assertTrue(thread2WasException);
    assertTrue(!thread3WasException);
  }

  /**
   * <p>Thread1 getting cyclical "i" from Map,
   * if null then locking Map, then "put" it.</p>
   **/
  public void doThread1() throws Exception {
    Thread thread1 = new Thread() {
      
      public void run() {
        try {
          for (Integer i = 1; i < 1000; i++) {
            Integer res = HashtableSpeedTest.this.testMap.get(i);
            if (res != null) {
              //System.out.println("Thread#" + Thread.currentThread().getId()
                //+ ": got from map - " + res);
            } else {
              synchronized (HashtableSpeedTest.this.testMap) {
                HashtableSpeedTest.this.testMap.put(i, i);
                //System.out.println("Thread#" + Thread.currentThread().getId()
                  //+ ": put to map - " + i);
              }
            }
            Thread.sleep(3);
          }
        } catch (Exception ex) {
          HashtableSpeedTest.this.thread1WasException = true;
          ex.printStackTrace();
        }
        HashtableSpeedTest.this.isThread1End = true;
      }
    };
    thread1.start();
  }

  /**
   * <p>Thread3 getting cyclical "i" from Map,
   * if null then locking Map, then "put" it.</p>
   **/
  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          for (Integer i = 1; i < 1000; i++) {
            Integer res = HashtableSpeedTest.this.testMap.get(i);
            if (res != null) {
              //System.out.println("Thread#" + Thread.currentThread().getId()
                //+ ": got from map - " + res);
            } else {
              synchronized (HashtableSpeedTest.this.testMap) {
                HashtableSpeedTest.this.testMap.put(i, i);
                //System.out.println("Thread#" + Thread.currentThread().getId()
                  //+ ": put to map - " + i);
              }
            }
            Thread.sleep(3);
          }
        } catch (Exception ex) {
          HashtableSpeedTest.this.thread3WasException = true;
          ex.printStackTrace();
        }
        HashtableSpeedTest.this.isThread3End = true;
      }
    };
    thread3.start();
  }

  /**
   * <p>Thread2 that just read Map by iterator without locking.</p>
   **/
  public void doThread2() throws Exception {
    Thread thread2 = new Thread() {
      
      public void run() {
        try {
          for (int i = 1; i < 1000; i++) {
            for (Map.Entry<Integer, Integer> entry
              : HashtableSpeedTest.this.testMap.entrySet()) {
              System.out.println("Thread#" + Thread.currentThread().getId()
                + ": getting from map by iterator - " + entry.getKey());
            }
            Thread.sleep(3);
          }
        } catch (Exception ex) {
          HashtableSpeedTest.this.thread2WasException = true;
          ex.printStackTrace();
        }
        HashtableSpeedTest.this.isThread2End = true;
      }
    };
    thread2.start();
  }
}
