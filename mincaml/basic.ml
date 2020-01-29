let rec f x = 
    let y = x + 1 in
    let z = y + 1 in
    z in
let rec g x =
    let y = x + 1 in
    let z = y + 1 in
    f y in
print_int (g 1)