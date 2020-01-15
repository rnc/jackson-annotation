package org.goots.jackson.deserializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.redhat.cpaas.pipeline.model.component.Component;
import com.redhat.cpaas.pipeline.model.component.build.Build;
import com.redhat.cpaas.pipeline.model.component.build.CEKitBuild;
import org.junit.Test;

public class JacksonPostHookDeserializerTest
{

    private String json = "{\n" +
                    "  \"type\" : \"container\",\n" +
                    "  \"owners\" : null,\n" +
                    "  \"name\" : \"eap-7-cd\",\n" +
                    "  \"builds\" : [ {\n" +
                    "    \"type\" : \"cekit\",\n" +
                    "    \"scratch\" : false,\n" +
                    "    \"force\" : false,\n" +
                    "    \"timeout\" : 120,\n" +
                    "    \"name\" : \"rhel8\",\n" +
                    "    \"description\" : null,\n" +
                    "    \"artifacts\" : [ ],\n" +
//                    "    \"brew-package\" : \"\",\n" +
                    //            "    \"brew-package\" : \"jboss-eap-7-eap-cd-openshift-rhel8-container\",\n" +
                    // "    \"brew-target\" : \"jb-eap-cd-openshift-dev-rhel-8-containers-candidate\",\n" +
                    "    \"brew-flags\" : null,\n" +
                    "    \"brew-source\" : {\n" +
                    "      \"repo\" : \"containers/jboss-eap-7-tech-preview\",\n" +
                    "      \"ref\" : \"jb-eap-cd-openshift-dev-rhel-8\",\n" +
                    "      \"root\" : null\n" +
                    "    },\n" +
                    "    \"cekit-version\" : null,\n" +
                    "    \"cekit-flags\" : null,\n" +
                    "    \"cekit-source\" : {\n" +
                    "      \"repo\" : \"https://github.com/jboss-container-images/jboss-eap-7-openshift-image.git\",\n" +
                    "      \"ref\" : \"eap-cd-dev\",\n" +
                    "      \"root\" : null\n" +
                    "    }\n" +
                    "  } ],\n" +
                    "  \"image-type\" : \"layered\",\n" +
                    "  \"host-level-access\" : \"unprivileged\",\n" +
                    "  \"summary\" : \"Platform for building and running JavaEE applications on JBoss EAP continuous delivery (JBoss EAP CD) release stream\",\n" +
                    "  \"description\" : \"JBoss EAP Continuous Delivery\",\n" +
                    "  \"display-name\" : \"JBoss EAP CD\"\n" +
                    "}";

    @Test
    public void testDeserialize() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy( PropertyNamingStrategy.KEBAB_CASE);
        mapper.registerModule(JacksonPostHookDeserializer.getSimpleModule());

        Component c = mapper.readValue( json, Component.class);
        System.out.println ("### Unit test ; iterating builds");
        for ( Build b : c.builds )
        {
            System.out.println ("Build name is " + b.name + " and " + b.getClass() + " component " + b.component);
            CEKitBuild cb = ((CEKitBuild)b);

            System.out.println (" ### BEFORE SETUP package is " + cb.brewPackage + " and target is " + cb.brewTarget );
            //            cb.setup()
            //            System.out.println (" ### AFTER SETUP package is " + cb.brewPackage + " and target " + cb.brewTarget + " component " + b.component)

        }
    }

}