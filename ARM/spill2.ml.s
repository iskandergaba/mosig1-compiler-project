.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #0              
        LDR    r0, =#12345             
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label2: PUSH   {r4-r11, lr}             @ Function _fun1
        ADD    r11, sp, #0             
        SUB    sp, sp, #4              
        MOV    r4, #1                   @ let var1 = ? in...
        LDR    r5, [fp, #40]           
        ADD    r0, r5, r4              
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label3: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #184            
        LDR    r4, [fp, #-8]            @ let fun0 = ? in...
        MOV    r4, #4                  
        LDR    r5, heap_start          
        LDR    r6, heap_offset         
        LDR    r7, [r5]                
        LDR    r8, [r6]                
        ADD    r7, r7, r8              
        ADD    r8, r8, r4              
        STR    r8, [r6]                
        MOV    r4, r7                  
        STR    r4, [fp, #-8]           
        LDR    r5, =label1              @ let addr_fun0 = ? in...
        LDR    r6, [fp, #-8]            @ let tmp1 = ? in...
        MOV    r4, #0                  
        LSL    r4, #2                  
        LDR    r7, [r6]                
        STR    r5, [r6, r4]            
        MOV    r5, r7                  
        LDR    r4, [fp, #-4]            @ let fun1 = ? in...
        MOV    r4, #4                  
        LDR    r6, heap_start          
        LDR    r7, heap_offset         
        LDR    r8, [r6]                
        LDR    r9, [r7]                
        ADD    r8, r8, r9              
        ADD    r9, r9, r4              
        STR    r9, [r7]                
        MOV    r4, r8                  
        STR    r4, [fp, #-4]           
        LDR    r5, =label2              @ let addr_fun1 = ? in...
        LDR    r6, [fp, #-4]            @ let tmp0 = ? in...
        MOV    r4, #0                  
        LSL    r4, #2                  
        LDR    r7, [r6]                
        STR    r5, [r6, r4]            
        MOV    r5, r7                  
        MOV    r5, #10                  @ let var3 = ? in...
        MOV    r4, #1                   @ let var4 = ? in...
        MOV    r0, r5                   @ let var2 = ? in...
        MOV    r1, r4                  
        PUSH   {r0, r1}                
        SUB    sp, sp, #4               @ Placeholder for closure info
        BL     _min_caml_create_array   @ call _min_caml_create_array
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        LDR    r6, [fp, #-12]           @ let var7 = ? in...
        MOV    r0, r5                  
        LDR    r6, [fp, #-8]           
        MOV    r1, r6                  
        LDR    r7, [fp, #-8]           
        LDR    r6, [r7]                
        PUSH   {r0, r1}                
        PUSH   {r7}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r6, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        STR    r6, [fp, #-12]          
        LDR    r5, =#67890              @ let var9 = ? in...
        LDR    r6, [fp, #-16]           @ let var10 = ? in...
        MOV    r6, #0                  
        LSL    r6, #2                  
        ADD    r4, r4, r6              
        LDR    r6, [r4]                
        STR    r6, [fp, #-16]          
        LDR    r6, [fp, #-20]           @ let var13 = ? in...
        LDR    r6, [fp, #-16]          
        LDR    r7, [fp, #-16]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-20]          
        LDR    r6, [fp, #-24]           @ let var14 = ? in...
        LDR    r6, [fp, #-20]          
        LDR    r7, [fp, #-20]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-24]          
        LDR    r6, [fp, #-28]           @ let var15 = ? in...
        LDR    r6, [fp, #-24]          
        LDR    r7, [fp, #-24]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-28]          
        LDR    r6, [fp, #-32]           @ let var16 = ? in...
        LDR    r6, [fp, #-28]          
        LDR    r7, [fp, #-28]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-32]          
        LDR    r6, [fp, #-36]           @ let var17 = ? in...
        LDR    r6, [fp, #-32]          
        LDR    r7, [fp, #-32]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-36]          
        LDR    r6, [fp, #-40]           @ let var18 = ? in...
        LDR    r6, [fp, #-36]          
        LDR    r7, [fp, #-36]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-40]          
        LDR    r6, [fp, #-44]           @ let var19 = ? in...
        LDR    r6, [fp, #-40]          
        LDR    r7, [fp, #-40]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-44]          
        LDR    r6, [fp, #-48]           @ let var20 = ? in...
        LDR    r6, [fp, #-44]          
        LDR    r7, [fp, #-44]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-48]          
        LDR    r6, [fp, #-52]           @ let var21 = ? in...
        LDR    r6, [fp, #-48]          
        LDR    r7, [fp, #-48]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-52]          
        LDR    r6, [fp, #-56]           @ let var22 = ? in...
        LDR    r6, [fp, #-52]          
        LDR    r7, [fp, #-52]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-56]          
        LDR    r6, [fp, #-60]           @ let var23 = ? in...
        LDR    r6, [fp, #-56]          
        LDR    r7, [fp, #-56]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-60]          
        LDR    r6, [fp, #-64]           @ let var24 = ? in...
        LDR    r6, [fp, #-60]          
        LDR    r7, [fp, #-60]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-64]          
        LDR    r6, [fp, #-68]           @ let var25 = ? in...
        LDR    r6, [fp, #-64]          
        LDR    r7, [fp, #-64]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-68]          
        LDR    r6, [fp, #-72]           @ let var26 = ? in...
        LDR    r6, [fp, #-68]          
        LDR    r7, [fp, #-68]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-72]          
        LDR    r6, [fp, #-76]           @ let var27 = ? in...
        LDR    r6, [fp, #-72]          
        LDR    r7, [fp, #-72]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-76]          
        MOV    r6, #1                   @ let var30 = ? in...
        LSL    r6, #2                  
        ADD    r4, r4, r6              
        LDR    r4, [r4]                
        MOV    r6, #0                   @ let var29 = ? in...
        CMP    r4, r6                  
        BNE    label4                  
        MOV    r0, r5                  
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
        B      label5                  
label4: LDR    r6, [fp, #-16]           @ let var48 = ? in...
        LDR    r7, [fp, #-20]          
        ADD    r4, r6, r7              
        LDR    r6, [fp, #-24]           @ let var47 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-28]           @ let var46 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-32]           @ let var45 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-36]           @ let var44 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-40]           @ let var43 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-44]           @ let var42 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-48]           @ let var41 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-52]           @ let var40 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-56]           @ let var39 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-60]           @ let var38 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-64]           @ let var37 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-68]           @ let var36 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-72]           @ let var35 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-76]           @ let var34 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-12]          
        ADD    r4, r4, r6              
label5: MOV    r0, r4                   @ let var28 = ? in...
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
        BL     label3                   @ Branch to main function
        MOV    r0, #0                   @ Exit syscall
        MOV    r7, #1                  
        SVC    #0                      
heap_start: .word heap_start_addr
heap_offset: .word heap_offset_addr
