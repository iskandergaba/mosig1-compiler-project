.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun6
        ADD    r11, sp, #0             
        SUB    sp, sp, #16             
        LDR    r4, [fp, #36]            @ let arg6 = ? in...
        MOV    r5, #4                  
        LSL    r5, #2                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        LDR    r5, [fp, #36]            @ let arg7 = ? in...
        MOV    r6, #8                  
        LSL    r6, #2                  
        ADD    r5, r5, r6              
        LDR    r5, [r5]                
        LDR    r6, [fp, #40]            @ let var18 = ? in...
        MOV    r0, r6                  
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var19 = ? in...
        MOV    r1, r5                  
        LDR    r6, [r5]                
        PUSH   {r0, r1}                
        PUSH   {r5}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                  
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label2: PUSH   {r4-r11, lr}             @ Function _fun5
        ADD    r11, sp, #0             
        SUB    sp, sp, #20             
        MOV    r4, #12                  @ let fun6 = ? in...
        LDR    r5, heap_start          
        LDR    r6, heap_offset         
        LDR    r7, [r5]                
        LDR    r8, [r6]                
        ADD    r7, r7, r8              
        ADD    r8, r8, r4              
        STR    r8, [r6]                
        MOV    r4, r7                  
        LDR    r5, =label1              @ let addr_fun6 = ? in...
        MOV    r6, #0                   @ let tmp2 = ? in...
        LSL    r6, #2                  
        LDR    r7, [r4]                
        STR    r5, [r4, r6]            
        MOV    r5, r7                  
        LDR    r6, [fp, #40]            @ let tmp1 = ? in...
        MOV    r7, #4                  
        LSL    r7, #2                  
        LDR    r8, [r4]                
        STR    r6, [r4, r7]            
        MOV    r5, r8                  
        LDR    r6, [fp, #44]            @ let tmp0 = ? in...
        MOV    r7, #8                  
        LSL    r7, #2                  
        LDR    r8, [r4]                
        STR    r6, [r4, r7]            
        MOV    r5, r8                  
        MOV    r0, r4                  
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label3: PUSH   {r4-r11, lr}             @ Function _fun7
        ADD    r11, sp, #0             
        SUB    sp, sp, #0              
        LDR    r4, [fp, #40]           
        LDR    r5, [fp, #40]           
        ADD    r0, r4, r5              
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label4: PUSH   {r4-r11, lr}             @ Function _fun8
        ADD    r11, sp, #0             
        SUB    sp, sp, #4              
        MOV    r4, #1                   @ let var20 = ? in...
        LDR    r5, [fp, #40]           
        ADD    r0, r5, r4              
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label5: PUSH   {r4-r11, lr}             @ Function _fun9
        ADD    r11, sp, #0             
        SUB    sp, sp, #4              
        MOV    r4, #1                   @ let var21 = ? in...
        LDR    r5, [fp, #40]           
        SUB    r0, r5, r4              
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label6: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #72             
        LDR    r4, [fp, #-4]            @ let fun5 = ? in...
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
        LDR    r5, =label2              @ let addr_fun5 = ? in...
        LDR    r6, [fp, #-4]            @ let tmp6 = ? in...
        MOV    r4, #0                  
        LSL    r4, #2                  
        LDR    r7, [r6]                
        STR    r5, [r6, r4]            
        MOV    r5, r7                  
        LDR    r4, [fp, #-8]            @ let fun7 = ? in...
        MOV    r4, #4                  
        LDR    r6, heap_start          
        LDR    r7, heap_offset         
        LDR    r8, [r6]                
        LDR    r9, [r7]                
        ADD    r8, r8, r9              
        ADD    r9, r9, r4              
        STR    r9, [r7]                
        MOV    r4, r8                  
        STR    r4, [fp, #-8]           
        LDR    r4, =label3              @ let addr_fun7 = ? in...
        LDR    r7, [fp, #-8]            @ let tmp5 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r4, [r7, r6]            
        MOV    r4, r8                  
        MOV    r6, #4                   @ let fun8 = ? in...
        LDR    r7, heap_start          
        LDR    r8, heap_offset         
        LDR    r9, [r7]                
        LDR    r10, [r8]               
        ADD    r9, r9, r10             
        ADD    r10, r10, r6            
        STR    r10, [r8]               
        MOV    r4, r9                  
        LDR    r5, =label4              @ let addr_fun8 = ? in...
        MOV    r6, #0                   @ let tmp4 = ? in...
        LSL    r6, #2                  
        LDR    r7, [r4]                
        STR    r5, [r4, r6]            
        MOV    r5, r7                  
        LDR    r6, [fp, #-12]           @ let fun9 = ? in...
        MOV    r6, #4                  
        LDR    r7, heap_start          
        LDR    r8, heap_offset         
        LDR    r9, [r7]                
        LDR    r10, [r8]               
        ADD    r9, r9, r10             
        ADD    r10, r10, r6            
        STR    r10, [r8]               
        MOV    r6, r9                  
        STR    r6, [fp, #-12]          
        LDR    r5, =label5              @ let addr_fun9 = ? in...
        LDR    r7, [fp, #-12]           @ let tmp3 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        LDR    r8, [r7]                
        STR    r5, [r7, r6]            
        MOV    r5, r8                  
        LDR    r6, [fp, #-8]            @ let var22 = ? in...
        MOV    r0, r6                  
        LDR    r6, [fp, #-12]          
        MOV    r1, r6                  
        LDR    r6, [fp, #-4]           
        MOV    r2, r6                  
        LDR    r7, [fp, #-4]           
        LDR    r6, [r7]                
        PUSH   {r0, r1, r2}            
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r5, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1, r2}            
        MOV    r0, r4                   @ let var25 = ? in...
        MOV    r1, r5                  
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
        MOV    r5, #123                 @ let var26 = ? in...
        MOV    r0, r5                   @ let var27 = ? in...
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var28 = ? in...
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
