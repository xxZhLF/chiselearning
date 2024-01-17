TARGET := NoProcess

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