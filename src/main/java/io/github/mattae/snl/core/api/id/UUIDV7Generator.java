package io.github.mattae.snl.core.api.id;

import com.github.f4b6a3.uuid.UuidCreator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

import java.io.Serializable;
import java.lang.reflect.Member;

public class UUIDV7Generator implements IdentifierGenerator {

    public UUIDV7Generator(UUIDV7 config, Member idMember, CustomIdGeneratorCreationContext creationContext) {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
