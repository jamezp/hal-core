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

package org.jboss.as.console.client.shared.subsys.logging;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.subsys.logging.model.CustomFormatter;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns.NameColumn;
import org.jboss.as.console.client.shared.viewframework.EmbeddedPropertyView;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkPresenter;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.shared.viewframework.SingleEntityView;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.dmr.client.dispatch.DispatchAsync;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class CustomFormatterSubview extends AbstractEntityView<CustomFormatter> implements FrameworkView {

    private final EntityToDmrBridge<CustomFormatter> formatterBridge;

    public CustomFormatterSubview(ApplicationMetaData applicationMetaData, DispatchAsync dispatcher) {
        super(CustomFormatter.class, applicationMetaData);
        formatterBridge = new EntityToDmrBridgeImpl<CustomFormatter>(applicationMetaData, CustomFormatter.class, this, dispatcher);
    }

    protected String provideDescription() {
        return Console.CONSTANTS.subsys_logging_customFormatter_desc();
    }

    @Override
    public Widget createWidget() {
        setDescription(provideDescription());
        Widget widget = super.createEmbeddableWidget();
        return widget;
    }

    @Override
    public EntityToDmrBridge getEntityBridge() {
        return this.formatterBridge;
    }

    @Override
    protected String getEntityDisplayName() {
        return Console.CONSTANTS.subsys_logging_custom_formatter();
    }

    @Override
    protected FormAdapter<CustomFormatter> makeAddEntityForm() {
        Form<CustomFormatter> form = new Form<CustomFormatter>(CustomFormatter.class);
        form.setNumColumns(1);
        form.setFields(formMetaData.findAttribute("name").getFormItemForAdd(),
                formMetaData.findAttribute("module").getFormItemForAdd(),
                formMetaData.findAttribute("className").getFormItemForAdd());
        return form;
    }

    @Override
    protected DefaultCellTable<CustomFormatter> makeEntityTable() {
        DefaultCellTable<CustomFormatter> table = new DefaultCellTable<CustomFormatter>(4);

        table.addColumn(new NameColumn(), NameColumn.LABEL);

        table.addColumn(new TextColumn<CustomFormatter>() {
            @Override
            public String getValue(final CustomFormatter record) {
                return record.getClassName();
            }
        }, Console.CONSTANTS.subsys_logging_className());

        return table;
    }

    @Override
    protected List<SingleEntityView<CustomFormatter>> provideAdditionalTabs(
            Class<?> beanType,
            FormMetaData formMetaData,
            FrameworkPresenter presenter) {

        List<SingleEntityView<CustomFormatter>> additionalTabs =
                new ArrayList<SingleEntityView<CustomFormatter>>();

        final SingleEntityView propertiesBridge = new EmbeddedPropertyView<CustomFormatter, CustomFormatter>(new FrameworkPresenter() {
            @Override
            public EntityToDmrBridge getEntityBridge() {
                return CustomFormatterSubview.this.getEntityBridge();
            }
        });
        additionalTabs.add(propertiesBridge);

        return additionalTabs;
    }
}
