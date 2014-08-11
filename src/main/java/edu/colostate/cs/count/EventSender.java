package edu.colostate.cs.count;

import org.apache.s4.base.Event;
import org.apache.s4.core.RemoteStream;

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

    public EventSender(RemoteStream remoteStream, Event event) {
        this.remoteStream = remoteStream;
        this.event = event;
    }

    @Override
    public void run() {

        while (true){
            this.remoteStream.put(this.event);
        }
    }
}
