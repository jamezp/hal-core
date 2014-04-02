/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.console.client.shared.subsys.logging.model;

import java.util.List;

import org.jboss.as.console.client.shared.properties.PropertyRecord;
import org.jboss.as.console.client.shared.viewframework.HasProperties;
import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Address("/subsystem=logging/custom-formatter={0}")
public interface CustomFormatter extends NamedEntity, HasProperties {

    @Override
    @Binding(detypedName = "name", key = true)
    @FormItem(defaultValue = "",
            localLabel = "common_label_name",
            required = true,
            formItemTypeForEdit = "TEXT",
            formItemTypeForAdd = "TEXT_BOX")
    public String getName();

    @Override
    public void setName(String name);

    @Binding(detypedName="module")
    @FormItem(defaultValue="",
            localLabel="subsys_logging_module",
            required=true,
            formItemTypeForEdit="TEXT",
            formItemTypeForAdd="TEXT_BOX",
            order=500)
    public String getModule();
    public void setModule(String module);

    @Binding(detypedName="class")
    @FormItem(defaultValue="",
            localLabel="subsys_logging_className",
            required=true,
            formItemTypeForEdit="TEXT",
            formItemTypeForAdd="TEXT_BOX",
            order=501)
    public String getClassName();
    public void setClassName(String className);

    @Override
    @Binding(detypedName = "properties",
            listType = "org.jboss.as.console.client.shared.properties.PropertyRecord")
    @FormItem(defaultValue = "",
            localLabel = "common_label_properties",
            required = false,
            formItemTypeForEdit = "PROPERTY_EDITOR",
            formItemTypeForAdd = "PROPERTY_EDITOR",
            tabName = "CUSTOM")
    public List<PropertyRecord> getProperties();

    void setProperties(List<PropertyRecord> properties);
}
