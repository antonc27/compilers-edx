class Main inherits IO {
  x: Int <- 0;

  main(): Object {
    while x < 3 loop {
      out_int(x);
      out_string("\n");
      x <- x + 1;      
    } pool
  };
};