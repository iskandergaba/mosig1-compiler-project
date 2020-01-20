let rec f x =
   let rec g y =
      let rec h z =
         z+y in
      h in
   let rec g2 t =
      1 in
   let a = Array.create 3 g2 in
   a.(0) <- (g 0);
   a in
let a = (f 1) in
print_int((a.(0)) 1);
print_int((a.(1)) 1)