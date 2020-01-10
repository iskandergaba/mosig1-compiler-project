let rec sum x =
  if x = 1 then
  sum (x+1) else
  x+10 in 
let y=
  (sum 1) in
sum y