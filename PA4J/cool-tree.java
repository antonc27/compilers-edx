// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.*;
import java.io.PrintStream;


/**
 * Defines simple phylum Program
 */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

}


/**
 * Defines simple phylum Class_
 */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}


/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Classes" list
     */
    public Classes(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Class_" element to this list
     */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/**
 * Defines simple phylum Feature
 */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass);
}


/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Features" list
     */
    public Features(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Feature" element to this list
     */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/**
 * Defines simple phylum Formal
 */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}


/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Formals" list
     */
    public Formals(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Formal" element to this list
     */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/**
 * Defines simple phylum Expression
 */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }

    private AbstractSymbol type = null;

    public AbstractSymbol get_type() {
        return type;
    }

    public Expression set_type(AbstractSymbol s) {
        type = s;
        return this;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public abstract AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass);
}


/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Expressions" list
     */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Expression" element to this list
     */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/**
 * Defines simple phylum Case
 */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass);
}


/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;

    /**
     * Returns class of this lists's elements
     */
    public Class getElementClass() {
        return elementClass;
    }

    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /**
     * Creates an empty "Cases" list
     */
    public Cases(int lineNumber) {
        super(lineNumber);
    }

    /**
     * Appends "Case" element to this list
     */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }

    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol lub = null;
        for (Enumeration e = getElements(); e.hasMoreElements(); ) {
            Case c = (Case) e.nextElement();
            AbstractSymbol current = c.type_check(classTable, objects, methods, currentClass);
            if (lub == null) {
                lub = current;
                continue;
            }
            lub = classTable.lub(lub, current);
        }
        return  lub;
    }
}


/**
 * Defines AST constructor 'programc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class programc extends Program {
    protected Classes classes;

    /**
     * Creates "programc" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for classes
     */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new programc(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
            ((Class_) e.nextElement()).dump_with_types(out, n + 2);
        }
    }

    /**
     * This method is the entry point to the semantic checker.  You will
     * need to complete it in programming assignment 4.
     * <p>
     * Your checker should do the following two things:
     * <ol>
     * <li>Check that the program is semantically correct
     * <li>Decorate the abstract syntax tree with type information
     * by setting the type field in each Expression node.
     * (see tree.h)
     * </ol>
     * <p>
     * You are free to first do (1) and make sure you catch all semantic
     * errors. Part (2) can be done in a second stage when you want
     * to test the complete compiler.
     */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        ClassTable classTable = new ClassTable(classes);

        /* some semantic analysis code may go here */
        if (!classTable.errors()) {
            classTable.doTypeCheck();
        }

        if (classTable.errors()) {
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

}


/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;

    /**
     * Creates "class_c" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a2         initial value for parent
     * @param a3         initial value for features
     * @param a4         initial value for filename
     */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features) features.copy(), copy_AbstractSymbol(filename));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
    }


    public AbstractSymbol getFilename() {
        return filename;
    }

    public AbstractSymbol getName() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
            ((Feature) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

}


/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;

    /**
     * Creates "method" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a2         initial value for formals
     * @param a3         initial value for return_type
     * @param a4         initial value for expr
     */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals) formals.copy(), copy_AbstractSymbol(return_type), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n + 2, name);
        formals.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
            ((Formal) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        boolean hasBadReturnType = false;
        if (return_type != TreeConstants.SELF_TYPE && !classTable.typeExists(return_type)) {
            classTable.semantError(currentClass).println("Undefined return type " + return_type + " in method " + name + ".");
            // do not return immediately, wait for type check of expr
            // (better handle order of errors ?)
            hasBadReturnType = true;
        }

        objects.enterScope();
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
            formalc flc = (formalc) e.nextElement();
            if (flc.name == TreeConstants.self) {
                classTable.semantError(currentClass).println("'self' cannot be the name of a formal parameter.");
                objects.exitScope();
                return TreeConstants.Object_;
            } else if (flc.type_decl == TreeConstants.SELF_TYPE) {
                classTable.semantError(currentClass).println("Formal parameter " + flc.name + " cannot have type SELF_TYPE.");
                objects.exitScope();
                return TreeConstants.Object_;
            } else {
                objects.addId(flc.name, flc.type_decl);
            }
        }
        AbstractSymbol actualType = expr.type_check(classTable, objects, methods, currentClass);
        objects.exitScope();

        if (hasBadReturnType) {
            return TreeConstants.Object_;
        }
        if (actualType == TreeConstants.SELF_TYPE) {
            actualType = currentClass.getName();
        }
        AbstractSymbol parentType = return_type;
        if (parentType == TreeConstants.SELF_TYPE) {
            parentType = currentClass.getName();
        }
        if (!classTable.isSubtype(actualType, parentType)) {
            classTable.semantError(currentClass).println("Inferred return type " + actualType + " of method " + name + " does not conform to declared return type " + return_type + ".");
            return TreeConstants.Object_;
        }
        return return_type;
    }
}


