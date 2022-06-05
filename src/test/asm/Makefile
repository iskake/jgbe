AS  := rgbasm
LD  := rgblink
FIX := rgbfix

ASFLAGS  := -L
FIXFLAGS := -v -p 0xff

GB := test_alu.gb\
	test_jump.gb\
	test_call.gb\
	test_bitwise.gb\
	test_cpl_daa.gb\
	test_stop.gb\
	test_sp.gb\
	test_tile.gb\
	test_interrupts.gb
	
all: $(GB)

%.gb: %.o
	$(LD) $< -o $@
	$(FIX) $(FIXFLAGS) $@
	mkdir -p bin/
	mv $@ bin/

%.o: %.asm
	$(AS) $< $(ASFLAGS) -o $@

clean:
	rm -rf *.gb *.o bin/
