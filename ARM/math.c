#include <math.h>

const double PI=3.1415926535897932384650288;

float _min_caml_sin(float x){
  float sign=1;
  if (x<0){
    sign=-1.0;
    x=-x;
  }
  if (x>360) x -= ((int)(x/360))*360;
  x*=PI/180.0;
  float res=0;
  float term=x;
  int k=1;
  while (res+term!=res){
    res+=term;
    k+=2;
    term*=-x*x/k/(k-1);
  }

  return sign*res;
}

float _min_caml_cos(float x){
  if (x<0) x=-x;
  if (x>360) x -= ((int)(x/360))*360;
  x*=PI/180.0;
  double res=0;
  double term=1;
  int k=0;
  while (res+term!=res){
    res+=term;
    k+=2;
    term*=-x*x/k/(k-1);
  }  
  return res;
}

float _min_caml_sqrt(float n) {
  double lo = 0, hi = n, mid;
  for(int i = 0 ; i < 1000 ; i++){
      mid = (lo+hi)/2;
      if(mid*mid == n) return mid;
      if(mid*mid > n) hi = mid;
      else lo = mid;
  }
  return mid;
}

float _min_caml_abs_float(float x) {
	if (x < 0) {
		return -x;
	}
	return x;
}

int _min_caml_int_of_float(float x) {
	return (int)x;
}

float _min_caml_float_of_int(int x) {
	return (float)x;
}

int _min_caml_truncate(float x) {
	return (int)x;
}