/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;

    /**
     * Creates "attr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a2         initial value for type_decl
     * @param a3         initial value for init
     */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }

    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression) init.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        init.type_check(classTable, objects, methods, currentClass);
        return type_decl;
    }
}


/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;

    /**
     * Creates "formalc" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for type_decl
     */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}


/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;

    /**
     * Creates "branch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a2         initial value for type_decl
     * @param a3         initial value for expr
     */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }

    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }

    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        objects.enterScope();
        objects.addId(name, type_decl);
        AbstractSymbol branchType = expr.type_check(classTable, objects, methods, currentClass);
        objects.exitScope();
        return branchType;
    }
}


/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;

    /**
     * Creates "assign" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     * @param a1         initial value for expr
     */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }

    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol declaredType = (AbstractSymbol) objects.lookup(name);
        AbstractSymbol assignedType = expr.type_check(classTable, objects, methods, currentClass);
        if (!classTable.isSubtype(assignedType, declaredType)) {
            classTable.semantError(currentClass).println("Type " + assignedType + " of assigned expression does not conform to declared type " + declaredType + " of identifier " + name + ".");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        set_type(declaredType);
        return declaredType;
    }
}


/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "static_dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a2         initial value for type_name
     * @param a3         initial value for name
     * @param a4         initial value for actual
     */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }

    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol origMethodDeclaration = expr.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol methodDeclaration = origMethodDeclaration;
        if (methodDeclaration == TreeConstants.SELF_TYPE) {
            methodDeclaration = currentClass.getName();
        }
        if (!classTable.isSubtype(methodDeclaration, type_name)) {
            classTable.semantError(currentClass).println("Expression type " + origMethodDeclaration + " does not conform to declared static dispatch type " + type_name + ".");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }

        Map<AbstractSymbol, List<AbstractSymbol>> classMethods = (Map) methods.lookup(methodDeclaration);
        if (!classMethods.containsKey(name)) {
            classTable.semantError(currentClass).println("Dispatch to undefined method " + name + ".");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        List<AbstractSymbol> signature = classMethods.get(name);

        int idx = 0;
        for (Enumeration args = actual.getElements(); args.hasMoreElements(); ) {
            Expression arg = (Expression) args.nextElement();
            AbstractSymbol origActualType = arg.type_check(classTable, objects, methods, currentClass);
            AbstractSymbol actualType = origActualType;
            if (actualType == TreeConstants.SELF_TYPE) {
                actualType = type_name;
            }
            AbstractSymbol origDeclType = signature.get(idx);
            AbstractSymbol declType = origDeclType;
            if (declType == TreeConstants.SELF_TYPE) {
                declType = type_name;
            }

            if (!classTable.isSubtype(actualType, declType)) {
                classTable.semantError(currentClass).println("In call of method " + name + ", type " + origActualType + " of parameter b does not conform to declared type " + origDeclType + ".");
                set_type(TreeConstants.Object_);
                return TreeConstants.Object_;
            }
            idx++;
        }

        AbstractSymbol returnType = signature.get(signature.size() - 1);
        if (returnType == TreeConstants.SELF_TYPE) {
            returnType = methodDeclaration;
        }
        set_type(returnType);
        return returnType;
    }
}


/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a2         initial value for name
     * @param a3         initial value for actual
     */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(name), (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol origMethodDeclaration = expr.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol methodDeclaration = origMethodDeclaration;
        if (methodDeclaration == TreeConstants.SELF_TYPE) {
            methodDeclaration = currentClass.getName();
        }

        Map<AbstractSymbol, List<AbstractSymbol>> classMethods = (Map) methods.lookup(methodDeclaration);
        if (!classMethods.containsKey(name)) {
            classTable.semantError(currentClass).println("Dispatch to undefined method " + name + ".");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        List<AbstractSymbol> signature = classMethods.get(name);

        int idx = 0;
        for (Enumeration args = actual.getElements(); args.hasMoreElements(); ) {
            Expression arg = (Expression) args.nextElement();
            AbstractSymbol origActualType = arg.type_check(classTable, objects, methods, currentClass);
            AbstractSymbol actualType = origActualType;
            if (actualType == TreeConstants.SELF_TYPE) {
                actualType = currentClass.getName();
            }
            AbstractSymbol origDeclType = signature.get(idx);
            AbstractSymbol declType = origDeclType;
            if (declType == TreeConstants.SELF_TYPE) {
                declType = currentClass.getName();
            }

            if (!classTable.isSubtype(actualType, declType)) {
                classTable.semantError(currentClass).println("In call of method " + name + ", type " + origActualType + " of parameter b does not conform to declared type " + origDeclType + ".");
                set_type(TreeConstants.Object_);
                return TreeConstants.Object_;
            }
            idx++;
        }

        AbstractSymbol returnType = signature.get(signature.size() - 1);
        if (returnType == TreeConstants.SELF_TYPE) {
            returnType = origMethodDeclaration;
        }
        set_type(returnType);
        return returnType;
    }
}


