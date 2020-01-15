let rec f x =
    let rec g y =
        g x+y in
    g 1 in
print_int (f 1)