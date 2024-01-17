TARGET := NoProc

.PHONY: all
all:
	mill $(TARGET).compile

.PHONY: run
run:
	mill $(TARGET).run

.PHONY: as
as:
	mill show $(TARGET).assembly

.PHONY: clean
clean:
	rm -rf out
	rm $(TARGET).anno.json
	rm $(TARGET).fir
	rm $(TARGET).v