
# Install
BUILD_DIR = build
BIN = $(BUILD_DIR)/libnuklear-java
SRC_DIR = src
GENERATED_DIR = generated

ifndef JAVA_HOME
$(error JAVA_HOME is not set)
endif

# Flags
CFLAGS = -std=c99 -pedantic -fPIC -O0 -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I$(SRC_DIR)
LDFLAGS = -shared

SRC = $(SRC_DIR)/library.c $(GENERATED_DIR)/nuklear_wrap.c 
OBJS = $(SRC:.c=.o)

ifeq ($(OS),Windows_NT)
BIN := $(BIN).exe
LIBS = -lm
else
BIN := $(BIN).so
LIBS = -lm
endif

.PHONY: all clean

#all: $(BUILD_DIR)/NuklearDemo.class
all: $(BIN)

%.o: %.c
	$(CC) $(CFLAGS) -o $@ -c $< 

$(GENERATED_DIR)/nuklear_wrap.c: nuklear.i
	mkdir -p $(GENERATED_DIR) $(BUILD_DIR)
	swig3.0 -java -outdir $(GENERATED_DIR) -o $@ $<

$(BIN):$(OBJS)
	$(CC) $(LDFLAGS) -o $(BIN) $(LIBS) $(OBJS)

$(BUILD_DIR)/NuklearDemo.class: $(BIN)
	javac -d $(BUILD_DIR) $(SRC_DIR)/*.java $(GENERATED_DIR)/*.java

clean:
	 rm -rf $(BIN) $(OBJS) $(GENERATED_DIR) $(BUILD_DIR)
