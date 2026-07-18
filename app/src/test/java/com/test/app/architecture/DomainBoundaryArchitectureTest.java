package com.test.app.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Executable enforcement of the brief's dependency rules. These run in the app module because it is
 * the one place with every domain on the classpath. Break a rule (e.g. import an enrollments class
 * from catalog-core) and the build fails here — not in code review.
 */
@AnalyzeClasses(packages = "com.test", importOptions = ImportOption.DoNotIncludeTests.class)
class DomainBoundaryArchitectureTest {

    /**
     * The headline rule: no domain's {@code -core} may depend on another domain's {@code -core}.
     * Cross-domain traffic is only ever allowed through an {@code -api} module.
     */
    @ArchTest
    static final ArchRule cores_do_not_depend_on_each_other = slices()
            .matching("com.test.(*).core..")
            .should().notDependOnEachOther()
            .as("a -core module must not depend on another domain's -core (cross only via -api)");

    /**
     * {@code -api} modules are pure contracts: JDK + {@code common} only. No Spring, no JPA.
     * This is what keeps domain internals off consumers' classpaths.
     */
    @ArchTest
    static final ArchRule api_modules_are_framework_free = noClasses()
            .that().resideInAPackage("com.test..api..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.persistence..",
                    "org.hibernate..")
            .as("-api modules must stay framework-free (no Spring, no JPA)");

    /**
     * JPA entities never cross a module boundary — they must live inside a {@code -core}. Every
     * boundary crossing is a DTO, so no {@code @Entity} may appear in an {@code -api} module.
     */
    @ArchTest
    static final ArchRule entities_stay_inside_core = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("com.test..core..")
            .as("JPA entities must never leave a -core module");
}
