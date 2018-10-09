package io.github.demirmustafa.kinesisconsumer.core;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordProcessorFactory implements IRecordProcessorFactory {

    private final RecordProcessor recordProcessor;

    public IRecordProcessor createProcessor() {
        return  recordProcessor;
    }
}
