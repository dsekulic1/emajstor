package etf.unsa.ba.nwt.emajstor.systemevents;

import etf.unsa.ba.nwt.emajstor.systemevents.repository.EventRepository;
import etf.unsa.ba.nwt.emajstor.systemevents.service.EventService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SystemEventsApplication {
	private static EventRepository eventRepository;

	private static int grpcPort;

	@Value("${app.grpc-port}")
	public void setGrpcPort(int grpcPort) {
		SystemEventsApplication.grpcPort = grpcPort;
	}

	public SystemEventsApplication(EventRepository eventRepository) {
		SystemEventsApplication.eventRepository = eventRepository;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(SystemEventsApplication.class, args);

		Server server = ServerBuilder
				.forPort(grpcPort)
				.addService(new EventService(SystemEventsApplication.eventRepository))
				.build();
		server.start();
		System.out.println("gRPC server runing on port " + server.getPort());
		server.awaitTermination();
	}

}
