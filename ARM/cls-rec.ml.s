.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #12             
        LDR    r4, [fp, #36]            @ let var0 = ? in...
        MOV    r5, #1                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        LDR    r5, [fp, #40]           
        MOV    r6, #0                  
        CMP    r5, r6                  
        BNE    label2                  
        MOV    r0, #0                  
        B      label3                  
label2: LDR    r5, [fp, #40]            @ let var6 = ? in...
        MOV    r6, #1                  
        SUB    r5, r5, r6              
        MOV    r0, r5                   @ let var5 = ? in...
        LDR    r6, [fp, #44]           
        MOV    r1, r6                  
        LDR    r7, [fp, #44]           
        LDR    r6, [r7]                
        PUSH   {r0, r1}                
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r5, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        ADD    r0, r4, r5              
label3: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label4: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #32             
        MOV    r4, #10                  @ let var0 = ? in...
        LDR    r5, [fp, #-4]            @ let fun0 = ? in...
        MOV    r5, #8                  
        LDR    r6, heap_start          
        LDR    r7, heap_offset         
        LDR    r8, [r6]                
        LDR    r9, [r7]                
        ADD    r8, r8, r9              
        ADD    r9, r9, r5              
        STR    r9, [r7]                
        MOV    r5, r8                  
        STR    r5, [fp, #-4]           
        LDR    r5, =label1              @ let addr_fun0 = ? in...
        LDR    r7, [fp, #-4]            @ let tmp1 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r5, [r7, r6]            
        MOV    r5, r8                  
        LDR    r7, [fp, #-4]            @ let tmp0 = ? in...
        MOV    r6, #1                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r4, [r7, r6]            
        MOV    r4, r8                  
        MOV    r4, #123                 @ let var13 = ? in...
        MOV    r0, r4                   @ let var12 = ? in...
        LDR    r6, [fp, #-4]           
        MOV    r1, r6                  
        LDR    r7, [fp, #-4]           
        LDR    r6, [r7]                
        PUSH   {r0, r1}                
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var10 = ? in...
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
        BL     label4                   @ Branch to main function
        MOV    r0, #0                   @ Exit syscall
        MOV    r7, #1                  
        SVC    #0                      
heap_start: .word heap_start_addr
heap_offset: .word heap_offset_addr
