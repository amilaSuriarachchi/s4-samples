/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.colostate.cs.count;

import org.apache.s4.base.Event;
import org.apache.s4.core.RemoteStream;
import org.apache.s4.core.adapter.AdapterApp;

import javax.inject.Inject;
import javax.inject.Named;

public class Producer extends AdapterApp {

    @Inject
    @Named("threads")
    int numOfThreads;

    @Override
    protected void onStart() {

        Event pEvent = new Event();
        pEvent.put("time", Double.class, 1000.00);
        pEvent.put("key1", String.class, "value1");
        pEvent.put("key2", Double.class, 4567.89);
        pEvent.put("key3", Long.class, 100000l);
        pEvent.put("key4", String.class, "Last value to send");

        RemoteStream remoteStream =  createOutputStream("prodStream");

        for (int i = 0; i < numOfThreads; i++) {
            EventSender eventSender = new EventSender(remoteStream, pEvent);
            Thread thread = new Thread(eventSender);
            thread.start();
        }

    }
}
