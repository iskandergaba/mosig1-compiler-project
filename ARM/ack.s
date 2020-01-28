.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #32             
        LDR    r4, [fp, #40]           
        MOV    r5, #0                  
        CMP    r4, r5                  
        BGT    label2                  
        MOV    r4, #1                   @ let var3 = ? in...
        LDR    r5, [fp, #44]           
        ADD    r0, r5, r4              
        B      label5                  
label2: LDR    r5, [fp, #44]           
        MOV    r6, #0                  
        CMP    r5, r6                  
        BGT    label4                  
        LDR    r5, [fp, #40]            @ let var7 = ? in...
        MOV    r6, #1                  
        SUB    r4, r5, r6              
        MOV    r5, #1                   @ let var10 = ? in...
        MOV    r0, r4                   @ let var6 = ? in...
        MOV    r1, r5                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     label1                   @ call _fun0
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        B      label5                  
label4: LDR    r6, [fp, #40]            @ let var13 = ? in...
        MOV    r7, #1                  
        SUB    r4, r6, r7              
        LDR    r6, [fp, #44]            @ let var19 = ? in...
        MOV    r7, #1                  
        SUB    r5, r6, r7              
        LDR    r6, [fp, #40]            @ let var17 = ? in...
        MOV    r0, r6                  
        MOV    r1, r5                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     label1                   @ call _fun0
        MOV    r5, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var12 = ? in...
        MOV    r1, r5                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     label1                   @ call _fun0
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                  
label5: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label6: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #16             
        MOV    r4, #3                   @ let var27 = ? in...
        MOV    r5, #10                  @ let var28 = ? in...
        MOV    r0, r4                   @ let var26 = ? in...
        MOV    r1, r5                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     label1                   @ call _fun0
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var24 = ? in...
        PUSH   {r0}                    
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     _min_caml_print_int      @ call _min_caml_print_int
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0}                    
        MOV    r0, r4                  
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

_start: MOV    r0, #0                   @ Heap allocation
        MOV    r1, #4096               
        MOV    r2, #0x3                
        MOV    r3, #0x22               
        MOV    r4, #-1                 
        MOV    r5, #0                  
        MOV    r7, #0xc0                @ mmap2() syscall number
        SVC    #0                       @ Execute syscall
        LDR    r4, heap_start          
        STR    r0, [r4]                 @ Store the heap start at the 'heap_start' symbol
        BL     label6                   @ Branch to main function
        MOV    r0, #0                   @ Exit syscall
        MOV    r7, #1                  
        SVC    #0                      
heap_start: .word heap_start_addr
heap_offset: .word heap_offset_addr
