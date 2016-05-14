
# Install
BUILD_DIR = build
BIN = $(BUILD_DIR)/libnuklear-java
SRC_DIR = src
GENERATED_DIR_BASE = generated
GENERATED_DIR = $(GENERATED_DIR_BASE)/nuklear/swig
TEST_NAME = NuklearDemo
TEST_CLASS_BIN = $(BUILD_DIR)/$(TEST_NAME).class

ifndef JAVA_HOME
$(error JAVA_HOME is not set)
endif

# Flags
CFLAGS = -std=c99 -pedantic -fPIC -O0 -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I$(SRC_DIR)
LDFLAGS = -shared

SRC = $(SRC_DIR)/nuklear_headless.c $(GENERATED_DIR)/nuklear_wrap.c 
OBJS = $(SRC:.c=.o)

ifeq ($(OS),Windows_NT)
BIN := $(BIN).exe
LIBS = -lm
else
BIN := $(BIN).so
LIBS = -lm
endif

.PHONY: all clean test

all: $(TEST_CLASS_BIN)
#all: $(BIN)

%.o: %.c
	$(CC) $(CFLAGS) -o $@ -c $< 

$(GENERATED_DIR)/nuklear_wrap.c: nuklear.i
	mkdir -p $(GENERATED_DIR) $(BUILD_DIR)
	swig3.0 -java -package nuklear.swig -outdir $(GENERATED_DIR) -o $@ $<

$(BIN):$(OBJS)
	$(CC) $(LDFLAGS) -o $(BIN) $(OBJS) $(LIBS)

$(TEST_CLASS_BIN): $(BIN)
	$(JAVA_HOME)/bin/javac -source 1.3 -target 1.1 -d $(BUILD_DIR) $(SRC_DIR)/*.java $(GENERATED_DIR)/*.java

test:
	$(JAVA_HOME)/bin/java -Djava.library.path=build -cp build $(TEST_NAME)

clean:
	 rm -rf $(BIN) $(OBJS) $(GENERATED_DIR_BASE) $(BUILD_DIR)
