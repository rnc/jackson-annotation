package org.goots.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
                logger.info( "BeanDescription : {} ", beanDescription.getClassInfo() );
                logger.info( "BeanClass : {} ", beanDescription.getBeanClass() );
//                beanDescription.getClassInfo().memberMethods().forEach( m -> m.h );
                return new JacksonPostHookDeserializer( originalDeserializer, beanDescription );
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

//        if ( deserializedObject != null )
//        {
            callPostDeserializeMethods( deserializedObject );
//        }

        return deserializedObject;
    }

    private void callPostDeserializeMethods( Object deserializedObject )
    {
        for ( AnnotatedMethod method : beanDescription.getClassInfo().memberMethods() )
        {
            if ( method.hasAnnotation( JsonPostDeserialize.class ) )
            {
                try
                {
                    method.callOn( deserializedObject );
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( "Failed to call @JsonPostDeserialize annotated method in class "
                                                                + beanDescription.getClassInfo().getName(), e );
                }
            }
        }
    }
}
