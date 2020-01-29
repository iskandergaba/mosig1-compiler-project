.data
heap_start_addr: .word 0
heap_offset_addr: .word 0

.text
.global _start

label1: PUSH   {r4-r11, lr}             @ Function _fun0
        ADD    r11, sp, #0             
        SUB    sp, sp, #284            
        LDR    r4, [fp, #40]            @ let var0 = ? in...
        MOV    r5, #1                  
        ADD    r4, r4, r5              
        MOV    r5, #1                   @ let var3 = ? in...
        ADD    r5, r4, r5              
        LDR    r6, [fp, #-4]            @ let var6 = ? in...
        MOV    r6, #1                  
        ADD    r6, r5, r6              
        STR    r6, [fp, #-4]           
        LDR    r6, [fp, #-8]            @ let var9 = ? in...
        LDR    r6, [fp, #-4]           
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-8]           
        LDR    r6, [fp, #-12]           @ let var12 = ? in...
        LDR    r6, [fp, #-8]           
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-12]          
        LDR    r6, [fp, #-16]           @ let var15 = ? in...
        LDR    r6, [fp, #-12]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-16]          
        LDR    r6, [fp, #-20]           @ let var18 = ? in...
        LDR    r6, [fp, #-16]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-20]          
        LDR    r6, [fp, #-24]           @ let var21 = ? in...
        LDR    r6, [fp, #-20]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-24]          
        LDR    r6, [fp, #-28]           @ let var24 = ? in...
        LDR    r6, [fp, #-24]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-28]          
        LDR    r6, [fp, #-32]           @ let var27 = ? in...
        LDR    r6, [fp, #-28]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-32]          
        LDR    r6, [fp, #-36]           @ let var30 = ? in...
        LDR    r6, [fp, #-32]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-36]          
        LDR    r6, [fp, #-40]           @ let var33 = ? in...
        LDR    r6, [fp, #-36]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-40]          
        LDR    r6, [fp, #-44]           @ let var36 = ? in...
        LDR    r6, [fp, #-40]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-44]          
        LDR    r6, [fp, #-48]           @ let var39 = ? in...
        LDR    r6, [fp, #-44]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-48]          
        LDR    r6, [fp, #-52]           @ let var42 = ? in...
        LDR    r6, [fp, #-48]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-52]          
        LDR    r6, [fp, #-56]           @ let var45 = ? in...
        LDR    r6, [fp, #-52]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-56]          
        LDR    r6, [fp, #-60]           @ let var48 = ? in...
        LDR    r6, [fp, #-56]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-60]          
        LDR    r6, [fp, #-64]           @ let var51 = ? in...
        LDR    r6, [fp, #-60]          
        MOV    r7, #1                  
        ADD    r6, r6, r7              
        STR    r6, [fp, #-64]          
        LDR    r6, [fp, #-68]           @ let var54 = ? in...
        LDR    r6, [fp, #-64]          
        ADD    r6, r6, r4              
        STR    r6, [fp, #-68]          
        LDR    r6, [fp, #-72]           @ let var55 = ? in...
        LDR    r6, [fp, #-68]          
        ADD    r6, r6, r5              
        STR    r6, [fp, #-72]          
        LDR    r6, [fp, #-76]           @ let var56 = ? in...
        LDR    r6, [fp, #-72]          
        LDR    r7, [fp, #-4]           
        ADD    r6, r6, r7              
        STR    r6, [fp, #-76]          
        LDR    r6, [fp, #-80]           @ let var57 = ? in...
        LDR    r6, [fp, #-76]          
        LDR    r7, [fp, #-8]           
        ADD    r6, r6, r7              
        STR    r6, [fp, #-80]          
        LDR    r6, [fp, #-84]           @ let var58 = ? in...
        LDR    r6, [fp, #-80]          
        LDR    r7, [fp, #-12]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-84]          
        LDR    r6, [fp, #-88]           @ let var59 = ? in...
        LDR    r6, [fp, #-84]          
        LDR    r7, [fp, #-16]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-88]          
        LDR    r6, [fp, #-92]           @ let var60 = ? in...
        LDR    r6, [fp, #-88]          
        LDR    r7, [fp, #-20]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-92]          
        LDR    r6, [fp, #-96]           @ let var61 = ? in...
        LDR    r6, [fp, #-92]          
        LDR    r7, [fp, #-24]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-96]          
        LDR    r6, [fp, #-100]          @ let var62 = ? in...
        LDR    r6, [fp, #-96]          
        LDR    r7, [fp, #-28]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-100]         
        LDR    r6, [fp, #-104]          @ let var63 = ? in...
        LDR    r6, [fp, #-100]         
        LDR    r7, [fp, #-32]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-104]         
        LDR    r6, [fp, #-108]          @ let var64 = ? in...
        LDR    r6, [fp, #-104]         
        LDR    r7, [fp, #-36]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-108]         
        LDR    r6, [fp, #-112]          @ let var65 = ? in...
        LDR    r6, [fp, #-108]         
        LDR    r7, [fp, #-40]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-112]         
        LDR    r6, [fp, #-116]          @ let var66 = ? in...
        LDR    r6, [fp, #-112]         
        LDR    r7, [fp, #-44]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-116]         
        LDR    r6, [fp, #-120]          @ let var67 = ? in...
        LDR    r6, [fp, #-116]         
        LDR    r7, [fp, #-48]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-120]         
        LDR    r6, [fp, #-124]          @ let var68 = ? in...
        LDR    r6, [fp, #-120]         
        LDR    r7, [fp, #-52]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-124]         
        LDR    r6, [fp, #-128]          @ let var69 = ? in...
        LDR    r6, [fp, #-124]         
        LDR    r7, [fp, #-56]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-128]         
        LDR    r6, [fp, #-132]          @ let var70 = ? in...
        LDR    r6, [fp, #-128]         
        LDR    r7, [fp, #-60]          
        ADD    r6, r6, r7              
        STR    r6, [fp, #-132]         
        LDR    r6, [fp, #-136]          @ let var71 = ? in...
        LDR    r6, [fp, #-132]         
        LDR    r7, [fp, #40]           
        ADD    r6, r6, r7              
        STR    r6, [fp, #-136]         
        ADD    r4, r4, r5               @ let var106 = ? in...
        LDR    r6, [fp, #-4]            @ let var105 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-8]            @ let var104 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-12]           @ let var103 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-16]           @ let var102 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-20]           @ let var101 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-24]           @ let var100 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-28]           @ let var99 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-32]           @ let var98 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-36]           @ let var97 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-40]           @ let var96 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-44]           @ let var95 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-48]           @ let var94 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-52]           @ let var93 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-56]           @ let var92 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-60]           @ let var91 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-64]           @ let var90 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-68]           @ let var89 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-72]           @ let var88 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-76]           @ let var87 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-80]           @ let var86 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-84]           @ let var85 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-88]           @ let var84 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-92]           @ let var83 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-96]           @ let var82 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-100]          @ let var81 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-104]          @ let var80 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-108]          @ let var79 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-112]          @ let var78 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-116]          @ let var77 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-120]          @ let var76 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-124]          @ let var75 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-128]          @ let var74 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-132]          @ let var73 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #-136]          @ let var72 = ? in...
        ADD    r4, r4, r6              
        LDR    r6, [fp, #40]           
        ADD    r0, r4, r6              
        SUB    sp, r11, #0             
        POP    {r4-r11, lr}            
        BX     lr                       @ Return

label2: PUSH   {r4-r11, lr}             @ Function _
        ADD    r11, sp, #0             
        SUB    sp, sp, #24             
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
        MOV    r6, #0                   @ let tmp0 = ? in...
        LSL    r6, #2                  
        LDR    r7, [r4]                
        STR    r5, [r4, r6]            
        MOV    r5, r7                  
        MOV    r5, #0                   @ let var145 = ? in...
        MOV    r0, r5                   @ let var144 = ? in...
        MOV    r1, r4                  
        LDR    r6, [r4]                
        PUSH   {r0, r1}                
        PUSH   {r4}                     @ Closure info
        BLX    r6                       @ Apply closure
        MOV    r4, r0                  
        ADD    sp, sp, #4              
        POP    {r0, r1}                
        MOV    r0, r4                   @ let var142 = ? in...
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
        BL     label2                   @ Branch to main function
        MOV    r0, #0                   @ Exit syscall
        MOV    r7, #1                  
        SVC    #0                      
heap_start: .word heap_start_addr
heap_offset: .word heap_offset_addr
