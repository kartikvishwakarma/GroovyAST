package org.devoxx.ast;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.AbstractASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class MessageAdderAstTransformation extends AbstractASTTransformation {
    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotationNode messageAnnotation = (AnnotationNode) nodes[0];
        ConstantExpression shout = (ConstantExpression) messageAnnotation.getMember("shout");
        ClassNode annotatedClass = (ClassNode) nodes[1];

        VariableExpression message = new VariableExpression("message");

        MethodCallExpression toUpperCaseCall = new MethodCallExpression(message, "toUpperCase",
                ArgumentListExpression.EMPTY_ARGUMENTS);

        ExpressionStatement code = new ExpressionStatement(
                new MethodCallExpression(new VariableExpression("this"), "println",
                        new TernaryExpression(new BooleanExpression(shout), toUpperCaseCall, message))
        );

        annotatedClass.addMethod("message",
                ACC_PUBLIC, ClassHelper.VOID_TYPE,
                new Parameter[] { new Parameter(ClassHelper.STRING_TYPE, "message")},
                ClassNode.EMPTY_ARRAY,
                code);
    }
}
