export PATH := /opt/gnu/arm/bin:$(PATH)

AS=arm-none-eabi-as
LD=arm-none-eabi-ld

ASM=$(wildcard *.s)
PRG=$(subst .s,.arm,${ASM})

all: ${PRG}

echo:
	echo ${ASM}
	echo ${PRG}

%.o : %.s libmincaml.S libarray.S math.S
	${AS} -o $@ $^

%.arm : %.o
	${LD} -o $@ $^ -L. -lgcc -lc -lm

test: ${PRG}
	@for p in ${PRG} ; do \
	  echo "*** Launching $$p"; \
	  qemu-arm ./$$p ; \
	  echo "" ; \
	done

