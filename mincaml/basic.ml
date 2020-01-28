let rec f x =
    let rec g y = 1 in
    g in
let h = f 1 in
print_int (h 1)