#!/bin/bash

#create clusters
./s4 newCluster -c=count -nbTasks=1 -flp=12000
./s4 newCluster -c=relay -nbTasks=1 -flp=13000
./s4 newCluster -c=adapter -nbTasks=1 -flp=14000

#build the app
./s4 s4r -appClass=edu.colostate.cs.count.CounterApp -b=`pwd`/build.gradle s4count
./s4 s4r -appClass=edu.colostate.cs.count.RelayApp -b=`pwd`/build.gradle s4relay
./s4 s4r -appClass=edu.colostate.cs.count.Producer -b=`pwd`/build.gradle s4adapter

#deploy the app
./s4 deploy -s4r=`pwd`/build/libs/s4count.s4r -c=count -appName=countApp  
./s4 deploy -s4r=`pwd`/build/libs/s4relay.s4r -c=relay -appName=relayApp  

#deploy adapter
./s4 deploy -s4r=`pwd`/build/libs/s4adapter.s4r -p=s4.adapter.output.stream=names,threads=10 -c=adapter -appName=adapterApp

#deploy status
./s4 status


