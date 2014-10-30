CS310-elevator
==============
/**********************************************
 * Please DO NOT MODIFY the format of this file
 **********************************************/


/*************************
 * Team Info & Time spent
 *************************/


        Name1: Tze Kang Ng
        NetId1: tn52
        Time spent: 30 hours


        Name2: Pritam Mathivanan
        NetId2: pm98
        Time spent: 30 hours


        Name3: Arun Karottu
        NetId3: ajk44
        Time spent: 30 hours
        
        Name4: Benson Tran
        NetId4: bwt8
        Time spent: 30 hours


/******************
 * Files to submit
 ******************/


        lab3.jar // An executable jar including all the source files and test cases.
        README        // This file filled with the lab implementation details
        Elevator.input // You can submit a sample input and log file
        Elevator.log   // corresponding to the input but Elevator.log should be 
                       // auto-generated on execution of jar file


/************************
 * Implementation details
 *************************/


/* 
 * This section should contain the implementation details and a overview of the
 * results. 


 * You are required to provide a good README document including the
 * implementation details. In particular, you can use pseudocode to describe
 * your implementation details where necessary. However that does not mean to
 * copy/paste your C code. Specifically, explain the synchronization primities
 * used in implmenting the elevator, scheduling choices used, how the capacity
 * constraint is handled, and how the mulitple elevators are supported. Also,
 * explain how to run the test cases for your EventBarrier and Elevator and how
 * to interpret the results. Check the README.OUTPUTFORMAT for the acceptable
 * input/output format for the elevator. Expect the design and implementation
 * details to be at most 2-3 pages.  A plain textfile is encouraged. However, a
 * pdf is acceptable.  No other forms are permitted.


 * In case of lab is limited in some functionality, you should provide the
 * details to maximize your partial credit.  
 * */


Event Barrier Implementation:


The EventBarrier implementation follows the guidelines listed in the lab report. EventBarrier extends AbstractEventBarrier to implement arrive(), raise(), complete(), and waiters(). All these methods are synchronized as EventBarrier itself can be seen as a lock, which is passed around by the Consumer and Producer threads. 


The class TestEvenBarrier runs our implementation of EventBarrier by first running the Consumer threads and then running the single Producer thread, all of which are passed the EventBarrier lock. The first consumer thread calls EventBarrier’s arrive(), which increments a global counter (numWaiters). This keeps track of the number of current waiters. Within arrive(), the thread then notifies any other blocking thread and blocks, releasing the lock. notifyAll() can notify the producer thread or any consumer thread. If it’s the latter then the same, cyclic process occurs: numWaiters increments and the corresponding consumer blocks and notifies. Eventually, notifyAll() will wake up the Producer thread, which now has access to EventBarrier, and calls raise(). The barrier will not ‘lower’ until all threads have crossed.


raise() notifies all sleeping consumer threads. The sleeping consumer threads wake up in the raise() method call. Only one thread at a time will actually complete(); this is ensured via a ‘loop-before-you-leap implementation’. After all consumer threads have completed(), a final notifyAll() is used to wake up the producer thread to ‘lower the gate’.




Elevator Implementation:


The overarching implementation of Elevator follows the familiar theme of Producer threads, Consumer threads, and locks. Moreover, this implementation loosely follows the universal model-view-controller design. The following paragraphs will elaborate on such implementation.


Intuitively, Elevators serve as our producer threads while Riders serve as our Consumer threads. In addition, ElevatorBarrier is the parallel of EventBarrier. The main difference is that the former contains a reference to a floor number and a reference to whether a consumer is going up, down, or exiting. Three ElevatorBarriers are instantiated per floor of the building. These ElevatorBarriers facilitate the movement of riders to and from the elevators. This will be described in more detail later, but essentially the rider threads are blocked while waiting for the elevator and waiting on the elevator. This allows the elevator itself access to the ElevatorBarrier to eventually signal the rider to get on or off the elevator.


Naturally, design patterns provide abstractions for which members of a team can focus specifically on a module without having to worry about another module’s implementation. With that said, we decided that an MVC architectural pattern would work well for this project. 


