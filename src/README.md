### How to compile code: 
1. Navigate into /src `$ cd src`
2. Run `$ make` 

### How to execute code:
1. Run `$ make server` from /src.
2. Run `$ make client id=<identifier>` where identifier is any unqiue string 
from one or many different terminals.

### How to interact from client:
1. Run any prefix math operation in format of <operator><operand1> <operand2>
2. To exit, enter "stop"
	
##### Constaints: 
- Operands must be a non-negative integer
- Operators are limited to +, -, /, *, %
- Only two operands are allowed at a time

### Common Issues
- java/javac is not recognized: execute \
	`$ export PATH=$PATH:/drives/c/"Program Files"/Java/jdk-20/bin:.` and substitute appropriate local directory names 
- Bind error: port is in use \
	Choose a valid  port number and replace @ TCPClient.java (line 32) and TCPServer.java (line 14) then recompile
