let rec f x y z = (x,y,z) in
let (x,y,z)=(f 1 2 3) in
print_int x