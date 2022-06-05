; TODO fix this file
include "rst.asm"

include "entry.asm"

include "test_alu.asm"
include "test_bitwise.asm"
include "test_call.asm"
include "test_cpl_daa.asm"
include "test_jump.asm"
include "test_sp.asm"
include "test_stop.asm"
include "test_interrupts.asm"

jp done
include "done.asm"
