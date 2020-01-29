.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #0              
        MOV    r0, #123                
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label2: PUSH   {r4-r11, lr}             @ Function _fun1
        ADD    r11, sp, #0             
        SUB    sp, sp, #0              
        LDR    r0, =#456               
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label3: PUSH   {r4-r11, lr}             @ Function _fun2
        ADD    r11, sp, #0             
        SUB    sp, sp, #0              
        LDR    r0, =#789               
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label4: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #84             
        MOV    r4, #4                   @ let fun0 = ? in...
        LDR    r5, heap_start          
        LDR    r6, heap_offset         
        LDR    r7, [r5]                
        LDR    r8, [r6]                
        ADD    r7, r7, r8              
        ADD    r8, r8, r4              
        STR    r8, [r6]                
        MOV    r4, r7                  
        LDR    r5, =label1              @ let addr_fun0 = ? in...
        MOV    r6, #0                   @ let tmp2 = ? in...
        LSL    r6, #2                  
        LDR    r7, [r4]                
        STR    r5, [r4, r6]            
        MOV    r5, r7                  
        LDR    r6, [fp, #-4]            @ let fun1 = ? in...
        MOV    r6, #4                  
        LDR    r7, heap_start          
        LDR    r8, heap_offset         
        LDR    r9, [r7]                
        LDR    r10, [r8]               
        ADD    r9, r9, r10             
        ADD    r10, r10, r6            
        STR    r10, [r8]               
        MOV    r6, r9                  
        STR    r6, [fp, #-4]           
        LDR    r5, =label2              @ let addr_fun1 = ? in...
        LDR    r7, [fp, #-4]            @ let tmp1 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r5, [r7, r6]            
        MOV    r5, r8                  
        LDR    r6, [fp, #-8]            @ let fun2 = ? in...
        MOV    r6, #4                  
        LDR    r7, heap_start          
        LDR    r8, heap_offset         
        LDR    r9, [r7]                
        LDR    r10, [r8]               
        ADD    r9, r9, r10             
        ADD    r10, r10, r6            
        STR    r10, [r8]               
        MOV    r6, r9                  
        STR    r6, [fp, #-8]           
        LDR    r5, =label3              @ let addr_fun2 = ? in...
        LDR    r7, [fp, #-8]            @ let tmp0 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r5, [r7, r6]            
        MOV    r5, r8                  
        MOV    r0, r5                   @ let var2 = ? in...
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        LDR    r6, [fp, #-12]           @ let var6 = ? in...
        MOV    r0, r5                  
        LDR    r6, [fp, #-4]           
        MOV    r1, r6                  
        LDR    r7, [fp, #-4]           
        LDR    r6, [r7]                
        PUSH   {r0, r1}                
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r6, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        STR    r6, [fp, #-12]          
        MOV    r0, r5                   @ let var14 = ? in...
        LDR    r6, [fp, #-8]           
        MOV    r1, r6                  
        LDR    r7, [fp, #-8]           
        LDR    r6, [r7]                
        PUSH   {r0, r1}                
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r5, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r6, #0                   @ let var11 = ? in...
        CMP    r5, r6                  
        BNE    label5                  
        MOV    r5, #1                   @ let var18 = ? in...
        ADD    r5, r4, r5              
        B      label6                  
label5: MOV    r5, #2                   @ let var20 = ? in...
        LDR    r6, [fp, #-12]          
        ADD    r5, r6, r5              
label6: ADD    r4, r5, r4               @ let var10 = ? in...
        LDR    r6, [fp, #-12]           @ let var9 = ? in...
        ADD    r4, r4, r6              
        MOV    r0, r4                   @ let var8 = ? in...
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
