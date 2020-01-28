let rec ack x y =
  print_int x;
  print_newline (); 
  print_int y;
  print_newline ();
  print_newline ();
  if x <= 0 then y + 1 else
  if y <= 0 then ack (x - 1) 1 else
  ack (x - 1) (ack x (y - 1)) in
print_int (ack 2 3)