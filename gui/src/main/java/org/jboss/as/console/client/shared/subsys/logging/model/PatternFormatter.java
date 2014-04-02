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

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Address("/subsystem=logging/pattern-formatter={0}")
public interface PatternFormatter extends NamedEntity {

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

    @Binding(detypedName = "pattern")
    @FormItem(defaultValue = "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n",
            localLabel = "subsys_logging_pattern",
            required = true,
            formItemTypeForEdit = "FREE_FORM_TEXT_BOX",
            formItemTypeForAdd = "FREE_FORM_TEXT_BOX")
    public String getPattern();

    public void setPattern(String pattern);

    @Binding(detypedName = "color-map")
    @FormItem(defaultValue = "",
            localLabel = "subsys_logging_color_map",
            required = true,
            formItemTypeForEdit = "FREE_FORM_TEXT_BOX",
            formItemTypeForAdd = "FREE_FORM_TEXT_BOX")
    public String getColorMap();

    public void setColorMap(String colorMap);
}
