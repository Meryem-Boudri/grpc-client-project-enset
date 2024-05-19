package ma.enset.Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.org.sid.grpc.stubs.Bank;
import ma.enset.org.sid.grpc.stubs.BankServiceGrpc;

import java.io.IOException;

public class BankGrpcClient1 {
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
      // Bank.ConvertCurrencyResponse convert = blockingStub.convert(request);
     asyncStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {
         @Override
         public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
             System.out.println("*****************");
             System.out.println(convertCurrencyResponse);
             System.out.println("*****************");

         }

         @Override
         public void onError(Throwable throwable) {
             System.out.println("*****************");

             System.out.println(throwable);
         }

         @Override
         public void onCompleted() {

             System.out.println("END...");

         }
     });
        System.out.println(".............?");
        System.in.read();



    }
}