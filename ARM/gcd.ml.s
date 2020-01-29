.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #16             
        LDR    r4, [fp, #40]           
        MOV    r5, #0                  
        CMP    r4, r5                  
        BNE    label2                  
        LDR    r4, [fp, #44]           
        MOV    r0, r4                  
        B      label5                  
label2: LDR    r5, [fp, #40]           
        LDR    r6, [fp, #44]           
        CMP    r5, r6                  
        BGT    label4                  
        LDR    r5, [fp, #44]            @ let var6 = ? in...
        LDR    r6, [fp, #40]           
        SUB    r4, r5, r6              
        LDR    r5, [fp, #40]            @ let var4 = ? in...
        MOV    r0, r5                  
        MOV    r1, r4                  
        LDR    r5, [fp, #48]           
        MOV    r2, r5                  
        LDR    r6, [fp, #48]           
        LDR    r5, [r6]                
        PUSH   {r0, r1, r2}            
        PUSH   {r6}                     @ Closure info
        BLX    r5                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1, r2}            
        MOV    r0, r4                  
        B      label5                  
label4: LDR    r5, [fp, #40]            @ let var10 = ? in...
        LDR    r6, [fp, #44]           
        SUB    r4, r5, r6              
        LDR    r5, [fp, #44]            @ let var8 = ? in...
        MOV    r0, r5                  
        MOV    r1, r4                  
        LDR    r5, [fp, #48]           
        MOV    r2, r5                  
        LDR    r6, [fp, #48]           
        LDR    r5, [r6]                
        PUSH   {r0, r1, r2}            
        PUSH   {r6}                     @ Closure info
        BLX    r5                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1, r2}            
        MOV    r0, r4                  
label5: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label6: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #28             
        LDR    r4, [fp, #-4]            @ let fun0 = ? in...
        MOV    r4, #4                  
        LDR    r5, heap_start          
        LDR    r6, heap_offset         
        LDR    r7, [r5]                
        LDR    r8, [r6]                
        ADD    r7, r7, r8              
        ADD    r8, r8, r4              
        STR    r8, [r6]                
        MOV    r4, r7                  
        STR    r4, [fp, #-4]           
        LDR    r5, =label1              @ let addr_fun0 = ? in...
        LDR    r6, [fp, #-4]            @ let tmp0 = ? in...
        MOV    r4, #0                  
        LSL    r4, #2                  
        LDR    r7, [r6]                
        STR    r5, [r6, r4]            
        MOV    r5, r7                  
        LDR    r5, =#21600              @ let var15 = ? in...
        LDR    r4, =#337500             @ let var16 = ? in...
        MOV    r0, r5                   @ let var14 = ? in...
        MOV    r1, r4                  
        LDR    r6, [fp, #-4]           
        MOV    r2, r6                  
        LDR    r7, [fp, #-4]           
        LDR    r6, [r7]                
        PUSH   {r0, r1, r2}            
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1, r2}            
        MOV    r0, r4                   @ let var12 = ? in...
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
