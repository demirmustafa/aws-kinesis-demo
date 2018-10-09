package io.github.demirmustafa.kinesisconsumer.core;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import io.github.demirmustafa.core.model.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
public class RecordProcessor implements IRecordProcessor {

    public void initialize(InitializationInput initializationInput) {

    }

    public void processRecords(ProcessRecordsInput processRecordsInput) {

        Function<Record, UserEvent> record2Event = (record) -> {
            String sequenceNumber = record.getSequenceNumber();
            String partitionKey = record.getPartitionKey();
            UserEvent event = (UserEvent) SerializationUtils.deserialize(record.getData().array());
            log.info("user event: {}, sequenceNumber: {}, partitionKey: {}", event.toString(), sequenceNumber, partitionKey);
            return event;
        };

        try {
            processRecordsInput.getRecords()
                    .forEach(record -> record2Event.apply(record));

        } catch (Exception e) {
            log.error("An unexpected error occurred when consuming kinesis stream", e);
        }
    }

    public void shutdown(ShutdownInput shutdownInput) {
        log.info("Kinesis consumer is shutdown.Reason[{}]", shutdownInput.getShutdownReason());

        if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
            try {
                shutdownInput.getCheckpointer().checkpoint();
            } catch (InvalidStateException e) {
                log.error("Cannot update consumer offset to the DynamoDB table.", e);
            } catch (ShutdownException e) {
                log.error("Consumer Shutting down", e);
            }
        }
    }
}
