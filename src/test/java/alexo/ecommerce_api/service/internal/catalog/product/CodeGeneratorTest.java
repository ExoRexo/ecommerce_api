package alexo.ecommerce_api.service.internal.catalog.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeGeneratorTest {

    @Test
    void shouldGenerateProductCodeWithExpectedFormat() {
        String code = new CodeGenerator().generateCode();

        assertThat(code)
                .startsWith("PRD-")
                .hasSize(16)
                .matches("PRD-[0-9A-Z]{12}");
    }

    @Test
    void shouldGenerateDifferentCodesAcrossCalls() {
        CodeGenerator generator = new CodeGenerator();

        assertThat(generator.generateCode()).isNotEqualTo(generator.generateCode());
    }
}
