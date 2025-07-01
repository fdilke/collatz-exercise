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

