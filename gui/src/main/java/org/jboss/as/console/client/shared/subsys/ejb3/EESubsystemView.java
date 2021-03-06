package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.ProductConfig;
import org.jboss.as.console.client.core.SuspendableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;
import org.jboss.as.console.client.widgets.tabs.DefaultTabLayoutPanel;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.Property;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 11/28/11
 */
public class EESubsystemView extends SuspendableViewImpl implements EEPresenter.MyView {

    private EEPresenter presenter;
    private EEModulesView moduleView;
    private EEGlobalAttributesView attributesView;
    private EEServicesView servicesView;
    private BindingsView bindingsView;

    ProductConfig productConfig = GWT.create(ProductConfig.class);

    @Override
    public Widget createWidget() {

        moduleView = new EEModulesView(presenter);
        attributesView = new EEGlobalAttributesView(presenter);

        DefaultTabLayoutPanel tabLayoutpanel = new DefaultTabLayoutPanel(40, Style.Unit.PX);
        tabLayoutpanel.addStyleName("default-tabpanel");

        OneToOneLayout layout = new OneToOneLayout()
                .setPlain(true)
                .setHeadline("Global EE Settings")
                .setDescription(Console.CONSTANTS.subsys_ee_desc())
                .addDetail("Deployments", attributesView.asWidget())
                .addDetail("Global Modules", moduleView.asWidget());

        if(!isLegacyView()) {
            bindingsView = new BindingsView(presenter);
            layout.addDetail("Default Bindings", bindingsView.asWidget());
        }

        tabLayoutpanel.add(layout.build(), "EE Subsystem", true);

        if(!isLegacyView()) {
            servicesView = new EEServicesView(presenter);
            tabLayoutpanel.add(servicesView.asWidget(), "Services", true);
        }

        tabLayoutpanel.selectTab(0);

        return tabLayoutpanel;
    }

    // see  https://issues.jboss.org/browse/HAL-491
    // This can be removed as soon as the EE subsystem is uptodate in both branches
    public boolean isLegacyView() {
        return !productConfig.getProfile().equals(ProductConfig.Profile.COMMUNITY)
                || productConfig.getProductName().equals("EAP");
    }

    @Override
    public void setPresenter(EEPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateFrom(ModelNode data) {
        attributesView.setData(data);
    }

    @Override
    public void setModules(List<ModelNode> modules) {
        moduleView.setModules(modules);
    }

    @Override
    public void setContextServices(List<Property> services) {
        servicesView.setContextServices(services);
    }

    @Override
    public void setThreadFactories(List<Property> data) {
        servicesView.setThreadFactories(data);
    }

    @Override
    public void setExecutor(List<Property> data) {
        servicesView.setExecutor(data);
    }

    @Override
    public void setScheduledExecutor(List<Property> data) {
        servicesView.setScheduledExecutor(data);
    }

    public void setBindings(ModelNode data) {
        bindingsView.setData(data);
    }
}
