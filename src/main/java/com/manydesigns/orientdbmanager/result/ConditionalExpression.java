package com.manydesigns.orientdbmanager.result;

import com.github.javaparser.ast.expr.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Emanuele Collura
 * Date: 23/04/22
 * Time: 12:55
 */
@Getter @Setter @AllArgsConstructor
public class ConditionalExpression {

    private Expression condition;
    private boolean denied;

    public ConditionalExpression(Expression condition) {
        this(condition, false);
    }

    @Override
    public String toString() {
        var strCondition = condition.toString();
        if (denied)
            strCondition = "!(" + strCondition + ")";

        return strCondition;
    }
}
