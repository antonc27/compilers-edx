class Main inherits IO {
  x: Int <- 0;

  main(): Object {{
    if x <= 3 then 0 else abort() fi;
    if x < 3 then 0 else abort() fi;
  }};
};