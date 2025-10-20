package eci.edu.dosw.taller.services;

import eci.edu.dosw.taller.models.DatabaseSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Objects;

/**
 * Clase servicio que genera el numero sequencial para la receta
 */
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongo;

    public SequenceGeneratorService(MongoOperations mongo) {
        this.mongo = mongo;
    }

    public int getNextSequence(String seqName) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        DatabaseSequence counter = mongo.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );
        if (Objects.isNull(counter)) {
            throw new RuntimeException("No se pudo generar la secuencia para: " + seqName);
        }
        return (int) counter.getSeq();
    }
}
