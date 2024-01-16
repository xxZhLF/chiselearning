TARGET := Hello

.PHONY: all
all:
	mill $(TARGET).compile

.PHONY: run
run:
	mill $(TARGET).run

.PHONY: ut
ut:
	mill $(TARGET).test

.PHONY: as
as:
	mill show $(TARGET).assembly
