package com.arsenarsen.userbot;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Not in use until I figure out how to make it work
 */
@SuppressWarnings("deprecation")
@Deprecated
public class Security extends SecurityManager {

    /**
     * Threads that get limited privileges
     */
    public static Set<Thread> threads = new CopyOnWriteArraySet<>();

    @Override
    public void checkExec(String cmd) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }
    }

    @Override
    public void checkExit(int status) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }
    }

    @Override
    public void checkCreateClassLoader() {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }
    }

    @Override
    public void checkAccept(String host, int port) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkAccess(ThreadGroup g) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkAccess(Thread t) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkConnect(String host, int port) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkDelete(String file) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkLink(String lib) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkListen(int port) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkPackageAccess(String pkg) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkMulticast(InetAddress maddr, byte ttl) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkPackageDefinition(String pkg) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

//    @Override
//    public void checkPermission(Permission perm) {
//        if(threads.contains(Thread.currentThread())){
//            throw new SecurityException();
//        }
//
//    }

//    @Override
//    public void checkPermission(Permission perm, Object context) {
//        if(threads.contains(Thread.currentThread())){
//            throw new SecurityException();
//        }
//
//    }

    @Override
    public void checkPrintJobAccess() {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkRead(FileDescriptor fd) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkRead(String file) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkRead(String file, Object context) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkSecurityAccess(String target) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkSetFactory() {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkWrite(String file) {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }

    @Override
    public void checkSystemClipboardAccess() {
        if(threads.contains(Thread.currentThread())){
            throw new SecurityException();
        }

    }
}
