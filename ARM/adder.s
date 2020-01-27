.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: MOV    ip, sp                   @ Function _fun1
        PUSH   {fp, ip, lr, pc}        
        MOV    fp, ip                  
        ADD    sp, ip, #-4             
        LDR    r4, [fp, #20]            @ let arg0 = ? in...
        MOV    r5, #4                  
        ADD    r4, r4, r5              
        LDR    r4, [r4]                
        LDR    r5, [fp, #4]            
        ADD    r0, r4, r5              
        LDM    sp, {fp, sp, lr}        
        BX     lr                       @ Return

label2: MOV    ip, sp                   @ Function _fun0
        PUSH   {fp, ip, lr, pc}        
        MOV    fp, ip                  
        ADD    sp, ip, #-16            
        MOV    r5, #8                   @ let fun1 = ? in...
        LDR    r6, heap_start          
        LDR    r7, heap_offset         
        LDR    r8, [r6]                
        LDR    r9, [r7]                
        ADD    r8, r8, r9              
        ADD    r9, r9, r5              
        STR    r9, [r7]                
        MOV    r4, r8                  
        LDR    r5, =label1              @ let addr_fun1 = ? in...
        MOV    r6, #0                   @ let tmp1 = ? in...
        ADD    r4, r4, r6              
        LDR    r7, [r4]                
        STR    r5, [r4]                
        MOV    r5, r7                  
        LDR    r6, [fp, #4]             @ let tmp0 = ? in...
        MOV    r7, #4                  
        ADD    r4, r4, r7              
        LDR    r8, [r4]                
        STR    r6, [r4]                
        MOV    r5, r8                  
        LDM    sp, {fp, sp, lr}        
        BX     lr                       @ Return

label3: MOV    ip, sp                   @ Function _
        PUSH   {fp, ip, lr, pc}        
        MOV    fp, ip                  
        ADD    sp, ip, #-20            
        MOV    r4, #7                   @ let var3 = ? in...
        MOV    r5, #3                   @ let var6 = ? in...
        MOV    r0, r5                   @ let var5 = ? in...
        PUSH   {r0}                    
        BL     label2                   @ call _fun0
        POP    {r0}                    
        MOV    r5, r0                  
        MOV    r0, r4                   @ let var2 = ? in...
        LDR    r6, [r5]                
        PUSH   {r0, r5}                
        BX     r6                       @ Apply closure
        POP    {r0, r5}                
        MOV    r0, r4                   @ let var0 = ? in...
        PUSH   {r0}                    
        BL     _min_caml_print_int      @ call _min_caml_print_int
        POP    {r0}                    
        MOV    r4, r0                  
        LDM    sp, {fp, sp, lr}        
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