/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;

    /**
     * Creates "cond" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for pred
     * @param a2         initial value for then_exp
     * @param a3         initial value for else_exp
     */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    public TreeNode copy() {
        return new cond(lineNumber, (Expression) pred.copy(), (Expression) then_exp.copy(), (Expression) else_exp.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n + 2);
        then_exp.dump(out, n + 2);
        else_exp.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        pred.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol thenType = then_exp.type_check(classTable, objects, methods, currentClass);
        if (thenType == TreeConstants.SELF_TYPE) {
            thenType = currentClass.getName();
        }
        AbstractSymbol elseType = else_exp.type_check(classTable, objects, methods, currentClass);
        if (elseType == TreeConstants.SELF_TYPE) {
            elseType = currentClass.getName();
        }
        AbstractSymbol lubType = classTable.lub(thenType, elseType);
        set_type(lubType);
        return lubType;
    }

}


/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;

    /**
     * Creates "loop" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for pred
     * @param a1         initial value for body
     */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }

    public TreeNode copy() {
        return new loop(lineNumber, (Expression) pred.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n + 2);
        body.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
        pred.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol predType = pred.type_check(classTable, objects, methods, currentClass);
        if (predType != TreeConstants.Bool) {
            classTable.semantError(currentClass).println("Loop condition does not have type Bool.");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        body.type_check(classTable, objects, methods, currentClass);
        set_type(TreeConstants.Object_);
        return TreeConstants.Object_;
    }

}


/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;

    /**
     * Creates "typcase" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for expr
     * @param a1         initial value for cases
     */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }

    public TreeNode copy() {
        return new typcase(lineNumber, (Expression) expr.copy(), (Cases) cases.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n + 2);
        cases.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements(); ) {
            ((Case) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        expr.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type = cases.type_check(classTable, objects, methods, currentClass);
        set_type(type);
        return type;
    }

}


/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
    protected Expressions body;

    /**
     * Creates "block" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for body
     */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }

    public TreeNode copy() {
        return new block(lineNumber, (Expressions) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol last = null;
        for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
            last = ((Expression) e.nextElement()).type_check(classTable, objects, methods, currentClass);
        }
        set_type(last);
        return last;
    }

}


/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;

    /**
     * Creates "let" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for identifier
     * @param a2         initial value for type_decl
     * @param a3         initial value for init
     * @param a4         initial value for body
     */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression) init.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
        body.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        objects.enterScope();
        objects.addId(identifier, type_decl);
        AbstractSymbol bodyType = body.type_check(classTable, objects, methods, currentClass);
        objects.exitScope();
        if (!(init instanceof no_expr)) {
            AbstractSymbol initType = init.type_check(classTable, objects, methods, currentClass);
            if (!classTable.isSubtype(initType, type_decl)) {
                classTable.semantError(currentClass).println("Inferred type " + initType + " of initialization of " + identifier + " does not conform to identifier's declared type " + type_decl + ".");
                set_type(TreeConstants.Object_);
                return TreeConstants.Object_;
            }
        }
        set_type(bodyType);
        return bodyType;
    }

}


/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "plus" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new plus(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type1 == TreeConstants.Int && type2 == TreeConstants.Int) {
            set_type(TreeConstants.Int);
            return TreeConstants.Int;
        }
        classTable.semantError(currentClass).println("non-Int arguments: " + type1 + " + " + type2 + "");
        set_type(TreeConstants.Object_);
        return TreeConstants.Object_;
    }

}


/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "sub" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new sub(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type1 == TreeConstants.Int && type2 == TreeConstants.Int) {
            set_type(TreeConstants.Int);
            return TreeConstants.Int;
        }
        classTable.semantError(currentClass).println("non-Int arguments: " + type1 + " - " + type2 + "");
        set_type(TreeConstants.Object_);
        return TreeConstants.Object_;
    }

}


