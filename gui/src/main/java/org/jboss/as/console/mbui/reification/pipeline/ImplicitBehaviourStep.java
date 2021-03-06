package org.jboss.as.console.mbui.reification.pipeline;

import org.jboss.ballroom.client.rbac.SecurityContext;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.ModelNode;
import org.useware.kernel.gui.behaviour.ProcedureExecution;
import org.jboss.as.console.mbui.behaviour.DMROperationProcedure;
import org.jboss.as.console.mbui.behaviour.LoadResourceProcedure;
import org.jboss.as.console.mbui.behaviour.SaveChangesetProcedure;
import org.useware.kernel.gui.reification.Context;
import org.useware.kernel.gui.reification.ContextKey;
import org.useware.kernel.gui.reification.ReificationException;
import org.useware.kernel.gui.reification.pipeline.ReificationStep;
import org.useware.kernel.model.Dialog;
import org.useware.kernel.model.behaviour.Resource;
import org.useware.kernel.model.behaviour.ResourceType;
import org.useware.kernel.model.structure.Container;
import org.useware.kernel.model.structure.InteractionUnit;
import org.useware.kernel.model.structure.QName;
import org.jboss.as.console.mbui.model.StereoTypes;
import org.useware.kernel.model.structure.builder.InteractionUnitVisitor;

import java.util.Collections;
import java.util.Map;

/**
 * Naive implementation to register the implicit behaviour (Procedure) with the coordinator.
 *
 * @author Heiko Braun
 * @date 2/21/13
 */
public class ImplicitBehaviourStep extends ReificationStep
{
    private final DispatchAsync dispatcher;

    public ImplicitBehaviourStep(DispatchAsync dispatcher)
    {
        super("implicit behaviour");
        this.dispatcher = dispatcher;
    }

    @Override
    public void execute(final Dialog dialog, final Context context) throws ReificationException
    {
        final ProcedureExecution procedureExecution = context.get(ContextKey.COORDINATOR);

        InteractionUnit<StereoTypes> root = dialog.getInterfaceModel();

        root.accept(new InteractionUnitVisitor()
        {

            @Override
            public void startVisit(Container container)
            {
                registerDefaultBehaviour(dialog, container, procedureExecution, context);
            }

            @Override
            public void visit(InteractionUnit interactionUnit)
            {
                registerDefaultBehaviour(dialog, interactionUnit, procedureExecution, context);
            }

            @Override
            public void endVisit(Container container)
            {

            }
        });
    }

    private void registerDefaultBehaviour(
            Dialog dialog,
            InteractionUnit<StereoTypes> unit,
            ProcedureExecution procedureExecution,
            Context context) {


        Map<QName, ModelNode> operationDescriptions = null;

        final SecurityContext securityContext = context.get(ContextKey.SECURITY_CONTEXT);
        assert securityContext!=null : "Security context is missing";

        if(context.has(ContextKey.OPERATION_DESCRIPTIONS))
            operationDescriptions = context.get(ContextKey.OPERATION_DESCRIPTIONS);
        else
            operationDescriptions = Collections.EMPTY_MAP;

        // map consumers to outputs of interaction units
        if(unit.doesProduce())
        {
            for(Resource<ResourceType> output : unit.getOutputs())
            {
                if(LoadResourceProcedure.ID.equals(output.getId()))
                {
                    procedureExecution.addProcedure(
                            new LoadResourceProcedure(dialog, unit.getId(), dispatcher)
                    );
                }
                else if(SaveChangesetProcedure.ID.equals(output.getId()))
                {
                    procedureExecution.addProcedure(
                            new SaveChangesetProcedure(dialog, unit.getId(), dispatcher)
                    );
                }
                else if(DMROperationProcedure.PREFIX.equalsIgnoreSuffix(output.getId()))
                {
                    procedureExecution.addProcedure(
                            new DMROperationProcedure(dialog, output.getId(), unit.getId(), dispatcher, operationDescriptions, securityContext)
                    );
                }
            }
        }

        // map producers to inputs of interaction units
        if(unit.doesConsume())
        {
            for(Resource<ResourceType> input : unit.getInputs())
            {
                // Some of these inputs are implicitly satisfied with the procedures registered as consumers above ...
                // Apart from that, there are currently none known behaviours to be registered
            }
        }
    }
}
