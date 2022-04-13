package etf.unsa.ba.nwt.emajstor.user.event;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.37.0)",
        comments = "Source: EventService.proto")
public final class EventServiceGrpc {

    private EventServiceGrpc() {}

    public static final String SERVICE_NAME = "EventService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<EventRequest,
            EventResponse> getLogMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "log",
            requestType = EventRequest.class,
            responseType = EventResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<EventRequest,
            EventResponse> getLogMethod() {
        io.grpc.MethodDescriptor<EventRequest, EventResponse> getLogMethod;
        if ((getLogMethod = EventServiceGrpc.getLogMethod) == null) {
            synchronized (EventServiceGrpc.class) {
                if ((getLogMethod = EventServiceGrpc.getLogMethod) == null) {
                    EventServiceGrpc.getLogMethod = getLogMethod =
                            io.grpc.MethodDescriptor.<EventRequest, EventResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "log"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            EventRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            EventResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new EventServiceMethodDescriptorSupplier("log"))
                                    .build();
                }
            }
        }
        return getLogMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static EventServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EventServiceStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EventServiceStub>() {
                    @Override
                    public EventServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EventServiceStub(channel, callOptions);
                    }
                };
        return EventServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static EventServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EventServiceBlockingStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EventServiceBlockingStub>() {
                    @Override
                    public EventServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EventServiceBlockingStub(channel, callOptions);
                    }
                };
        return EventServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static EventServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<EventServiceFutureStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<EventServiceFutureStub>() {
                    @Override
                    public EventServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new EventServiceFutureStub(channel, callOptions);
                    }
                };
        return EventServiceFutureStub.newStub(factory, channel);
    }

    /**
     */
    public static abstract class EventServiceImplBase implements io.grpc.BindableService {

        /**
         */
        public void log(EventRequest request,
                        io.grpc.stub.StreamObserver<EventResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLogMethod(), responseObserver);
        }

        @Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getLogMethod(),
                            io.grpc.stub.ServerCalls.asyncUnaryCall(
                                    new MethodHandlers<
                                            EventRequest,
                                            EventResponse>(
                                            this, METHODID_LOG)))
                    .build();
        }
    }

    /**
     */
    public static final class EventServiceStub extends io.grpc.stub.AbstractAsyncStub<EventServiceStub> {
        private EventServiceStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EventServiceStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EventServiceStub(channel, callOptions);
        }

        /**
         */
        public void log(EventRequest request,
                        io.grpc.stub.StreamObserver<EventResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getLogMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     */
    public static final class EventServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<EventServiceBlockingStub> {
        private EventServiceBlockingStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EventServiceBlockingStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EventServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public EventResponse log(EventRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getLogMethod(), getCallOptions(), request);
        }
    }

    /**
     */
    public static final class EventServiceFutureStub extends io.grpc.stub.AbstractFutureStub<EventServiceFutureStub> {
        private EventServiceFutureStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected EventServiceFutureStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new EventServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<EventResponse> log(
                EventRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(
                    getChannel().newCall(getLogMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_LOG = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final EventServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(EventServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_LOG:
                    serviceImpl.log((EventRequest) request,
                            (io.grpc.stub.StreamObserver<EventResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class EventServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        EventServiceBaseDescriptorSupplier() {}

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return EventServiceOuterClass.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("EventService");
        }
    }

    private static final class EventServiceFileDescriptorSupplier
            extends EventServiceBaseDescriptorSupplier {
        EventServiceFileDescriptorSupplier() {}
    }

    private static final class EventServiceMethodDescriptorSupplier
            extends EventServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        EventServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (EventServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new EventServiceFileDescriptorSupplier())
                            .addMethod(getLogMethod())
                            .build();
                }
            }
        }
        return result;
    }
}

