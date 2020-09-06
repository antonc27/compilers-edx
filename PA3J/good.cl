class A {
};

Class BB__ inherits A {
};

class C {
  divisor : Int;
  start : Int <- 499;
  stop : Int <- 500;
  bool1 : Bool <- false;
  bool2 : Bool <- true;
  str : String <- "hello";
  bool3 : Bool <- bool2;
  bool4 : Bool <- (bool3);
  bool5 : Bool <- not bool4;
  bool6 : Bool <- start = stop;
  bool7 : Bool <- 1 <= 3;
  bool8 : Bool <- start < 3;
  int1 : Int <- ~56;
  int2 : Int <- 1 / 2;
  int3 : Int <- 1 * 2;
  int4 : Int <- 1 - 2;
  int5 : Int <- 1 + 2;
  bool9 : Bool <- isvoid int5;
  a : A <- new A;
  b10 : Bool <- case true of 
      b : Bool => true;
  esac;
  b11 : Bool <- case a of
      aa : A => true;
      bb : BB__ => false;
  esac;
  int6 : Int <- let x : Int in 3;
  int7 : Int <- let x : Int <- 4 in 5;
  int8 : Int <- let x : Int, y : Int <- 6 in x + y;
  int9 : Int <- let x : Int, y : Int, z : Int in 9;
  b12 : Bool <- { true; };
  b13 : Bool <- { true; false; };
  obj1 : Object <- while false loop true pool;
  b14 : Bool <- if true then true else false fi;
};
