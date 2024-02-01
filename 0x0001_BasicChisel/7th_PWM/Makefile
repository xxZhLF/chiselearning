TARGET := PWM

.PHONY: all
all:
	mill $(TARGET).compile

.PHONY: run
run:
	mill $(TARGET).run

.PHONY: test
test:
	mill $(TARGET).test

.PHONY: as
as:
	mill show $(TARGET).assembly

.PHONY: clean
clean:
	rm -rf out target HDL test_run_dir