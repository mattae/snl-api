package io.github.jbella.snl.core.api.id;

import com.github.f4b6a3.uuid.UuidCreator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class UUIDV7Generator implements IdentifierGenerator {
    public static final String GENERATOR = "UUIDV7Generator";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UuidCreator.getTimeOrderedEpoch();
    }
}