# Exercise for ICE

## To build and run

    git clone https://github.com/fdilke/tx-exercise.git
    cd tx-exercise
    sbt run

## To demonstrate the app

from the command line:

    curl  -X POST http://localhost:8080/create/0/7

or equivalently run ./create-machine.bat (on Windows).
Then from the browser you can view the events:

    http://localhost:8080/messages/0

# Notes

Given the 48 hour time constraint and other commitments, I was only able to work out a partial solution here -
but the core of the required algorithm is in place, generating a stream of server sent events
which is visible in the browser. Hopefully this is indicative of my approach and coding style.

I've implemented all five endpoints, but only the first three are functional.
Packaging up the app as a Docker image I'm sure would be a simple exercise in principle, but my
Windows machine currently seems to be having trouble with Docker, so I couldn't complete this.

I was familiar with cats-effect but not so much fs2 or http4s, so
there was a fair amount to figure out here from the documentation.
I'm sure some of the patterns could be improved.

I've added only rudimentary error checking - the response will be an
Internal Server Error if you try to destroy a nonexistent machine, or to create one with a start value which
doesn't consist entirely of digits.

Design decision: have only one timer for all of the machines.
Activated every second, it will trigger each one to iterate through the Collatz calculation.

When we invoke the 'messages' endpoint, it'll create a stream which
  tracks all ongoing events for that machine. Presumably if we browse elsewhere, the
  ref to the stream is lost and it eventually goes out of scope.

Came to this conclusion after reading up on fs2:
The correct architecture is to define an infinite stream which generates all the Collatz numbers, and then populate the SSE event stream by means of stream operations on this.

