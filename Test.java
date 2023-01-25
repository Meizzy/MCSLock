class Test implements Runnable{

    // @Override
    public void run() { Lock();}
      
    public static synchronized void Lock() {
        System.out.println(
                Thread.currentThread().getName());
                synchronized(Test.class) 
                {
    
                    System.out.println(
                        "in block "
                        + Thread.currentThread().getName());
                    System.out.println(
                        "in block "
                        + Thread.currentThread().getName()
                        + " end");
                      
            }   
    }
}
