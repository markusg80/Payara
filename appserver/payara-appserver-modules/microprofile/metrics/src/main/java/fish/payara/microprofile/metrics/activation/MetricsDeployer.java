/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) [2020] Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package fish.payara.microprofile.metrics.activation;

import java.util.Collection;
import java.util.function.Supplier;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.glassfish.api.deployment.DeploymentContext;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.weld.WeldDeployer;
import org.jvnet.hk2.annotations.Service;

import fish.payara.microprofile.connector.MicroProfileDeployer;
import fish.payara.microprofile.metrics.MetricsService;
import fish.payara.microprofile.metrics.cdi.extension.MetricCDIExtension;
import fish.payara.microprofile.metrics.impl.MetricsServiceImpl;

@Service
@PerLookup
public class MetricsDeployer extends MicroProfileDeployer<MetricsContainer, MetricsApplicationContainer> {

    @Inject
    private MetricsServiceImpl metricsService;

    @Override
    @SuppressWarnings("unchecked")
    public MetricsApplicationContainer load(MetricsContainer container,
            DeploymentContext deploymentContext) {

        // Register the CDI extension
        Collection<Supplier<Extension>> snifferExtensions = deploymentContext.getTransientAppMetaData(WeldDeployer.SNIFFER_EXTENSIONS, Collection.class);
        if (snifferExtensions != null) {
            snifferExtensions.add(MetricCDIExtension::new);
        }

        return new MetricsApplicationContainer(metricsService, deploymentContext);
    }

    @Override
    public void unload(MetricsApplicationContainer applicationContainer, DeploymentContext ctx) {
    }

}