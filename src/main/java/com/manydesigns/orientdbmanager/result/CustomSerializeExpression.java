package com.manydesigns.orientdbmanager.result;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Author: Emanuele Collura
 * Date: 23/04/22
 * Time: 12:49
 */
public class CustomSerializeExpression extends StdSerializer<ConditionalExpression> {


    protected CustomSerializeExpression() {
        super((Class<ConditionalExpression>) null);
    }

    protected CustomSerializeExpression(Class<ConditionalExpression> t) {
        super(t);
    }

    @Override
    public void serialize(ConditionalExpression expression, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(expression.toString());
    }
}
