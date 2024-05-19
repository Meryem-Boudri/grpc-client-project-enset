package ma.enset.Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.org.sid.grpc.stubs.Bank;
import ma.enset.org.sid.grpc.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcClient5 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5555)
                .usePlaintext()
                .build();
      //  BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);//synchrone
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);//asynchrone

        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(6500)
                .setCurrencyFrom("usd")
                .setCurrencyTo("mad")
                .build();

        StreamObserver<Bank.ConvertCurrencyRequest> performStream =
                asyncStub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
          //reponse
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("**************");
                System.out.println(convertCurrencyResponse);
                System.out.println("**************");

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("END...");

            }
        });
        Timer timer=new Timer();

        //envoi
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {

                Bank.ConvertCurrencyRequest currencyRequest=Bank.ConvertCurrencyRequest.newBuilder()
                                .setAmount(Math.random()*7000)
                                        .build();
                performStream.onNext(currencyRequest);
                ++counter;
                if(counter==20) {
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
        performStream.onNext(request);

        System.out.println(".............?");
        System.in.read();



    }
}