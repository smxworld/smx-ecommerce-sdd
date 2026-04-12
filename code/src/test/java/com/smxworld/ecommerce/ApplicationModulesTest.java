package com.smxworld.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ApplicationModulesTest {

    static final ApplicationModules modules =
            ApplicationModules.of(SmxECommerceApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void generateDocumentation() {
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
