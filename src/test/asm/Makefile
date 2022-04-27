AS  := rgbasm
LD  := rgblink
FIX := rgbfix

ASFLAGS  := -L
FIXFLAGS := -v -p 0xff

ZB := test_alu.zb\
	test_jump.zb\
	test_call.zb\
	test_bitwise.zb\
	test_cpl_daa.zb\
	test_stop.zb\
	test_sp.zb
	
	
all: $(ZB)

%.zb: %.o
	$(LD) $< -o $@
	mkdir -p bin/
	mv $@ bin/

%.o: %.asm
	$(AS) $< $(ASFLAGS) -o $@

clean:
	rm -rf *.zb *.o bin/
