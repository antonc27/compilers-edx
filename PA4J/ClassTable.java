import java.io.PrintStream;
import java.util.*;

/**
 * This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.
 */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;

    private Classes classes;

    private class_c objectClass;
    private SymbolTable basicClasses;

    private SymbolTable classDeclarations;

    /**
     * Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     */
    private void installBasicClasses() {
        AbstractSymbol filename
                = AbstractTable.stringtable.addString("<basic class>");

        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes.  There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables.  You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        //        cool_abort() : Object    aborts the program
        //        type_name() : Str        returns a string representation
        //                                 of class name
        //        copy() : SELF_TYPE       returns a copy of the object

        class_c Object_class =
                new class_c(0,
                        TreeConstants.Object_,
                        TreeConstants.No_class,
                        new Features(0)
                                .appendElement(new method(0,
                                        TreeConstants.cool_abort,
                                        new Formals(0),
                                        TreeConstants.Object_,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.type_name,
                                        new Formals(0),
                                        TreeConstants.Str,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.copy,
                                        new Formals(0),
                                        TreeConstants.SELF_TYPE,
                                        new no_expr(0))),
                        filename);
        objectClass = Object_class;

        // The IO class inherits from Object. Its methods are
        //        out_string(Str) : SELF_TYPE  writes a string to the output
        //        out_int(Int) : SELF_TYPE      "    an int    "  "     "
        //        in_string() : Str            reads a string from the input
        //        in_int() : Int                "   an int     "  "     "

        class_c IO_class =
                new class_c(0,
                        TreeConstants.IO,
                        TreeConstants.Object_,
                        new Features(0)
                                .appendElement(new method(0,
                                        TreeConstants.out_string,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.SELF_TYPE,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.out_int,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Int)),
                                        TreeConstants.SELF_TYPE,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.in_string,
                                        new Formals(0),
                                        TreeConstants.Str,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.in_int,
                                        new Formals(0),
                                        TreeConstants.Int,
                                        new no_expr(0))),
                        filename);

        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_c Int_class =
                new class_c(0,
                        TreeConstants.Int,
                        TreeConstants.Object_,
                        new Features(0)
                                .appendElement(new attr(0,
                                        TreeConstants.val,
                                        TreeConstants.prim_slot,
                                        new no_expr(0))),
                        filename);

        // Bool also has only the "val" slot.
        class_c Bool_class =
                new class_c(0,
                        TreeConstants.Bool,
                        TreeConstants.Object_,
                        new Features(0)
                                .appendElement(new attr(0,
                                        TreeConstants.val,
                                        TreeConstants.prim_slot,
                                        new no_expr(0))),
                        filename);

        // The class Str has a number of slots and operations:
        //       val                              the length of the string
        //       str_field                        the string itself
        //       length() : Int                   returns length of the string
        //       concat(arg: Str) : Str           performs string concatenation
        //       substr(arg: Int, arg2: Int): Str substring selection

        class_c Str_class =
                new class_c(0,
                        TreeConstants.Str,
                        TreeConstants.Object_,
                        new Features(0)
                                .appendElement(new attr(0,
                                        TreeConstants.val,
                                        TreeConstants.Int,
                                        new no_expr(0)))
                                .appendElement(new attr(0,
                                        TreeConstants.str_field,
                                        TreeConstants.prim_slot,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.length,
                                        new Formals(0),
                                        TreeConstants.Int,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.concat,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.Str,
                                        new no_expr(0)))
                                .appendElement(new method(0,
                                        TreeConstants.substr,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Int))
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg2,
                                                        TreeConstants.Int)),
                                        TreeConstants.Str,
                                        new no_expr(0))),
                        filename);

	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */

        basicClasses = new SymbolTable();
        basicClasses.enterScope();

        basicClasses.addId(Object_class.getName(), Object_class);
        basicClasses.addId(IO_class.getName(), IO_class);
        basicClasses.addId(Int_class.getName(), Int_class);
        basicClasses.addId(Bool_class.getName(), Bool_class);
        basicClasses.addId(Str_class.getName(), Str_class);
    }


    public ClassTable(Classes cls) {
        semantErrors = 0;
        errorStream = System.err;

        classes = cls;

        installBasicClasses();

        classDeclarations = buildAndCheckClassDeclarations();
        checkParentDeclarations();

        checkInheritanceTree(buildInheritanceTree());
    }

    public void doTypeCheck() {
        SymbolTable objects = new SymbolTable();
        SymbolTable methods = new SymbolTable();

        for (Enumeration ce = classes.getElements(); ce.hasMoreElements(); ) {
            class_c c = (class_c) ce.nextElement();
            for (Enumeration fe = c.features.getElements(); fe.hasMoreElements(); ) {
                Feature f = (Feature) fe.nextElement();
                f.type_check(this, objects, methods, c);
            }
        }
    }

    public boolean typeExists(AbstractSymbol typeSymbol) {
        return getClassDeclaration(typeSymbol) != null;
    }

    public boolean isSubtype(AbstractSymbol subtypeSymbol, AbstractSymbol typeSymbol) {
        class_c type = getClassDeclaration(typeSymbol);
        assert type != null;
        class_c subtype = getClassDeclaration(subtypeSymbol);
        assert subtype != null;
        while (subtype.getName() != TreeConstants.Object_) {
            if (subtype.getName() == type.getName()) {
                break;
            }

            subtype = getClassDeclaration(subtype.getParent());
        }
        return subtype.getName() == type.getName();
    }

    private class_c getClassDeclaration(AbstractSymbol className) {
        class_c classDeclaration = (class_c) basicClasses.probe(className);
        if (classDeclaration == null) {
            classDeclaration = (class_c) classDeclarations.probe(className);
        }
        return classDeclaration;
    }

    private SymbolTable buildAndCheckClassDeclarations() {
        SymbolTable declarations = new SymbolTable();
        declarations.enterScope();
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            Object n = e.nextElement();
            assert n instanceof class_c;
            class_c c = (class_c)n;
            if (declarations.probe(c.getName()) != null) {
                semantError(c).println("Class " + c.getName() + " was previously defined.");
            }
            if (basicClasses.probe(c.getName()) != null) {
                semantError(c).println("Redefinition of basic class " + c.getName() + ".");
            }
            declarations.addId(c.getName(), c);
        }
        if (declarations.probe(TreeConstants.Main) == null) {
            semantError().println("Class Main is not defined.");
        }
        return declarations;
    }

    private void checkParentDeclarations() {
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c c = (class_c) e.nextElement();
            AbstractSymbol child = c.getName();
            AbstractSymbol parent = c.getParent();
            if (!parent.equals(objectClass.getName()) && basicClasses.probe(parent) != null) {
                semantError(c).println("Class " + child + " cannot inherit class " + parent + ".");
            }
            if (classDeclarations.probe(parent) == null && basicClasses.probe(parent) == null) {
                semantError(c).println("Class " + child + " inherits from an undefined class " + parent + ".");
            }
        }
    }

    private Map<class_c, List<class_c>> buildInheritanceTree() {
        Map<class_c, List<class_c>> res = new HashMap<class_c, List<class_c>>();
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c c = (class_c) e.nextElement();
            class_c parent = (class_c) classDeclarations.probe(c.getParent());
            if (parent == null) {
                parent = (class_c) basicClasses.probe(c.getParent());
            }
            if (!res.containsKey(parent)) {
                res.put(parent, new LinkedList<class_c>());
            }
            res.get(parent).add(c);
        }
        return res;
    }

    private void checkInheritanceTree(Map<class_c, List<class_c>> tree) {
        Set<class_c> erronous = new HashSet<class_c>();
        for (class_c cls : tree.keySet()) {
            Set<class_c> seen = new HashSet<class_c>();
            seen.add(cls);
            checkCycle(tree, cls, seen, erronous);
        }

        List<class_c> reversed = new LinkedList<class_c>();
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            class_c c = (class_c) e.nextElement();
            reversed.add(0, c);
        }

        for (class_c c : reversed) {
            if (erronous.contains(c)) {
                semantError(c).println("Class " + c.getName() + ", or an ancestor of " + c.getName() + ", is involved in an inheritance cycle.");
            }
        }
    }

    private void checkCycle(Map<class_c, List<class_c>> tree, class_c next, Set<class_c> seen, Set<class_c> erronous) {
        if (!tree.containsKey(next)) return;
        for (class_c cls : tree.get(next)) {
            if (seen.contains(cls)) {
                if (erronous.contains(cls)) return;
                erronous.addAll(seen);
                return;
            }
            seen.add(cls);
            checkCycle(tree, cls, seen, erronous);
            seen.remove(cls);
        }
    }

    /**
     * Prints line number and file name of the given class.
     * <p>
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     * <p>
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t        the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /**
     * Returns true if there are any static semantic errors.
     */
    public boolean errors() {
        return semantErrors != 0;
    }
}
			  
    
