package edu.colostate.cs.count;

import org.apache.s4.base.Event;
import org.apache.s4.base.KeyFinder;
import org.apache.s4.core.App;
import org.apache.s4.core.RemoteStream;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 5/20/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class RelayApp extends App {

    @Override
    protected void onStart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onInit() {
        // create a prototype
        Relay relay = createPE(Relay.class);
        // Create a stream that listens to the "names" stream and passes events to the counter instance.
        createInputStream("prodStream", new KeyFinder<Event>() {
            @Override
            public List<String> get(Event event) {
                return Arrays.asList(new String[]{event.getStreamId()});
            }
        }, relay);

        RemoteStream outputStream = createOutputStream("relayStream");
        relay.setOutputStream(outputStream);

    }

    @Override
    protected void onClose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
