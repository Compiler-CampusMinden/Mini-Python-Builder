############
# Programs #
############

MKDIR := mkdir
MKDIR_P := mkdir -p

#############
# Locations #
#############

# make SRCDIR and INCLUDEDIR available to doxygen
export SRCDIR := $(realpath ./src)
export INCLUDEDIR := $(realpath ./include)
DOXYFILE := $(realpath ./Doxyfile)

# make DOCDIR available to doxygen
export DOCDIR := $(abspath ./doc)
BINDIR := $(abspath ./bin)
OUTDIR := $(abspath ./out)

#################
# Default Flags #
#################

CFLAGS ?= -pedantic -Wall -Werror
CFLAGS += -std=c11 -I$(INCLUDEDIR)
CFLAGS += -ffile-prefix-map=$(SRCDIR)=.
# make getline() (see man getline(3)) available
CFLAGS += -D_POSIX_C_SOURCE=200809L

ifdef DEBUG
CFLAGS += -ggdb -DMINI_PYTHON_DEBUG=1 -Og
LDFLAGS += -ggdb -Og
else
# disable assertions
# not for now CFLAGS += -DNDEBUG
endif

###########
# Targets #
###########

BIN := $(BINDIR)/program

# supports up to two directories, add more '*/' elements in a wildcard to support deeper nesting
SRC := $(wildcard $(SRCDIR)/*.c) $(wildcard $(SRCDIR)/*/*.c) $(wildcard $(SRCDIR)/*/*/*.c)

OBJECT := $(patsubst $(SRCDIR)/%.c,$(OUTDIR)/%.o,$(SRC))
DEPENDENCY := $(OBJECT:.o=.d)


#########
# Rules #
#########

.PHONY: all
all: $(BIN)

ifneq ($(MAKECMDGOALS),clean)
$(DEPENDENCY) : $(OUTDIR)/%.d: $(SRCDIR)/%.c
	$(if $(wildcard $(dir $@)),,$(MKDIR_P) $(dir $@))
	$(CC) $(CFLAGS) -MM $< > $@
-include $(DEPENDENCY)
endif

$(BIN): $(OBJECT)
	$(if $(wildcard $(dir $@)),,$(MKDIR_P) $(dir $@))
	$(CC) $(LDFLAGS) $^ $(LOADLIBS) $(LDLIBS) -o $@

$(OBJECT) : $(OUTDIR)/%.o: $(SRCDIR)/%.c
	$(if $(wildcard $(dir $@)),,$(MKDIR_P) $(dir $@))
	$(CC) $(CPPFLAGS) $(CFLAGS) -c -o $@ $<

.PHONY: run
run: $(BIN)
	./bin/program

.PHONY: clean
clean:
	$(RM) -r $(BIN) $(OBJECT) $(DEPENDENCY) $(wildcard $(DOCDIR)/*)

.PHONY: doc
doc:
	$(if $(wildcard $(dir $@)),,$(MKDIR_P) $(dir $@))
	doxygen $(DOXYFILE)

.PHONY: lint
lint:
	clang-tidy --checks=*,-bugprone-reserved-identifier,-cert-dcl37-c,-cert-dcl51-cpp,-altera-struct-pack-align $(SRC)
