_min_caml_create_array:
    PUSH {r4-r11, lr}
    ADD r11, sp, #0
    LDR r4, heap_start
    LDR r5, heap_offset
    LDR r6, [r4]
    LDR r7, [r5]
    ADD r6, r6, r7
    ADD r7, r7, r0
    STR r7, [r5]
    MOV r4, #0
    MOV r5, r6
_min_caml_create_array_loop:
    CMP r0, r4
    BEQ _min_caml_create_array_end
    STR r1, [r5]
    ADD r5, r5, #4
    SUB r0, r0, #1
    B   _min_caml_create_array_loop
_min_caml_create_array_end:
    MOV r0, r6
    SUB sp, r11, #0
    POP {r4-r11, lr}
    BX  LR

