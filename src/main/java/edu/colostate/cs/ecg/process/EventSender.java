package edu.colostate.cs.ecg.process;

import edu.colostate.cs.ecg.analyse.Record;
import org.apache.s4.base.Event;
import org.apache.s4.core.RemoteStream;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 7/24/14
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender implements Runnable {

    public static final int MAX_SIZE = 1000;

    private Queue<Record> messages;
    private boolean isFinished;


    private long numberOfRecords = 0;

    private CountDownLatch latch;

    private RemoteStream remoteStream;

    // streams start point for this thread
    private int startPoint;
    // number of streams this thread has to produce
    private int streams;


    public EventSender(CountDownLatch latch, RemoteStream remoteStream, int startPoint, int streams) {

        this.messages = new LinkedList<Record>();
        this.isFinished = false;
        this.latch = latch;
        this.remoteStream = remoteStream;
        this.startPoint = startPoint;
        this.streams = streams;
    }

    public synchronized void addRecord(Record record) {
        if (this.messages.size() == MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addRecord(record);
        } else {
            this.messages.add(record);
            this.notify();
        }

    }

    public synchronized void addRecords(Record[] records) {
        if (this.messages.size() >= MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            addRecords(records);
        } else {
            for (Record record : records) {
                this.messages.add(record);
            }
            this.notify();
        }

    }

    public synchronized Record getRecord() {

        Record record = this.messages.poll();
        while ((record == null) && !this.isFinished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
            record = this.messages.poll();
        }
        this.notify();
        return record;

    }

    public synchronized void setFinish() {
        this.isFinished = true;
        this.notify();
    }

    public long getNumberOfRecords() {
        return this.numberOfRecords;
    }

    public void publishEvent(Record event) {

        Event s4Event = null;
        for (int i = 0; i < this.streams; i++){
            s4Event = new Event();
            s4Event.put(Constants.STREAM_ID, String.class, "ecg" + (this.startPoint + i));
            s4Event.put(Constants.TIME, Double.class, new Double(event.getTime()));
            s4Event.put(Constants.VALUE, Double.class, new Double(event.getValue()));
            remoteStream.put(s4Event);
            this.numberOfRecords++;
        }
    }


    @Override
    public void run() {

        Record record = null;
        // record will be thread executions is over.
        while ((record = getRecord()) != null) {
            this.publishEvent(record);
        }
        this.latch.countDown();
    }
}
