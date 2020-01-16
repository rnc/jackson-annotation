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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.redhat.cpaas.pipeline.model.component.Component;
import com.redhat.cpaas.pipeline.model.component.build.Build;
import com.redhat.cpaas.pipeline.model.component.build.CEKitBuild;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings( "FieldCanBeLocal" )
public class JacksonPostHookDeserializerTest
{
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();

    private String jsonPre1 = "{\n" +
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
                                    "    \"artifacts\" : [ ],\n" ;

    private String jsonBrewPkg = "    \"brew-package\" : \"jboss-eap-7-eap-cd-openshift-rhel8-container\",\n";

    private String jsonBrewTarget = "    \"brew-target\" : \"jb-eap-cd-openshift-dev-rhel-8-containers-candidate\",\n";

    private String jsonPost2 = "    \"brew-flags\" : null,\n" +
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

    private ObjectMapper mapper = null;

    @Before
    public void before()
    {
        mapper = new ObjectMapper().setPropertyNamingStrategy( PropertyNamingStrategy.KEBAB_CASE );
    }

    @Test
    public void testDeserializeWithoutHook() throws Exception
    {
        String json = jsonPre1 + jsonPost2;
        Component c = mapper.readValue( json, Component.class );

        assertEquals( 1, c.builds.size() );
        Build b = c.builds.get( 0 );
        CEKitBuild cb = ( (CEKitBuild) b );

        assertNull( cb.brewPackage );
        assertNull( cb.brewTarget );

        assertFalse( systemOutRule.getLog().contains( "Invoking method postDeserialize" ) );
    }

    @Test
    public void testDeserialize1() throws Exception
    {
        mapper.registerModule( JacksonPostHookDeserializer.getSimpleModule() );
        String json = jsonPre1 + jsonPost2;
        Component c = mapper.readValue( json, Component.class );

        assertEquals( 1, c.builds.size() );
        Build b = c.builds.get( 0 );
        CEKitBuild cb = ( (CEKitBuild) b );

        assertEquals( "jboss-eap-7-tech-preview-container", cb.brewPackage );
        assertEquals( "jb-eap-cd-openshift-dev-rhel-8-candidate", cb.brewTarget );

        assertTrue( systemOutRule.getLog().contains( "Invoking method postDeserialize" ) );
    }

    @Test
    public void testDeserialize2() throws Exception
    {
        mapper.registerModule( JacksonPostHookDeserializer.getSimpleModule() );
        String json = jsonPre1 + jsonBrewPkg + jsonBrewTarget + jsonPost2;
        Component c = mapper.readValue( json, Component.class );

        assertEquals( 1, c.builds.size() );
        Build b = c.builds.get( 0 );
        CEKitBuild cb = ( (CEKitBuild) b );

        assertEquals( "jboss-eap-7-eap-cd-openshift-rhel8-container", cb.brewPackage );
        assertEquals( "jb-eap-cd-openshift-dev-rhel-8-containers-candidate", cb.brewTarget );

        assertTrue( systemOutRule.getLog().contains( "Invoking method postDeserialize" ) );
    }
}