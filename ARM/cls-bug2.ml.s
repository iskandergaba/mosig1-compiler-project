.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun1
        ADD    r11, sp, #0             
        SUB    sp, sp, #32             
        MOV    r4, #0                   @ let var16 = ? in...
        LDR    r5, [fp, #40]           
        CMP    r4, r5                  
        BGT    label2                  
        LDR    r5, [fp, #40]            @ let var17 = ? in...
        MOV    r0, r5                  
        PUSH   {r0}                    
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     _min_caml_print_int      @ call _min_caml_print_int
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0}                    
        MOV    r4, #1                   @ let var18 = ? in...
        LDR    r5, [fp, #44]            @ let var19 = ? in...
        MOV    r5, r5                  
        MOV    r0, r4                   @ let var20 = ? in...
        MOV    r1, r5                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     _min_caml_create_array   @ call _min_caml_create_array
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        LDR    r6, [fp, #40]            @ let var22 = ? in...
        MOV    r7, #1                  
        SUB    r5, r6, r7              
        MOV    r6, #0                   @ let var24 = ? in...
        LSL    r6, #2                  
        ADD    r4, r4, r6              
        LDR    r4, [r4]                
        MOV    r0, r5                   @ let var25 = ? in...
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                  
        B      label2                  
label2: SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label4: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #20             
        MOV    r4, #4                   @ let fun1 = ? in...
        LDR    r5, heap_start          
        LDR    r6, heap_offset         
        LDR    r7, [r5]                
        LDR    r8, [r6]                
        ADD    r7, r7, r8              
        ADD    r8, r8, r4              
        STR    r8, [r6]                
        MOV    r4, r7                  
        LDR    r5, =label1              @ let addr_fun1 = ? in...
        MOV    r6, #0                   @ let tmp0 = ? in...
        LSL    r6, #2                  
        LDR    r7, [r4]                
        STR    r5, [r4, r6]            
        MOV    r5, r7                  
        MOV    r5, #9                   @ let var26 = ? in...
        MOV    r0, r5                   @ let var28 = ? in...
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
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
