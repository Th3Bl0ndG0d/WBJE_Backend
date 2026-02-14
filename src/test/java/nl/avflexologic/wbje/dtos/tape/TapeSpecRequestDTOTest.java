package nl.avflexologic.wbje.dtos.tape;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TapeSpecRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void valid_dto_should_have_no_violations() {
        TapeSpecRequestDTO dto =
                new TapeSpecRequestDTO("TESSA", "PET", 120, "Info");

        Set<ConstraintViolation<TapeSpecRequestDTO>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}