The Building Class serves as the model: It stores the number of elevators and floors as well as lists of up/down/out ElevatorBarriers that the elevator threads will reference. Moreover the Building Class contains CallUp() and CallDown() which interact with the Elevator Controller to return the correct elevator.


The ElevatorController Class serves as our controller and implements an algorithm similar to SCAN. It dedicates elevators to a particular direction, and these elevator’s can’t pick up riders going into another direction until the elevator is idle (i.e. empty). ElevatorController looks for available elevators and finds the best elevator given the parameters of the request (floor, direction) via returnBestElevator(). The ElevatorController has a queue of elevators that is populated when the parser runs. When a person requests an elevator, the ElevatorController checks the head of the queue and returns if the elevator has no people on it, otherwise, it simply returns the next elevator in the queue.

The resource scheduling of the elevators is done with logic in the elevator class itself. Each elevator has a boolean array that is the size of the number of floors in the building. The boolean array contains true at indexes (aka floors) where riders want to exit. All requests are placed in the boolean array of each elevator. By checking the current position of the elevator and the nearest destination, a specific elevator can serve a specific request. In this manner, one elevator can efficiently pick up riders on consecutive floors and drop them off as needed. The boolean array makes the performance of multiple elevators relatively smooth.

The Rider and Elevator classes can be interpreted as the view not only because Elevator prints information to the screen, but also because the model (the Building Class) updates both the Rider and Elevator instances. The Rider threads are run before the Elevator threads. The rider threads ‘arrive()’ at their floors by fetching their respective ElevatorBarriers (depending on whether they are going up or down) through the Building Class. 


Moreover, the riders are able to fetch (via callUp()/callDown()) their elevators through Building (Building interacts with ElevatorController). CallUp() and CallDown() update boolean arrays for the fetched elevator. Each index of this array refers to the floor of the building. The value ‘true’ essentially moves the elevator to that floor to either pickup or drop off riders. 


The pickup/drop off mechanism is facilitated via the ElevatorBarriers. The elevator gets hold of that floor’s ElevatorBarrier and raises(). Exactly like EventBarrier, this wakes up all riders on the floor. The elevator’s boolean and ElevatorBarrier lists are updated, the rider threads then fetch the destination floor’s corresponding ElevatorBarrier and block again. The process repeats then repeats except the riders will now exit the elevator.

Note*: Our building floors run from 0 to n-1 where n is the input number of floors given in the input file, e.g. n = 8 will give floors 0 to 7.

Note*: If our max capcity value is 3 or higher, there doesn't seem to be a case where a rider is unable to enter the elevator. We did our capacity testing at a value of 2.

Running the Event Barrier Test Case:

To test, run the following in your choice of shell/terminal:

    java -jar lab3.jar p1

The output will print out the number of threads waiting, the arrival of threads, the raising and lowering of the barrier and thread completion. The output can be found in EventBarrier.log.

Running the Elevator Test Case: 


The class to run is the TestElevator.java class. It uses the Parser.java class to read an input text file with parameters and run the elevator service for the riders. The results will be printed to the console and a Elevator.log file. The contents of the results can be generally summarized with the following:


* Riders pushing the up/down button from the current floor
* The appropriate elevator arrives via the scheduling algorithm, and opens doors
* Riders enter the elevators
* The doors of an elevator close
* Rider requests a floor
* Elevator arrives at requested floor
* Elevator doors open
* Rider exits
* Elevator doors close
* Repeat until all riders are served

To test, run the following in your choice of shell/terminal:

    java -jar lab3.jar [input text file]

[input text file] will have a value of one of the following:

    elevator_part1.txt
    elevator_part2.txt
    elevator_part3.txt

Elevator.log will be printed to the directory containing the lab3.jar file.


/************************
 * Feedback on the lab
 ************************/


/*
 * Any comments/questions/suggestions/experiences that you would help us to
 * improve the lab.
 * */


We have no opinion, we are simply glad to have completed the lab.


/************************
 * References
 ************************/


/*
 * List of collaborators involved including any online references/citations.
 * */


There were no collaborators and no extra references used other than the class lectures.