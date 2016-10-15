package Crawl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class URLQueue {
    private Queue<String> queue;
    private HashSet<String> queuedURLS;    
    private int maxQueued;
    
    public URLQueue() {
        this(Integer.MAX_VALUE);
    }
    
    public URLQueue(int maxQueued) {
        this.queue = new LinkedList<String>();
        this.queuedURLS = new HashSet<String>();
        this.maxQueued = maxQueued;
    }
    
    public void offer(String url) {
        if (!this.queuedURLS.contains(url) && this.queuedURLS.size() < this.maxQueued) {
            this.queuedURLS.add(url);
            this.queue.offer(url);
            System.out.println(queuedURLS.size() + ": " + url);
            
        }
    }
    
    public String poll() {
        return this.queue.poll();
    }
    
    public int size() {
        return this.queue.size();
    }
    
    public int queuedURLSSize() {
        return this.queuedURLS.size();
    }
    
    public HashSet<String> getQueuedURLS() {
        return (HashSet<String>) this.queuedURLS.clone();
    }
}