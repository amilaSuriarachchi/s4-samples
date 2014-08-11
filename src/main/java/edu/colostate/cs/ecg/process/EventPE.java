package edu.colostate.cs.ecg.process;

import edu.colostate.cs.ecg.analyse.Record;
import edu.colostate.cs.ecg.analyse.Tompikens;
import org.apache.s4.base.Event;
import org.apache.s4.core.ProcessingElement;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 7/31/14
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventPE extends ProcessingElement {

    private Tompikens tompikens = new Tompikens();
    private AtomicLong atomicLong = new AtomicLong();
    private long lastTime = System.currentTimeMillis();

    /**
     * This method is called upon a new Event on an incoming stream
     */
    public void onEvent(Event event) {

        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 1000000) == 0){
            System.out.println("Message Rate ==> " + 1000000000/ (System.currentTimeMillis() - this.lastTime) + " - Current value " + currentValue );
            this.lastTime =  System.currentTimeMillis();
        }

        Record record =
                new Record(event.get(Constants.TIME, Double.class).doubleValue(),
                        event.get(Constants.VALUE, Double.class).doubleValue());
        tompikens.bandPass(record);

    }

    @Override
    protected void onCreate() {

    }

    @Override
    protected void onRemove() {

    }








}
