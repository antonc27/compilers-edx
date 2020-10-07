class O {
};

class B inherits C {
};

class A inherits B {
};

class C inherits A {
};

class T inherits S {
};

class S inherits T {
};

Class Main {
     main():A {
      (new A)
  };
};