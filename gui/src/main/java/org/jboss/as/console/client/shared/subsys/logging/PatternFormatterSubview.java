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
import org.jboss.as.console.client.shared.subsys.logging.model.PatternFormatter;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns.NameColumn;
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
public class PatternFormatterSubview extends AbstractEntityView<PatternFormatter> implements FrameworkView {

    private EntityToDmrBridge<PatternFormatter> formatterBridge;

    public PatternFormatterSubview(ApplicationMetaData applicationMetaData, DispatchAsync dispatcher) {
        super(PatternFormatter.class, applicationMetaData);
        formatterBridge = new EntityToDmrBridgeImpl<PatternFormatter>(applicationMetaData, PatternFormatter.class, this, dispatcher);
    }

    protected String provideDescription() {
        return Console.CONSTANTS.subsys_logging_patternFormatter_desc();
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
        return Console.CONSTANTS.subsys_logging_pattern_formatter();
    }

    @Override
    protected FormAdapter<PatternFormatter> makeAddEntityForm() {
        Form<PatternFormatter> form = new Form<PatternFormatter>(PatternFormatter.class);
        form.setNumColumns(1);
        form.setFields(formMetaData.findAttribute("name").getFormItemForAdd(),
                formMetaData.findAttribute("pattern").getFormItemForAdd(),
                formMetaData.findAttribute("colorMap").getFormItemForAdd());
        return form;
    }

    @Override
    protected DefaultCellTable<PatternFormatter> makeEntityTable() {
        DefaultCellTable<PatternFormatter> table = new DefaultCellTable<PatternFormatter>(4);

        table.addColumn(new NameColumn(), NameColumn.LABEL);

        table.addColumn(new TextColumn<PatternFormatter>() {
            @Override
            public String getValue(final PatternFormatter record) {
                return record.getPattern();
            }
        }, Console.CONSTANTS.subsys_logging_pattern());

        return table;
    }

    @Override
    protected List<SingleEntityView<PatternFormatter>> provideAdditionalTabs(
            Class<?> beanType,
            FormMetaData formMetaData,
            FrameworkPresenter presenter) {

        List<SingleEntityView<PatternFormatter>> additionalTabs =
                new ArrayList<SingleEntityView<PatternFormatter>>();

        return additionalTabs;
    }
}
