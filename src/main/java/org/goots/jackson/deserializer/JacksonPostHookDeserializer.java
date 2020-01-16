/*
 * Copyright (C) 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.goots.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.StreamSupport;

/**
 * Class implementing the functionality of the {@link JsonPostDeserialize} annotation.
 */
public class JacksonPostHookDeserializer extends DelegatingDeserializer
{
    private static final Logger logger = LoggerFactory.getLogger( JacksonPostHookDeserializer.class );

    private final BeanDescription beanDescription;

    /**
     * Factory method to return a Module with this deserializer suitable for insertion into an ObjectMapper
     * @return a SimpleModule.
     */
    public static Module getSimpleModule()
    {
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier( new BeanDeserializerModifier()
        {
            @Override
            public JsonDeserializer<?> modifyDeserializer( DeserializationConfig config,
                                                           BeanDescription beanDescription,
                                                           JsonDeserializer<?> originalDeserializer )
            {
                if ( StreamSupport.stream( beanDescription.getClassInfo().memberMethods().spliterator(), true).
                                anyMatch( m -> m.hasAnnotation( JsonPostDeserialize.class ) ) )
                {
                    logger.debug( "BeanDescription {} ", beanDescription.getClassInfo());

                    return new JacksonPostHookDeserializer( originalDeserializer, beanDescription );
                }
                else
                {
                    return originalDeserializer;
                }
            }
        } );
        return module;
    }

    private JacksonPostHookDeserializer( JsonDeserializer<?> delegate, BeanDescription beanDescription )
    {
        super( delegate );
        this.beanDescription = beanDescription;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance( JsonDeserializer<?> newDelegatee )
    {
        return new JacksonPostHookDeserializer( newDelegatee, beanDescription );
    }

    @Override
    public Object deserialize( JsonParser p, DeserializationContext ctxt ) throws IOException
    {
        Object deserializedObject = super.deserialize( p, ctxt );

        StreamSupport.stream( beanDescription.getClassInfo().memberMethods().spliterator(), true ).forEach( m -> {
            if ( m.hasAnnotation( JsonPostDeserialize.class ) )
            {
                logger.debug( "Invoking method {} ", m.getName() );
                try
                {
                    m.fixAccess( true );
                    m.callOn( deserializedObject );
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( "Failed to call @JsonPostDeserialize annotated method in class " +
                                                                beanDescription.getClassInfo().getName(), e );
                }
            }
        } );
        return deserializedObject;
    }
}
