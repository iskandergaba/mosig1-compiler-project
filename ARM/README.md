# How to debug ?

## Launch the program in qemu
`qemu-arm -singlestep -g 1234 <file.arm>`

## Debug with gdb

Use gdb-multiarch or arm-none-eabi-gdb (whichever is available on your machine).
`gdb-multiarch <file.arm>`
`arm-none-eabi-gdb <file.arm>`

Inside, use the commands:
- `target remote:1234`
- `layout regs`

After that, to step through an assembly instruction, use `si`.
