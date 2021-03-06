/*
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.hub.api.rest.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.apicurio.hub.api.Version;
import io.apicurio.hub.api.beans.SystemStatus;
import io.apicurio.hub.api.rest.ISystemResource;
import test.io.apicurio.hub.api.MockSecurityContext;
import test.io.apicurio.hub.api.MockStorage;
import test.io.apicurio.hub.api.TestUtil;

/**
 * @author eric.wittmann@gmail.com
 */
public class SystemResourceTest {
    
    private MockStorage storage;
    private ISystemResource resource;

    @Before
    public void setUp() {
        storage = new MockStorage();
        resource = new SystemResource();
        Version version = new Version();
        version.load();
        TestUtil.setPrivateField(resource, "storage", storage);
        TestUtil.setPrivateField(resource, "version", version);
        TestUtil.setPrivateField(resource, "security", new MockSecurityContext());
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testStatus() {
        SystemStatus status = resource.getStatus();
        Assert.assertNotNull(status);
        Assert.assertEquals("Apicurio Studio Hub API", status.getName());
        Assert.assertEquals(true, status.isUp());
    }

}
