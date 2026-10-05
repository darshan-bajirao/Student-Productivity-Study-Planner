package service;
import model.AppData;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;

public final class DataStore {
    private static final Path FILE=Paths.get("data","student_planner.dat");
    private DataStore(){}

    public static AppData load(){
        try{
            Files.createDirectories(FILE.getParent());
            if(!Files.exists(FILE)) return new AppData();
            try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(FILE))){
                return (AppData)in.readObject();
            }
        }catch(Exception e){
            System.err.println("Could not load saved data. A fresh workspace will be used: "+e.getMessage());
            return new AppData();
        }
    }

    public static void save(AppData d){
        try{
            Files.createDirectories(FILE.getParent());
            Path tmp=Paths.get(FILE+".tmp");
            try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(tmp))){out.writeObject(d);}
            Files.move(tmp,FILE,StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){throw new RuntimeException("Could not save application data.",e);}
    }

    public static String hashPassword(String password){
        try{
            byte[] bytes=MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            for(byte b:bytes) sb.append(String.format("%02x",b));
            return sb.toString();
        }catch(Exception e){throw new IllegalStateException(e);}
    }
}
