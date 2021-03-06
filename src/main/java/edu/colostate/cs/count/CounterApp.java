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
import org.apache.s4.base.KeyFinder;
import org.apache.s4.core.App;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

public class CounterApp extends App {

    @Override
    protected void onStart() {
    }

    @Override
    protected void onInit() {

        // create a prototype
        Counter counter = createPE(Counter.class);
        // Create a stream that listens to the "names" stream and passes events to the counter instance.
        createInputStream("relayStream", new KeyFinder<Event>() {
            @Override
            public List<String> get(Event event) {
                return Arrays.asList(new String[]{event.getStreamId()});
            }
        }, counter);

    }

    @Override
    protected void onClose() {
    }

}
