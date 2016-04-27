package com.redhat.ceylon.langtools.source.tree;

public interface SuperTree extends Tree {
    ExpressionTree getSelected();
    ExpressionTree getExpr();
}
