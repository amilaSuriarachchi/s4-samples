package edu.colostate.cs.count;

import org.apache.s4.base.Event;
import org.apache.s4.core.ProcessingElement;
import org.apache.s4.core.RemoteStream;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 5/20/14
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Relay extends ProcessingElement {

    private RemoteStream remoteStream;

    public void onEvent(Event event) {

        this.remoteStream.put(event);

    }

    @Override
    protected void onCreate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onRemove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setOutputStream(RemoteStream remoteStream){
        this.remoteStream = remoteStream;
    }
}
