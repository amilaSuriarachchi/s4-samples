package edu.colostate.cs.ecg.process;

import org.apache.s4.base.Event;
import org.apache.s4.base.KeyFinder;
import org.apache.s4.core.App;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 7/31/14
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventApp extends App {

    @Override
    protected void onStart() {

    }

    @Override
    protected void onInit() {

        // create a prototype
        EventPE eventPE = createPE(EventPE.class);

        // Create a stream that listens to the "names" stream and passes events to the counter instance.
        createInputStream(Constants.STREAM_NAME, new KeyFinder<Event>() {
            @Override
            public List<String> get(Event event) {
                return Arrays.asList(new String[]{event.get(Constants.STREAM_ID)});
            }
        }, eventPE);
    }

    @Override
    protected void onClose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
