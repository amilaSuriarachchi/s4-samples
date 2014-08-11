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
import org.apache.s4.core.ProcessingElement;

import java.util.concurrent.atomic.AtomicLong;

public class Counter extends ProcessingElement {

    private AtomicLong atomicLong = new AtomicLong();
    private long lastTime = System.currentTimeMillis();

    public void onEvent(Event event) {

        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 1000000) == 0){
            System.out.println("Message Rate ==> " + 1000000000/ (System.currentTimeMillis() - this.lastTime) + " for stream " + event.getStreamId());
            this.lastTime =  System.currentTimeMillis();
        }
    }

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onRemove() {
    }

}
