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
package deserializer

import com.redhat.cpaas.pipeline.model.component.Component
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemOutRule

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class JobsSpecTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog()//.muteForSuccessfulTests()

    @Test
    void testGroovyDeserialize() throws Exception {

        def component = SchemaUtils.dataToObject(Component.class, """
                {
                    "name": "c1",
                    "type": "container",
                    "display-name": "virt",
                    "builds": [
                        {
                            "name": "b1",
                            "type": "brew",
                            "timeout": 1234,
                            "brew-source" : {
                                "repo" : "w/x",
                                "ref" : "y",
                                "root" : null
                            }
                        }
                    ]
                }
            """)

        assertEquals(1, component.builds.size())
        component.builds.get(0)

        assertTrue(systemOutRule.getLog().contains("Invoking method postDeserialize"))
    }
}