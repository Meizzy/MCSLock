# MCSLock
>Assignment for Architecture for Parallel Computer Systems course @[LETI](https://etu.ru/) university 2022/23 academic session
### Contributors
**Yaseer Buruji Ibrahim** - ibyaseer@mail.ru

[Buruji Yaseer](https://github.com/Meizzy)

# Overview
The MCSLock is represented as a linked list of QNode objects, where each QNode represents either a lock holder or a thread waiting to acquire the lock. Unlike the CLHLock class, the list is explicit, not virtual: Instead of embodying the list in thread-local variables, it is embodied in the (globally accessible) QNode objects, via their next fields.
To acquire the lock, a thread appends its own QNode at the tail of the list. If the queue was not previously empty, it sets the predecessor’s QNode’s next field to refer to its own QNode. The thread then spins on a (local) locked field in its own QNode waiting until its predecessor sets this field to false.

```java
lock():
1. When thread wants to access critical
   section, it stands at the end of the
   queue (FIFO).
2a. If there is no one in queue, it goes head
    with its critical section.
2b. Otherwise, it locks itself and asks the
    thread infront of it to unlock it when its
    done with CS.
```

```java
unlock():
1. When a thread is done with its critical
   section, it needs to unlock any thread
   standing behind it.
2a. If there is a thread standing behind,
    then it unlocks him.
2b. Otherwise it tries to mark queue as empty.
     If no one is joining, it leaves.
2c. If there is a thread trying to join the
    queue, it waits until he is done, and then
    unlocks him, and leaves.
```
