package io.github.demirmustafa.kinesisconsumer.runner;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerStartupRunner {

    private final Worker worker;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady(){
        worker.run();
    }

}
