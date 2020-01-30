	.cpu arm7tdmi
	.eabi_attribute 20, 1
	.eabi_attribute 21, 1
	.eabi_attribute 23, 3
	.eabi_attribute 24, 1
	.eabi_attribute 25, 1
	.eabi_attribute 26, 1
	.eabi_attribute 30, 6
	.eabi_attribute 34, 0
	.eabi_attribute 18, 4
	.file	"math.c"
	.global	PI
	.section	.rodata
	.align	3
	.type	PI, %object
	.size	PI, 8
PI:
	.word	1413754136
	.word	1074340347
	.global	__aeabi_fcmplt
	.global	__aeabi_fcmpgt
	.global	__aeabi_fdiv
	.global	__aeabi_f2iz
	.global	__aeabi_i2f
	.global	__aeabi_fsub
	.global	__aeabi_f2d
	.global	__aeabi_ddiv
	.global	__aeabi_dmul
	.global	__aeabi_d2f
	.global	__aeabi_fadd
	.global	__aeabi_fmul
	.global	__aeabi_fcmpeq
	.text
	.align	2
	.global	_min_caml_sin
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_sin, %function
_min_caml_sin:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 24
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{r4, r5, fp, lr}
	add	fp, sp, #12
	sub	sp, sp, #24
	str	r0, [fp, #-32]	@ float
	mov	r3, #1065353216
	str	r3, [fp, #-16]	@ float
	mov	r1, #0
	ldr	r0, [fp, #-32]	@ float
	bl	__aeabi_fcmplt
	mov	r3, r0
	cmp	r3, #0
	beq	.L2
	ldr	r3, .L11+8
	str	r3, [fp, #-16]	@ float
	ldr	r3, [fp, #-32]
	eor	r3, r3, #-2147483648
	str	r3, [fp, #-32]	@ float
.L2:
	ldr	r1, .L11+12
	ldr	r0, [fp, #-32]	@ float
	bl	__aeabi_fcmpgt
	mov	r3, r0
	cmp	r3, #0
	beq	.L4
	ldr	r1, .L11+12
	ldr	r0, [fp, #-32]	@ float
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r0, r3
	bl	__aeabi_f2iz
	mov	r2, r0
	mov	r3, r2
	lsl	r3, r3, #1
	add	r3, r3, r2
	lsl	r2, r3, #4
	sub	r2, r2, r3
	lsl	r3, r2, #3
	mov	r2, r3
	mov	r3, r2
	mov	r0, r3
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	ldr	r0, [fp, #-32]	@ float
	bl	__aeabi_fsub
	mov	r3, r0
	str	r3, [fp, #-32]	@ float
.L4:
	ldr	r0, [fp, #-32]	@ float
	bl	__aeabi_f2d
	mov	r4, r0
	mov	r5, r1
	adr	r1, .L11
	ldmia	r1, {r0-r1}
	mov	r2, #0
	ldr	r3, .L11+16
	bl	__aeabi_ddiv
	mov	r2, r0
	mov	r3, r1
	mov	r0, r4
	mov	r1, r5
	bl	__aeabi_dmul
	mov	r3, r0
	mov	r4, r1
	mov	r0, r3
	mov	r1, r4
	bl	__aeabi_d2f
	mov	r3, r0
	str	r3, [fp, #-32]	@ float
	mov	r3, #0
	str	r3, [fp, #-20]	@ float
	ldr	r3, [fp, #-32]	@ float
	str	r3, [fp, #-24]	@ float
	mov	r3, #1
	str	r3, [fp, #-28]
	b	.L6
.L7:
	ldr	r1, [fp, #-24]	@ float
	ldr	r0, [fp, #-20]	@ float
	bl	__aeabi_fadd
	mov	r3, r0
	str	r3, [fp, #-20]	@ float
	ldr	r3, [fp, #-28]
	add	r3, r3, #2
	str	r3, [fp, #-28]
	ldr	r3, [fp, #-32]
	eor	r3, r3, #-2147483648
	ldr	r1, [fp, #-32]	@ float
	mov	r0, r3
	bl	__aeabi_fmul
	mov	r3, r0
	mov	r4, r3
	ldr	r0, [fp, #-28]
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	mov	r0, r4
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r4, r3
	ldr	r3, [fp, #-28]
	sub	r3, r3, #1
	mov	r0, r3
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	mov	r0, r4
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r1, r3
	ldr	r0, [fp, #-24]	@ float
	bl	__aeabi_fmul
	mov	r3, r0
	str	r3, [fp, #-24]	@ float
.L6:
	ldr	r1, [fp, #-24]	@ float
	ldr	r0, [fp, #-20]	@ float
	bl	__aeabi_fadd
	mov	r3, r0
	ldr	r1, [fp, #-20]	@ float
	mov	r0, r3
	bl	__aeabi_fcmpeq
	mov	r3, r0
	cmp	r3, #0
	beq	.L7
	ldr	r1, [fp, #-20]	@ float
	ldr	r0, [fp, #-16]	@ float
	bl	__aeabi_fmul
	mov	r3, r0
	mov	r0, r3
	sub	sp, fp, #12
	@ sp needed
	pop	{r4, r5, fp, lr}
	bx	lr
.L12:
	.align	3
.L11:
	.word	1413754136
	.word	1074340347
	.word	-1082130432
	.word	1135869952
	.word	1080459264
	.size	_min_caml_sin, .-_min_caml_sin
	.global	__aeabi_dadd
	.global	__aeabi_dcmpeq
	.align	2
	.global	_min_caml_cos
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_cos, %function
_min_caml_cos:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 32
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{r4, r5, fp, lr}
	add	fp, sp, #12
	sub	sp, sp, #32
	str	r0, [fp, #-40]	@ float
	mov	r1, #0
	ldr	r0, [fp, #-40]	@ float
	bl	__aeabi_fcmplt
	mov	r3, r0
	cmp	r3, #0
	beq	.L14
	ldr	r3, [fp, #-40]
	eor	r3, r3, #-2147483648
	str	r3, [fp, #-40]	@ float
.L14:
	ldr	r1, .L23+8
	ldr	r0, [fp, #-40]	@ float
	bl	__aeabi_fcmpgt
	mov	r3, r0
	cmp	r3, #0
	beq	.L16
	ldr	r1, .L23+8
	ldr	r0, [fp, #-40]	@ float
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r0, r3
	bl	__aeabi_f2iz
	mov	r2, r0
	mov	r3, r2
	lsl	r3, r3, #1
	add	r3, r3, r2
	lsl	r2, r3, #4
	sub	r2, r2, r3
	lsl	r3, r2, #3
	mov	r2, r3
	mov	r3, r2
	mov	r0, r3
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	ldr	r0, [fp, #-40]	@ float
	bl	__aeabi_fsub
	mov	r3, r0
	str	r3, [fp, #-40]	@ float
.L16:
	ldr	r0, [fp, #-40]	@ float
	bl	__aeabi_f2d
	mov	r4, r0
	mov	r5, r1
	adr	r1, .L23
	ldmia	r1, {r0-r1}
	mov	r2, #0
	ldr	r3, .L23+12
	bl	__aeabi_ddiv
	mov	r2, r0
	mov	r3, r1
	mov	r0, r4
	mov	r1, r5
	bl	__aeabi_dmul
	mov	r3, r0
	mov	r4, r1
	mov	r0, r3
	mov	r1, r4
	bl	__aeabi_d2f
	mov	r3, r0
	str	r3, [fp, #-40]	@ float
	mov	r3, #0
	mov	r4, #0
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
	mov	r3, #0
	ldr	r4, .L23+16
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
	mov	r3, #0
	str	r3, [fp, #-32]
	b	.L18
.L19:
	sub	r3, fp, #28
	ldmia	r3, {r2-r3}
	sub	r1, fp, #20
	ldmia	r1, {r0-r1}
	bl	__aeabi_dadd
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
	ldr	r3, [fp, #-32]
	add	r3, r3, #2
	str	r3, [fp, #-32]
	ldr	r3, [fp, #-40]
	eor	r3, r3, #-2147483648
	ldr	r1, [fp, #-40]	@ float
	mov	r0, r3
	bl	__aeabi_fmul
	mov	r3, r0
	mov	r4, r3
	ldr	r0, [fp, #-32]
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	mov	r0, r4
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r4, r3
	ldr	r3, [fp, #-32]
	sub	r3, r3, #1
	mov	r0, r3
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r1, r3
	mov	r0, r4
	bl	__aeabi_fdiv
	mov	r3, r0
	mov	r0, r3
	bl	__aeabi_f2d
	mov	r3, r0
	mov	r4, r1
	mov	r2, r3
	mov	r3, r4
	sub	r1, fp, #28
	ldmia	r1, {r0-r1}
	bl	__aeabi_dmul
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
.L18:
	sub	r3, fp, #28
	ldmia	r3, {r2-r3}
	sub	r1, fp, #20
	ldmia	r1, {r0-r1}
	bl	__aeabi_dadd
	mov	r3, r0
	mov	r4, r1
	mov	r0, r3
	mov	r1, r4
	sub	r3, fp, #20
	ldmia	r3, {r2-r3}
	bl	__aeabi_dcmpeq
	mov	r3, r0
	cmp	r3, #0
	beq	.L19
	sub	r1, fp, #20
	ldmia	r1, {r0-r1}
	bl	__aeabi_d2f
	mov	r3, r0
	mov	r0, r3
	sub	sp, fp, #12
	@ sp needed
	pop	{r4, r5, fp, lr}
	bx	lr
.L24:
	.align	3
.L23:
	.word	1413754136
	.word	1074340347
	.word	1135869952
	.word	1080459264
	.word	1072693248
	.size	_min_caml_cos, .-_min_caml_cos
	.global	__aeabi_dcmpgt
	.align	2
	.global	_min_caml_sqrt
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_sqrt, %function
_min_caml_sqrt:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 40
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{r4, r5, fp, lr}
	add	fp, sp, #12
	sub	sp, sp, #40
	str	r0, [fp, #-48]	@ float
	mov	r3, #0
	mov	r4, #0
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
	ldr	r0, [fp, #-48]	@ float
	bl	__aeabi_f2d
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
	mov	r3, #0
	str	r3, [fp, #-40]
	b	.L26
.L33:
	sub	r3, fp, #28
	ldmia	r3, {r2-r3}
	sub	r1, fp, #20
	ldmia	r1, {r0-r1}
	bl	__aeabi_dadd
	mov	r3, r0
	mov	r4, r1
	mov	r0, r3
	mov	r1, r4
	mov	r2, #0
	mov	r3, #1073741824
	bl	__aeabi_ddiv
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-36]
	str	r4, [fp, #-32]
	sub	r3, fp, #36
	ldmia	r3, {r2-r3}
	sub	r1, fp, #36
	ldmia	r1, {r0-r1}
	bl	__aeabi_dmul
	mov	r3, r0
	mov	r4, r1
	mov	r5, r4
	mov	r4, r3
	ldr	r0, [fp, #-48]	@ float
	bl	__aeabi_f2d
	mov	r2, r0
	mov	r3, r1
	mov	r0, r4
	mov	r1, r5
	bl	__aeabi_dcmpeq
	mov	r3, r0
	cmp	r3, #0
	beq	.L36
	sub	r1, fp, #36
	ldmia	r1, {r0-r1}
	bl	__aeabi_d2f
	mov	r3, r0
	b	.L29
.L36:
	sub	r3, fp, #36
	ldmia	r3, {r2-r3}
	sub	r1, fp, #36
	ldmia	r1, {r0-r1}
	bl	__aeabi_dmul
	mov	r3, r0
	mov	r4, r1
	mov	r5, r4
	mov	r4, r3
	ldr	r0, [fp, #-48]	@ float
	bl	__aeabi_f2d
	mov	r2, r0
	mov	r3, r1
	mov	r0, r4
	mov	r1, r5
	bl	__aeabi_dcmpgt
	mov	r3, r0
	cmp	r3, #0
	beq	.L37
	sub	r4, fp, #36
	ldmia	r4, {r3-r4}
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
	b	.L32
.L37:
	sub	r4, fp, #36
	ldmia	r4, {r3-r4}
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
.L32:
	ldr	r3, [fp, #-40]
	add	r3, r3, #1
	str	r3, [fp, #-40]
.L26:
	ldr	r3, [fp, #-40]
	cmp	r3, #1000
	blt	.L33
	sub	r1, fp, #36
	ldmia	r1, {r0-r1}
	bl	__aeabi_d2f
	mov	r3, r0
.L29:
	mov	r0, r3
	sub	sp, fp, #12
	@ sp needed
	pop	{r4, r5, fp, lr}
	bx	lr
	.size	_min_caml_sqrt, .-_min_caml_sqrt
	.align	2
	.global	_min_caml_abs_float
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_abs_float, %function
_min_caml_abs_float:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 8
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #8
	str	r0, [fp, #-8]	@ float
	mov	r1, #0
	ldr	r0, [fp, #-8]	@ float
	bl	__aeabi_fcmplt
	mov	r3, r0
	cmp	r3, #0
	beq	.L43
	ldr	r3, [fp, #-8]
	eor	r3, r3, #-2147483648
	b	.L41
.L43:
	ldr	r3, [fp, #-8]	@ float
.L41:
	mov	r0, r3
	sub	sp, fp, #4
	@ sp needed
	pop	{fp, lr}
	bx	lr
	.size	_min_caml_abs_float, .-_min_caml_abs_float
	.align	2
	.global	_min_caml_int_of_float
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_int_of_float, %function
_min_caml_int_of_float:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 8
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #8
	str	r0, [fp, #-8]	@ float
	ldr	r0, [fp, #-8]	@ float
	bl	__aeabi_f2iz
	mov	r3, r0
	mov	r0, r3
	sub	sp, fp, #4
	@ sp needed
	pop	{fp, lr}
	bx	lr
	.size	_min_caml_int_of_float, .-_min_caml_int_of_float
	.align	2
	.global	_min_caml_float_of_int
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_float_of_int, %function
_min_caml_float_of_int:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 8
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #8
	str	r0, [fp, #-8]
	ldr	r0, [fp, #-8]
	bl	__aeabi_i2f
	mov	r3, r0
	mov	r0, r3
	sub	sp, fp, #4
	@ sp needed
	pop	{fp, lr}
	bx	lr
	.size	_min_caml_float_of_int, .-_min_caml_float_of_int
	.align	2
	.global	_min_caml_truncate
	.syntax unified
	.arm
	.fpu softvfp
	.type	_min_caml_truncate, %function
_min_caml_truncate:
	@ Function supports interworking.
	@ args = 0, pretend = 0, frame = 8
	@ frame_needed = 1, uses_anonymous_args = 0
	push	{fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #8
	str	r0, [fp, #-8]	@ float
	ldr	r0, [fp, #-8]	@ float
	bl	__aeabi_f2iz
	mov	r3, r0
	mov	r0, r3
	sub	sp, fp, #4
	@ sp needed
	pop	{fp, lr}
	bx	lr
	.size	_min_caml_truncate, .-_min_caml_truncate
	.ident	"GCC: (15:6.3.1+svn253039-1build1) 6.3.1 20170620"
