package org.jboss.as.console.client.shared.subsys.activemq.cluster;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.layout.MultipleToOneLayout;
import org.jboss.as.console.client.shared.subsys.activemq.forms.BroadcastGroupForm;
import org.jboss.as.console.client.shared.subsys.activemq.model.ActivemqBroadcastGroup;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;

import java.util.List;
import java.util.Map;

/**
 * @author Heiko Braun
 * @date 4/2/12
 */
public class BroadcastGroupList {

    private ContentHeaderLabel serverName;
    private DefaultCellTable<ActivemqBroadcastGroup> factoryTable;
    private ListDataProvider<ActivemqBroadcastGroup> factoryProvider;
    private MsgClusteringPresenter presenter;
    private BroadcastGroupForm defaultAttributes;

    public BroadcastGroupList(MsgClusteringPresenter presenter) {
        this.presenter = presenter;
    }

    @SuppressWarnings("unchecked")
    Widget asWidget() {
        serverName = new ContentHeaderLabel();

        factoryTable = new DefaultCellTable<>(10, ActivemqBroadcastGroup::getName);
        factoryProvider = new ListDataProvider<>();
        factoryProvider.addDataDisplay(factoryTable);

        Column<ActivemqBroadcastGroup, String> nameColumn = new Column<ActivemqBroadcastGroup, String>(new TextCell()) {
            @Override
            public String getValue(ActivemqBroadcastGroup object) {
                return object.getName();
            }
        };

        factoryTable.addColumn(nameColumn, "Name");

        // defaultAttributes
        defaultAttributes = new BroadcastGroupForm(new FormToolStrip.FormCallback<ActivemqBroadcastGroup>() {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.saveBroadcastGroup(getSelectedEntity().getName(), changeset);
            }

            @Override
            public void onDelete(ActivemqBroadcastGroup entity) {}
        });

        ToolStrip tools = new ToolStrip();
        tools.addToolButtonRight(
                new ToolButton(Console.CONSTANTS.common_label_add(),
                        clickEvent -> presenter.launchNewBroadcastGroupWizard()));

        tools.addToolButtonRight(
                new ToolButton(Console.CONSTANTS.common_label_remove(), clickEvent -> Feedback.confirm(
                        Console.MESSAGES.deleteTitle("BroadcastGroup"),
                        Console.MESSAGES.deleteConfirm("BroadcastGroup " + getSelectedEntity().getName()),
                        isConfirmed -> {
                            if (isConfirmed) {
                                presenter.onDeleteBroadcastGroup(getSelectedEntity().getName());
                            }
                        })));

        MultipleToOneLayout layout = new MultipleToOneLayout()
                .setPlain(true)
                .setHeadlineWidget(serverName)
                .setDescription(
                        "A broadcast group is the means by which a server broadcasts connectors over the network. A connector defines a way in which a client (or other server) can make connections to the server.")
                .setMaster("BroadcastGroups", factoryTable)
                .setMasterTools(tools)
                .setDetail("Details", defaultAttributes.asWidget());

        defaultAttributes.getForm().bind(factoryTable);
        defaultAttributes.getForm().setEnabled(false);

        return layout.build();
    }

    public void setBroadcastGroups(List<ActivemqBroadcastGroup> BroadcastGroups) {
        factoryProvider.setList(BroadcastGroups);
        serverName.setText("BroadcastGroups: Provider " + presenter.getCurrentServer());

        factoryTable.selectDefaultEntity();

        // populate oracle
        presenter.loadExistingSocketBindings(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(List<String> names) {
                defaultAttributes.setSocketBindings(names);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public ActivemqBroadcastGroup getSelectedEntity() {
        SingleSelectionModel<ActivemqBroadcastGroup> selectionModel = (SingleSelectionModel<ActivemqBroadcastGroup>) factoryTable
                .getSelectionModel();
        return selectionModel.getSelectedObject();
    }
}
