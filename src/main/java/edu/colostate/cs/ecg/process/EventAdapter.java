package edu.colostate.cs.ecg.process;

import edu.colostate.cs.ecg.analyse.Record;
import edu.colostate.cs.ecg.analyse.RecordReader;
import org.apache.s4.core.RemoteStream;
import org.apache.s4.core.adapter.AdapterApp;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 7/31/14
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventAdapter extends AdapterApp {

    @Inject
    @Named("record")
    String record;

    @Inject
    @Named("workingDir")
    String workingDir;

    @Inject
    @Named("streams")
    int numOfStreams;

    @Inject
    @Named("startPoint")
    int startPoint;

    private CountDownLatch latch;

    private EventSender[] eventSenders;

    private long startTime;

    private int MESSAGE_BUFFER_SIZE = 500;

    private void publishEvent(Record[] records) {
        for (EventSender eventSender : this.eventSenders) {
            eventSender.addRecords(records);
        }
    }

    public void sendMessages() {

        try {

            List<String> commands = new ArrayList<String>();
            commands.add("rdsamp");
            commands.add("-r");
            commands.add(this.record);
            commands.add("-p");
//            commands.add("-f");
//            commands.add("1000");
//            commands.add("-t");
//            commands.add("1100");
            commands.add("-c");
            commands.add("-s");
            commands.add("II");

            RecordReader recordReader = null;
            long totalMessages = 0;
            Record[] messageBuffer = new Record[MESSAGE_BUFFER_SIZE];
            int bufferPointer = 0;

            this.startTime = System.currentTimeMillis();

            try {
                recordReader = new RecordReader(commands, this.workingDir);
                while (recordReader.hasNext()) {
                    messageBuffer[bufferPointer] = recordReader.next();
                    bufferPointer++;
                    if (bufferPointer == MESSAGE_BUFFER_SIZE) {
                        // this means buffer is full.
                        this.publishEvent(messageBuffer);
                        bufferPointer = 0;
                    }
                    totalMessages++;
                    if (totalMessages % 500000 == 0) {
                        System.out.println("Number of messages processed " + totalMessages + " time " + System.currentTimeMillis());
                    }
                }
                recordReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateStat() {

        //first finish the threads
        for (EventSender eventSender : this.eventSenders) {
            eventSender.setFinish();
        }

        try {
            this.latch.await();
        } catch (InterruptedException e) {
        }

        long totalTime = System.currentTimeMillis() - this.startTime;

        long totalMessages = 0;
        for (EventSender eventSender : this.eventSenders) {
            totalMessages += eventSender.getNumberOfRecords();
        }
        System.out.println("Total messages " + totalMessages);
        System.out.println("Total time " + totalTime);
        System.out.println("Through put " + (totalMessages * 1000.0) / totalTime);
    }

    @Override
    protected void onStart() {
        //initialise the event senders
        this.latch = new CountDownLatch(this.numOfStreams);

        this.eventSenders = new EventSender[this.numOfStreams];

        for (int i = 0; i < this.numOfStreams; i++) {
            RemoteStream remoteStream = createOutputStream(Constants.STREAM_NAME + (i + this.startPoint));
            this.eventSenders[i] = new EventSender(remoteStream, this.latch);
            Thread thread = new Thread(this.eventSenders[i]);
            thread.start();
        }

        // just wait until all dummy messages are send
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }

        // initialise the event senders
        sendMessages();
        calculateStat();
    }
}