import java.util.concurrent.locks.*;

class Main {
  static Lock lock;
  static double[] sharedData;
  static int SD = 100,  TH = 10;
  // SD: shared data srray size
  // CS: critical section executed per thread
  // TH: number of threads

  // Critical section updated shared data
  // with a random value. If all values
  // dont match then it was not executed
  // atomically!
  static void criticalSection() {
    try {
    double v = Math.random();
    for(int i=0; i<SD; i++) {
      if(i % (SD/4)==0) Thread.sleep(1);
      sharedData[i] = v + v*sharedData[i];
    }
    }
    catch(InterruptedException e) {}
  }

  // Checks to see if all values match. If not,
  // then critical section was not executed
  // atomically.
  static boolean criticalSectionWasAtomic() {
    double v = sharedData[0];
    for(int i=0; i<SD; i++)
      if(sharedData[i]!=v) return false;
    return true;
  }

  // Unsafe thread executes CS N times, without
  // holding a lock. This can cause CS to be
  // executed non-atomically which can be detected.
  //
  // Safe thread executes CS N times, while
  // holding a lock. This allows CS to always be
  // executed atomically which can be verified.
  static Thread thread(int n, boolean safe, int CS) {
    Thread t = new Thread(() -> {
      
      for(int i=0; i<CS; i++) {
        if(safe) lock.lock();
        criticalSection();
        if(safe) lock.unlock();
      }
      
      // log(n+": done");
    });
    t.start();
    return t;
  }

  // Tests to see if threads execute critical
  // section atomically.
  static void testThreads(boolean safe, int CS) {
    String type = safe? "safe" : "unsafe";
    log("Starting "+TH+" "+type+" threads ..." + lock.toString());
    Thread[] threads = new Thread[TH];
    for(int i=0; i<TH; i++)
      threads[i] = thread(i, safe, CS);
    try {
    for(int i=0; i<TH; i++)
      threads[i].join();
    }
    catch(InterruptedException e) {}
    boolean atomic = criticalSectionWasAtomic();
    log("Critical Section was atomic? "+atomic);
  }

  public static void main(String[] args) {
     lock = new MCSLock();
     sharedData = new double[SD];
     for (int j=100; j<=1000; j+=100) {
        log("CS is "+ j);
        double start = System.currentTimeMillis();
        // testThreads(false);
        testThreads(true, j);
        double timeElapsed1 = System.currentTimeMillis() - start;
        System.out.println("Time taken was "+ timeElapsed1 +"ms");
        log("");
     }
    
    lock = new ReentrantLock();

    for (int j=100; j<=1000; j+=100) {
      log("CS is "+ j);
      double start = System.currentTimeMillis();
      // testThreads(false);
      testThreads(true, j);
      double timeElapsed1 = System.currentTimeMillis() - start;
      System.out.println("Time taken was "+ timeElapsed1 +"ms");
      log("");
   }
    // double start2 = System.currentTimeMillis();
    // testThreads(true, 100);
    // double timeElapsed2 = System.currentTimeMillis() - start2;
    // System.out.println("Time taken was "+ timeElapsed2 +"ms");
    // Test [] test= new Test[TH];
    // t1.setName("t1");
    // t1.start();
    // 
  //   for(int i=0; i<TH; i++){
  //     test[i] = new Test();
  //     String name = "Thread " + i;
  //     Thread thread = new Thread(test[i]);
  //     thread.setName(name);
  //     thread.start();
  //     System.out.println(i);
  // }
}

  static void log(String x) {
    System.out.println(x);
  }
}