/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "mul" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new mul(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type1 == TreeConstants.Int && type2 == TreeConstants.Int) {
            set_type(TreeConstants.Int);
            return TreeConstants.Int;
        }
        classTable.semantError(currentClass).println("non-Int arguments: " + type1 + " * " + type2 + "");
        set_type(TreeConstants.Object_);
        return TreeConstants.Object_;
    }

}


/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "divide" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new divide(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type1 == TreeConstants.Int && type2 == TreeConstants.Int) {
            set_type(TreeConstants.Int);
            return TreeConstants.Int;
        }
        classTable.semantError(currentClass).println("non-Int arguments: " + type1 + " / " + type2 + "");
        set_type(TreeConstants.Object_);
        return TreeConstants.Object_;
    }

}


/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
    protected Expression e1;

    /**
     * Creates "neg" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new neg(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type = e1.type_check(classTable, objects, methods, currentClass);
        if (type != TreeConstants.Int) {
            classTable.semantError(currentClass).println("Argument of '~' has type " + type + " instead of Int.");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        set_type(TreeConstants.Int);
        return TreeConstants.Int;
    }

}


/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "lt" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new lt(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        if (type1 != TreeConstants.Int) {
            classTable.semantError(currentClass).println("Left operand of '<' has type " + type1 + " instead of Int.");
        }
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type2 != TreeConstants.Int) {
            classTable.semantError(currentClass).println("Right operand of '<' has type " + type2 + " instead of Int.");
        }
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "eq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new eq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        Set<AbstractSymbol> comparable = new HashSet<AbstractSymbol>(Arrays.asList(
                TreeConstants.Int, TreeConstants.Bool, TreeConstants.Str
        ));
        if (comparable.contains(type1) || comparable.contains(type2)) {
            if (type1 != type2) {
                classTable.semantError(currentClass).println("Incomparable types: " + type1 + " and " +  type2);
            }
        }
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "leq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     * @param a1         initial value for e2
     */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new leq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type1 = e1.type_check(classTable, objects, methods, currentClass);
        if (type1 != TreeConstants.Int) {
            classTable.semantError(currentClass).println("Left operand of '<=' has type " + type1 + " instead of Int.");
        }
        AbstractSymbol type2 = e2.type_check(classTable, objects, methods, currentClass);
        if (type2 != TreeConstants.Int) {
            classTable.semantError(currentClass).println("Right operand of '<=' has type " + type2 + " instead of Int.");
        }
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
    protected Expression e1;

    /**
     * Creates "comp" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new comp(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol type = e1.type_check(classTable, objects, methods, currentClass);
        if (type != TreeConstants.Bool) {
            classTable.semantError(currentClass).println("'Not' applied to non-Bool type: " + type);
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "int_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for token
     */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_AbstractSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        set_type(TreeConstants.Int);
        return TreeConstants.Int;
    }

}


/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
    protected Boolean val;

    /**
     * Creates "bool_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for val
     */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }

    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n + 2, val);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
        dump_Boolean(out, n + 2, val);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "string_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for token
     */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, token.getString());
        out.println("\"");
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        set_type(TreeConstants.Str);
        return TreeConstants.Str;
    }

}


/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
    protected AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for type_name
     */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }

    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n + 2, type_name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        if (!classTable.typeExists(type_name)) {
            classTable.semantError(currentClass).println("'new' used with undefined class " + type_name + ".");
            return TreeConstants.Object_;
        }
        set_type(type_name);
        return type_name;
    }

}


/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
    protected Expression e1;

    /**
     * Creates "isvoid" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for e1
     */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n + 2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        e1.type_check(classTable, objects, methods, currentClass);
        set_type(TreeConstants.Bool);
        return TreeConstants.Bool;
    }

}


/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {
    /**
     * Creates "no_expr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }

    public TreeNode copy() {
        return new no_expr(lineNumber);
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        return null;
    }

}


/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
    protected AbstractSymbol name;

    /**
     * Creates "object" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a1         initial value for name
     */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }

    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n + 2, name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    @Override
    public AbstractSymbol type_check(ClassTable classTable, SymbolTable objects, SymbolTable methods, class_c currentClass) {
        AbstractSymbol objectType = (AbstractSymbol) objects.lookup(name);
        if (objectType == null) {
            classTable.semantError(currentClass).println("Undeclared identifier " + name + ".");
            set_type(TreeConstants.Object_);
            return TreeConstants.Object_;
        }
        set_type(objectType);
        return objectType;
    }

}


