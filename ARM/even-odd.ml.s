.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun1
        ADD    r11, sp, #0             
        SUB    sp, sp, #40             
        LDR    r4, [fp, #-4]            @ let fun0_self_clos = ? in...
        LDR    r4, [fp, #36]           
        MOV    r5, #1                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        STR    r4, [fp, #-4]           
        LDR    r4, [fp, #-4]            @ let fun0_self_clos = ? in...
        LDR    r4, [fp, #36]           
        MOV    r5, #2                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        STR    r4, [fp, #-4]           
        LDR    r4, [fp, #-4]            @ let fun0_self_clos = ? in...
        LDR    r4, [fp, #36]           
        MOV    r5, #3                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        STR    r4, [fp, #-4]           
        LDR    r4, [fp, #-4]            @ let fun0_self_clos = ? in...
        LDR    r4, [fp, #36]           
        MOV    r5, #4                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        STR    r4, [fp, #-4]           
        LDR    r4, [fp, #36]            @ let var1 = ? in...
        MOV    r5, #5                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r5, [r4]                
        LDR    r4, [fp, #40]           
        MOV    r6, #0                  
        CMP    r4, r6                  
        BGT    label2                  
        MOV    r4, #0                   @ let var9 = ? in...
        LDR    r6, [fp, #40]           
        CMP    r4, r6                  
        BGT    label4                  
        MOV    r0, r5                  
        B      label5                  
label4: LDR    r6, [fp, #40]            @ let var12 = ? in...
        MOV    r7, #1                  
        ADD    r4, r6, r7              
        MOV    r0, r4                   @ let var11 = ? in...
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
label5: MOV    r0, r4                  
        B      label3                  
label2: LDR    r6, [fp, #40]            @ let var5 = ? in...
        MOV    r7, #1                  
        SUB    r4, r6, r7              
        MOV    r0, r4                   @ let var4 = ? in...
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
        MOV    r0, r4                  
label3: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label6: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #60             
        LDR    r4, [fp, #36]            @ let var1 = ? in...
        MOV    r5, #1                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        LDR    r5, [fp, #-8]            @ let var0 = ? in...
        LDR    r5, [fp, #36]           
        MOV    r6, #2                  
        LSL    r6, #2                  
        ADD    r5, r5, r6              
        LDR    r5, [r5]                
        STR    r5, [fp, #-8]           
        LDR    r5, [fp, #-4]            @ let fun1 = ? in...
        MOV    r5, #24                 
        LDR    r6, heap_start          
        LDR    r7, heap_offset         
        LDR    r8, [r6]                
        LDR    r9, [r7]                
        ADD    r8, r8, r9              
        ADD    r9, r9, r5              
        STR    r9, [r7]                
        MOV    r5, r8                  
        STR    r5, [fp, #-4]           
        LDR    r5, =label1              @ let addr_fun1 = ? in...
        LDR    r7, [fp, #-4]            @ let tmp5 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r5, [r7, r6]            
        MOV    r5, r8                  
        LDR    r6, [fp, #44]            @ let tmp4 = ? in...
        LDR    r8, [fp, #-4]           
        MOV    r7, #1                  
        LSL    r7, #2                  
        LDR    r9, [r8]                
        STR    r6, [r8, r7]            
        MOV    r5, r9                  
        LDR    r6, [fp, #44]            @ let tmp3 = ? in...
        LDR    r8, [fp, #-4]           
        MOV    r7, #2                  
        LSL    r7, #2                  
        LDR    r9, [r8]                
        STR    r6, [r8, r7]            
        MOV    r5, r9                  
        LDR    r6, [fp, #44]            @ let tmp2 = ? in...
        LDR    r8, [fp, #-4]           
        MOV    r7, #3                  
        LSL    r7, #2                  
        LDR    r9, [r8]                
        STR    r6, [r8, r7]            
        MOV    r5, r9                  
        LDR    r6, [fp, #44]            @ let tmp1 = ? in...
        LDR    r8, [fp, #-4]           
        MOV    r7, #4                  
        LSL    r7, #2                  
        LDR    r9, [r8]                
        STR    r6, [r8, r7]            
        MOV    r5, r9                  
        LDR    r7, [fp, #-4]            @ let tmp0 = ? in...
        MOV    r6, #5                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r4, [r7, r6]            
        MOV    r4, r8                  
        LDR    r6, [fp, #40]           
        MOV    r7, #0                  
        CMP    r6, r7                  
        BGT    label7                  
        MOV    r4, #0                   @ let var23 = ? in...
        LDR    r6, [fp, #40]           
        CMP    r4, r6                  
        BGT    label9                  
        LDR    r6, [fp, #-8]           
        MOV    r0, r6                  
        B      label10                 
label9: LDR    r7, [fp, #40]            @ let var26 = ? in...
        MOV    r8, #1                  
        ADD    r4, r7, r8              
        MOV    r0, r4                   @ let var25 = ? in...
        LDR    r7, [fp, #-4]           
        MOV    r1, r7                  
        LDR    r8, [fp, #-4]           
        LDR    r7, [r8]                
        PUSH   {r0, r1}                
        PUSH   {r8}                     @ Closure info
        BLX    r7                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
label10: MOV    r0, r4                  
         B      label8                  
label7: LDR    r7, [fp, #40]            @ let var19 = ? in...
        MOV    r8, #1                  
        SUB    r4, r7, r8              
        MOV    r0, r4                   @ let var18 = ? in...
        LDR    r7, [fp, #-4]           
        MOV    r1, r7                  
        LDR    r8, [fp, #-4]           
        LDR    r7, [r8]                
        PUSH   {r0, r1}                
        PUSH   {r8}                     @ Closure info
        BLX    r7                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                  
label8: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label11: PUSH   {r4-r11, lr}             @ Function _
         ADD    r11, sp, #0             
         SUB    sp, sp, #40             
         LDR    r4, [fp, #-8]            @ let var0 = ? in...
         MOV    r4, #123                
         STR    r4, [fp, #-8]           
         LDR    r5, =#456                @ let var1 = ? in...
         LDR    r4, [fp, #-4]            @ let fun0 = ? in...
         MOV    r4, #12                 
         LDR    r6, heap_start          
         LDR    r7, heap_offset         
         LDR    r8, [r6]                
         LDR    r9, [r7]                
         ADD    r8, r8, r9              
         ADD    r9, r9, r4              
         STR    r9, [r7]                
         MOV    r4, r8                  
         STR    r4, [fp, #-4]           
         LDR    r4, =label6              @ let addr_fun0 = ? in...
         LDR    r7, [fp, #-4]            @ let tmp8 = ? in...
         MOV    r6, #0                  
         LSL    r6, #2                  
         LDR    r8, [r7]                
         STR    r4, [r7, r6]            
         MOV    r4, r8                  
         LDR    r7, [fp, #-4]            @ let tmp7 = ? in...
         MOV    r6, #1                  
         LSL    r6, #2                  
         LDR    r8, [r7]                
         STR    r5, [r7, r6]            
         MOV    r4, r8                  
         LDR    r6, [fp, #-8]            @ let tmp6 = ? in...
         LDR    r8, [fp, #-4]           
         MOV    r7, #2                  
         LSL    r7, #2                  
         LDR    r9, [r8]                
         STR    r6, [r8, r7]            
         MOV    r4, r9                  
         LDR    r4, =#789                @ let var33 = ? in...
         MOV    r0, r4                   @ let var32 = ? in...
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
         MOV    r0, r4                   @ let var30 = ? in...
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
        BL     label11                  @ Branch to main function
        MOV    r0, #0                   @ Exit syscall
        MOV    r7, #1                  
        SVC    #0                      
heap_start: .word heap_start_addr
heap_offset: .word heap_offset_addr
