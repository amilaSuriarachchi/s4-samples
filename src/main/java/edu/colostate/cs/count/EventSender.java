package edu.colostate.cs.count;

import org.apache.s4.base.Event;
import org.apache.s4.core.RemoteStream;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/2/14
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender implements Runnable {

    private RemoteStream remoteStream;
    private Event event;
    private int numberOfMsg;
    private CyclicBarrier startBarrier;
    private CountDownLatch endLatch;

    public EventSender(RemoteStream remoteStream, Event event, int numberOfMsg, CyclicBarrier startBarrier, CountDownLatch endLatch) {
        this.remoteStream = remoteStream;
        this.event = event;
        this.numberOfMsg = numberOfMsg;
        this.startBarrier = startBarrier;
        this.endLatch = endLatch;
    }

    @Override
    public void run() {

        try {
            this.startBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (int i = 0; i < this.numberOfMsg; i++) {
            this.remoteStream.put(this.event);
        }
        this.endLatch.countDown();
    }
}
