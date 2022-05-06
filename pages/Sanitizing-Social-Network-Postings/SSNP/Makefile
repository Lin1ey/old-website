MAKEFLAGS += --silent
INCLUDES = -I /usr/lib/jvm/java-11-openjdk/include -I /usr/lib/jvm/java-11-openjdk/include/linux -I /var/lib/snapd/snap/swi-prolog/current/usr/lib/swipl/include
LI = -L=/var/lib/snapd/snap/core/current/lib/x86_64-linux-gnu -L/snap/swi-prolog/current/usr/lib
DL = -Wl,-rpath-link,/var/lib/snapd/snap/core/current/lib/x86_64-linux-gnu
PRELOAD = /snap/swi-prolog/current/usr/lib/libswipl.so:/var/lib/snapd/snap/core/current/lib/x86_64-linux-gnu/libtinfo.so.5
A = "$(A)"
CSVDir = "$(CSVDir)"
candidate = "$(candidate)" 
interviewer = "$(interviewer)"

all: run

createHeader: 
	scalac ScalaLayer.scala
	javac ScalaH.java
	java ScalaH CallCpp.class

compileCppScala: CppLayer.cpp 
	g++ $(INCLUDES) -shared -fPIC -o libCallCFromScala.so CppLayer.cpp

compileCppProlog: CppLayer.cpp
	g++ -Wno-unused-result $(INCLUDES) $(LI) $(DL) -o callProlog CppLayer.cpp -lrt -lpthread -lswipl

compileCppLayer: CppLayer.cpp
	g++ -Wno-unused-result $(INCLUDES) $(LI) $(DL) -shared -fPIC -o libCallCFromScala.so CppLayer.cpp -lrt -lpthread -lswipl

runCppProlog:
	LD_PRELOAD=$(PRELOAD) ./callProlog

runScalaLayer: 
	LD_PRELOAD=$(PRELOAD) LD_LIBRARY_PATH=./ scala ScalaLayer "$(CSVDir)" "$(candidate)" "$(interviewer)"

clean:
	rm -f *.class *.h CppLayer libCallCFromScala.so callProlog CallCpp.java javapOutput.dsk *_*.csv

runScala:
	LD_PRELOAD=$(PRELOAD) LD_LIBRARY_PATH=./ scala ScalaLayer $(CSVDir) $(candidate) $(interviewer)

run: createHeader compileCppLayer runScala
		