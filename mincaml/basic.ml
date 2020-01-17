let x = 
   let y = 1 + 2 in y
in let rec succ x = x + 1 
in let rec double x = x + x 
in print_int (succ (double x))