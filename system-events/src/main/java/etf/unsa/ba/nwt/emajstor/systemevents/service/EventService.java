package etf.unsa.ba.nwt.emajstor.systemevents.service;

import etf.unsa.ba.nwt.emajstor.systemevents.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.systemevents.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.systemevents.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.systemevents.model.Event;
import etf.unsa.ba.nwt.emajstor.systemevents.model.enums.ActionType;
import etf.unsa.ba.nwt.emajstor.systemevents.repository.EventRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class EventService extends EventServiceGrpc.EventServiceImplBase{
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event add(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void log(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        Event event = eventRepository.save(new Event(
                request.getMicroservice(),
                request.getUser(),
                ActionType.valueOf(request.getAction().name()),
                request.getResource(),
                Integer.parseInt(request.getStatus())
        ));

        EventResponse response = EventResponse.newBuilder()
                .setMessage(event.toString())
                .setStatus("1")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
