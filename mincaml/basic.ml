let rec compose f g =
  let rec composed x = g+f in
  composed in
let h = compose 1 1 in
print_int(h 1)